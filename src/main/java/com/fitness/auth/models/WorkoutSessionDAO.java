package com.fitness.auth.models;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class WorkoutSessionDAO {
    
    public void createSession(WorkoutSession session) throws SQLException {
        String sql = "INSERT INTO workout_sessions (user_id, name, date, notes, routine_id) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, session.getUserId());
            pstmt.setString(2, session.getName());
            pstmt.setString(3, session.getDate().toString());
            pstmt.setString(4, session.getNotes());
            if (session.getRoutineId() != null) {
                pstmt.setInt(5, session.getRoutineId());
            } else {
                pstmt.setNull(5, Types.INTEGER);
            }

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating workout session failed, no rows affected.");
            }

            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT last_insert_rowid()")) {
                if (rs.next()) {
                    session.setSessionId(rs.getInt(1));
                }
            }
        }
    }

    public Optional<WorkoutSession> findById(int sessionId) throws SQLException {
        String sql = "SELECT * FROM workout_sessions WHERE session_id = ?";

        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, sessionId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToWorkoutSession(rs));
                }
            }
        }
        return Optional.empty();
    }

    public List<WorkoutSession> findByUserId(int userId) throws SQLException {
        String sql = "SELECT * FROM workout_sessions WHERE user_id = ? ORDER BY date DESC";
        List<WorkoutSession> sessions = new ArrayList<>();

        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    sessions.add(mapResultSetToWorkoutSession(rs));
                }
            }
        }
        return sessions;
    }

    public List<WorkoutSession> findByDateRange(int userId, LocalDateTime startDate, LocalDateTime endDate) throws SQLException {
        String sql = "SELECT * FROM workout_sessions WHERE user_id = ? AND date BETWEEN ? AND ? ORDER BY date DESC";
        List<WorkoutSession> sessions = new ArrayList<>();

        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            pstmt.setString(2, startDate.toString());
            pstmt.setString(3, endDate.toString());

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    sessions.add(mapResultSetToWorkoutSession(rs));
                }
            }
        }
        return sessions;
    }

    public void updateSession(WorkoutSession session) throws SQLException {
        String sql = "UPDATE workout_sessions SET name = ?, notes = ?, routine_id = ? WHERE session_id = ?";

        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, session.getName());
            pstmt.setString(2, session.getNotes());
            if (session.getRoutineId() != null) {
                pstmt.setInt(3, session.getRoutineId());
            } else {
                pstmt.setNull(3, Types.INTEGER);
            }
            pstmt.setInt(4, session.getSessionId());

            pstmt.executeUpdate();
        }
    }

    public void deleteSession(int sessionId) throws SQLException {
        String sql = "DELETE FROM workout_sessions WHERE session_id = ?";

        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, sessionId);
            pstmt.executeUpdate();
        }
    }

    private WorkoutSession mapResultSetToWorkoutSession(ResultSet rs) throws SQLException {
        Integer routineId = rs.getInt("routine_id");
        if (rs.wasNull()) {
            routineId = null;
        }

        return new WorkoutSession(
            rs.getInt("session_id"),
            rs.getInt("user_id"),
            rs.getString("name"),
            rs.getTimestamp("date").toLocalDateTime(),
            rs.getString("notes"),
            new ArrayList<>(), 
            routineId
        );
    }
}
