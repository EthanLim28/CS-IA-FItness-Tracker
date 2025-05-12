package com.fitness.auth.models;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DailyWorkoutDAO {
    private final Connection connection;

    public DailyWorkoutDAO(Connection connection) {
        this.connection = connection;
    }

    public Optional<DailyWorkout> getWorkoutForDate(LocalDate date) throws SQLException {
        String sql = "SELECT w.workout_id, w.user_id, w.workout_date, w.workout_type, w.notes FROM daily_workouts w WHERE w.workout_date = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setObject(1, date);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    DailyWorkout workout = new DailyWorkout(
                        rs.getInt("workout_id"),
                        rs.getInt("user_id"),
                        rs.getObject("workout_date", LocalDate.class),
                        rs.getString("workout_type"),
                        new ArrayList<>(),
                        rs.getString("notes")
                    );
                    
                    loadWorkoutSets(workout);
                    return Optional.of(workout);
                }
            }
        }
        return Optional.empty();
    }

    public List<DailyWorkout> getWorkoutHistory(int userId) throws SQLException {
        List<DailyWorkout> workouts = new ArrayList<>();
        String sql = "SELECT * FROM daily_workouts WHERE user_id = ? ORDER BY workout_date DESC";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    DailyWorkout workout = new DailyWorkout(
                        rs.getInt("workout_id"),
                        userId,
                        rs.getObject("workout_date", LocalDate.class),
                        rs.getString("workout_type"),
                        new ArrayList<>(),
                        rs.getString("notes")
                    );
                    loadWorkoutSets(workout);
                    workouts.add(workout);
                }
            }
        }
        return workouts;
    }

    public List<DailyWorkout> getWorkoutsForDateRange(int userId, LocalDate startDate, LocalDate endDate) throws SQLException {
        List<DailyWorkout> workouts = new ArrayList<>();
        String sql = "SELECT * FROM daily_workouts WHERE user_id = ? AND workout_date BETWEEN ? AND ? ORDER BY workout_date";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setObject(2, startDate);
            stmt.setObject(3, endDate);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    DailyWorkout workout = new DailyWorkout(
                        rs.getInt("workout_id"),
                        userId,
                        rs.getObject("workout_date", LocalDate.class),
                        rs.getString("workout_type"),
                        new ArrayList<>(),
                        rs.getString("notes")
                    );
                    loadWorkoutSets(workout);
                    workouts.add(workout);
                }
            }
        }
        return workouts;
    }

    private void loadWorkoutSets(DailyWorkout workout) throws SQLException {
        String sql = "SELECT s.*, e.* FROM workout_sets s " +
                    "JOIN exercises e ON s.exercise_id = e.exercise_id " +
                    "WHERE s.workout_id = ? ORDER BY s.set_number";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, workout.getWorkoutId());
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Exercise exercise = new Exercise(
                        rs.getInt("exercise_id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getString("muscle_group"),
                        ExerciseCategory.valueOf(rs.getString("category")),
                        rs.getString("equipment")
                    );
                    
                    WorkoutSet set = new WorkoutSet(
                        exercise,
                        rs.getInt("set_number"),
                        rs.getInt("reps"),
                        rs.getDouble("weight"),
                        rs.getString("notes")
                    );
                    
                    workout.addSet(set);
                }
            }
        }
    }

    public void saveWorkout(DailyWorkout workout) throws SQLException {
        connection.setAutoCommit(false);
        try {
            if (workout.getWorkoutId() == 0) {
                // Insert new workout
                String sql = "INSERT INTO daily_workouts (user_id, workout_date, workout_type, notes) VALUES (?, ?, ?, ?)";
                try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                    stmt.setInt(1, workout.getUserId());
                    stmt.setObject(2, workout.getDate());
                    stmt.setString(3, workout.getWorkoutType());
                    stmt.setString(4, workout.getNotes());
                    
                    stmt.executeUpdate();
                    try (ResultSet rs = stmt.getGeneratedKeys()) {
                        if (rs.next()) {
                            workout.setWorkoutId(rs.getInt(1));
                        }
                    }
                }
            } else {
                // Update existing workout
                String sql = "UPDATE daily_workouts SET user_id = ?, workout_date = ?, workout_type = ?, notes = ? WHERE workout_id = ?";
                try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                    stmt.setInt(1, workout.getUserId());
                    stmt.setObject(2, workout.getDate());
                    stmt.setString(3, workout.getWorkoutType());
                    stmt.setString(4, workout.getNotes());
                    stmt.setInt(5, workout.getWorkoutId());
                    stmt.executeUpdate();
                }
            }

            // Delete existing sets
            try (PreparedStatement stmt = connection.prepareStatement("DELETE FROM workout_sets WHERE workout_id = ?")) {
                stmt.setInt(1, workout.getWorkoutId());
                stmt.executeUpdate();
            }

            // Insert new sets
            String setSql = "INSERT INTO workout_sets (workout_id, exercise_id, set_number, reps, weight, notes) VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = connection.prepareStatement(setSql)) {
                for (WorkoutSet set : workout.getSets()) {
                    stmt.setInt(1, workout.getWorkoutId());
                    stmt.setInt(2, set.getExercise().getExerciseId());
                    stmt.setInt(3, set.getSetNumber());
                    stmt.setInt(4, set.getReps());
                    stmt.setDouble(5, set.getWeight());
                    stmt.setString(6, set.getNotes());
                    stmt.executeUpdate();
                }
            }

            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            throw e;
        } finally {
            connection.setAutoCommit(true);
        }
    }

    public void saveDailyWorkout(DailyWorkout workout) throws SQLException {
        saveWorkout(workout); // For backward compatibility
    }
}
