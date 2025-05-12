package com.fitness.auth.models;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class WorkoutScheduleDAO {
    private final Connection connection;

    public WorkoutScheduleDAO(Connection connection) {
        this.connection = connection;
    }

    public List<WorkoutSchedule> getScheduledWorkouts(int userId, LocalDate startDate, LocalDate endDate) throws SQLException {
        List<WorkoutSchedule> schedules = new ArrayList<>();
        String sql = "SELECT * FROM workout_schedules WHERE user_id = ? AND date BETWEEN ? AND ? ORDER BY date";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setDate(2, Date.valueOf(startDate));
            stmt.setDate(3, Date.valueOf(endDate));
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    schedules.add(new WorkoutSchedule(
                        rs.getInt("schedule_id"),
                        rs.getInt("user_id"),
                        rs.getDate("date").toLocalDate(),
                        rs.getString("workout_type"),
                        rs.getString("notes")
                    ));
                }
            }
        }
        
        return schedules;
    }

    public void saveSchedule(WorkoutSchedule schedule) throws SQLException {
        String sql = "INSERT INTO workout_schedules (user_id, date, workout_type, notes) VALUES (?, ?, ?, ?)";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, schedule.getUserId());
            stmt.setDate(2, Date.valueOf(schedule.getDate()));
            stmt.setString(3, schedule.getWorkoutType());
            stmt.setString(4, schedule.getNotes());
            stmt.executeUpdate();
            
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    schedule.setScheduleId(generatedKeys.getInt(1));
                }
            }
        }
    }

    public void updateSchedule(WorkoutSchedule schedule) throws SQLException {
        String sql = "UPDATE workout_schedules SET date = ?, workout_type = ?, notes = ? WHERE schedule_id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setDate(1, Date.valueOf(schedule.getDate()));
            stmt.setString(2, schedule.getWorkoutType());
            stmt.setString(3, schedule.getNotes());
            stmt.setInt(4, schedule.getScheduleId());
            stmt.executeUpdate();
        }
    }

    public void deleteSchedule(int scheduleId) throws SQLException {
        String sql = "DELETE FROM workout_schedules WHERE schedule_id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, scheduleId);
            stmt.executeUpdate();
        }
    }

    public void saveMesocycle(int userId, MesocycleManager mesocycle) throws SQLException {
        String sql = "INSERT INTO mesocycles (user_id, duration_weeks, start_date, auto_deload) " +
                    "VALUES (?, ?, ?, ?) " +
                    "ON CONFLICT (user_id) DO UPDATE SET " +
                    "duration_weeks = excluded.duration_weeks, " +
                    "start_date = excluded.start_date, " +
                    "auto_deload = excluded.auto_deload";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, mesocycle.getDurationWeeks());
            stmt.setDate(3, Date.valueOf(mesocycle.getStartDate()));
            stmt.setBoolean(4, mesocycle.isAutoDeload());
            stmt.executeUpdate();
        }
    }

    public Optional<MesocycleManager> loadMesocycle(int userId) throws SQLException {
        String sql = "SELECT * FROM mesocycles WHERE user_id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    MesocycleManager mesocycle = new MesocycleManager(
                        rs.getInt("duration_weeks"),
                        rs.getObject("start_date", LocalDate.class),
                        rs.getBoolean("auto_deload")
                    );
                    return Optional.of(mesocycle);
                }
            }
        }
        return Optional.empty();
    }

    private void saveSchedule(int userId, LocalDate date, List<Exercise> exercises) throws SQLException {
        String sql = "INSERT INTO workout_schedule (user_id, schedule_date, exercise_id) VALUES (?, ?, ?)";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            for (Exercise exercise : exercises) {
                stmt.setInt(1, userId);
                stmt.setDate(2, Date.valueOf(date));
                stmt.setInt(3, exercise.getExerciseId());
                stmt.addBatch();
            }
            stmt.executeBatch();
        }
    }

    public List<Exercise> getScheduledExercises(int userId, LocalDate date) throws SQLException {
        List<Exercise> exercises = new ArrayList<>();
        String sql = """
            SELECT e.* FROM workout_schedule ws
            JOIN exercises e ON ws.exercise_id = e.exercise_id
            WHERE ws.user_id = ? AND ws.schedule_date = ?
            ORDER BY e.name
        """;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setDate(2, Date.valueOf(date));
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Exercise exercise = new Exercise(
                    rs.getInt("exercise_id"),
                    rs.getString("name"),
                    rs.getString("description"),
                    rs.getString("muscle_group"),
                    ExerciseCategory.valueOf(rs.getString("category")),
                    rs.getString("equipment")
                );
                exercises.add(exercise);
            }
        }

        return exercises;
    }

    public void deleteSchedule(int userId, LocalDate date) throws SQLException {
        String sql = "DELETE FROM workout_schedule WHERE user_id = ? AND schedule_date = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setDate(2, Date.valueOf(date));
            stmt.executeUpdate();
        }
    }

    public void updateSchedule(int userId, LocalDate date, List<Exercise> exercises) throws SQLException {
        // First delete existing schedule
        deleteSchedule(userId, date);
        // Then save new schedule
        saveSchedule(userId, date, exercises);
    }

    public void saveSchedule(int userId, List<ScheduleEntry> schedule) throws SQLException {
        String deleteSql = "DELETE FROM workout_schedule WHERE user_id = ?";
        String insertSql = "INSERT INTO workout_schedule (user_id, day_of_week, workout_type, exercise_count, notes) " +
                          "VALUES (?, ?, ?, ?, ?)";

        try {
            connection.setAutoCommit(false);
            
            // Delete existing schedule
            try (PreparedStatement deleteStmt = connection.prepareStatement(deleteSql)) {
                deleteStmt.setInt(1, userId);
                deleteStmt.executeUpdate();
            }

            // Insert new schedule
            try (PreparedStatement insertStmt = connection.prepareStatement(insertSql)) {
                for (ScheduleEntry entry : schedule) {
                    insertStmt.setInt(1, userId);
                    insertStmt.setString(2, entry.getDay());
                    insertStmt.setString(3, entry.getWorkoutType());
                    insertStmt.setInt(4, entry.getExerciseCount());
                    insertStmt.setString(5, entry.getNotes());
                    insertStmt.addBatch();
                }
                insertStmt.executeBatch();
            }

            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            throw e;
        } finally {
            connection.setAutoCommit(true);
        }
    }

    public List<ScheduleEntry> loadSchedule(int userId) throws SQLException {
        List<ScheduleEntry> schedule = new ArrayList<>();
        String sql = "SELECT * FROM workout_schedule WHERE user_id = ? ORDER BY day_of_week";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    ScheduleEntry entry = new ScheduleEntry(
                        rs.getString("day_of_week"),
                        rs.getString("workout_type"),
                        rs.getInt("exercise_count"),
                        rs.getString("notes")
                    );
                    schedule.add(entry);
                }
            }
        }
        return schedule;
    }
}
