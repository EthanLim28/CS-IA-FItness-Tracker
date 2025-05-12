package com.fitness.auth.models;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class WorkoutDAO {
    private final Connection connection;

    public WorkoutDAO(Connection connection) {
        this.connection = connection;
    }

    public void saveWorkout(Workout workout) throws SQLException {
        String sql = "INSERT INTO workouts (user_id, date, type, notes) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, workout.getUserId());
            stmt.setDate(2, Date.valueOf(workout.getDate()));
            stmt.setString(3, workout.getWorkoutType());
            stmt.setString(4, workout.getNotes());
            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    workout.setWorkoutId(generatedKeys.getInt(1));
                }
            }
        }
    }

    public Optional<Workout> getWorkoutById(int workoutId) throws SQLException {
        String sql = "SELECT * FROM workouts WHERE workout_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, workoutId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(createWorkoutFromResultSet(rs));
                }
            }
        }
        return Optional.empty();
    }

    public List<Workout> getWorkoutsByUserId(int userId) throws SQLException {
        List<Workout> workouts = new ArrayList<>();
        String sql = "SELECT * FROM workouts WHERE user_id = ? ORDER BY date DESC";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    workouts.add(createWorkoutFromResultSet(rs));
                }
            }
        }
        return workouts;
    }

    public List<Workout> getWorkoutsByDateRange(int userId, LocalDate startDate, LocalDate endDate) throws SQLException {
        List<Workout> workouts = new ArrayList<>();
        String sql = "SELECT * FROM workouts WHERE user_id = ? AND date BETWEEN ? AND ? ORDER BY date";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setDate(2, Date.valueOf(startDate));
            stmt.setDate(3, Date.valueOf(endDate));
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    workouts.add(createWorkoutFromResultSet(rs));
                }
            }
        }
        return workouts;
    }

    public void updateWorkout(Workout workout) throws SQLException {
        String sql = "UPDATE workouts SET type = ?, notes = ? WHERE workout_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, workout.getWorkoutType());
            stmt.setString(2, workout.getNotes());
            stmt.setInt(3, workout.getWorkoutId());
            stmt.executeUpdate();
        }
    }

    public void deleteWorkout(int workoutId) throws SQLException {
        String sql = "DELETE FROM workouts WHERE workout_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, workoutId);
            stmt.executeUpdate();
        }
    }

    private Workout createWorkoutFromResultSet(ResultSet rs) throws SQLException {
        return new Workout(
            rs.getInt("workout_id"),
            rs.getInt("user_id"),
            rs.getDate("date").toLocalDate(),
            rs.getString("type"),
            rs.getString("notes")
        );
    }
}