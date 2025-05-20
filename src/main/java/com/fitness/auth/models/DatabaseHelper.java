package com.fitness.auth.models;

import java.sql.*;

public class DatabaseHelper {
    private static final String DB_URL = "jdbc:sqlite:fitness.db";

    public static void initializeDatabase() {
        try (Connection conn = getConnection()) {
            createTables(conn);
            insertDefaultExercises(conn);
        } catch (SQLException e) {
            System.err.println("Database initialization failed: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private static void createTables(Connection conn) throws SQLException {
        try (Statement stmt = conn.createStatement()) {
            // Enable foreign key support
            stmt.execute("PRAGMA foreign_keys = ON");

            // Drop tables in reverse order of dependencies
            stmt.execute("DROP TABLE IF EXISTS workout_schedules");
            stmt.execute("DROP TABLE IF EXISTS workouts");
            stmt.execute("DROP TABLE IF EXISTS workout_sessions");
            stmt.execute("DROP TABLE IF EXISTS workout_routines");
            stmt.execute("DROP TABLE IF EXISTS exercises");
            stmt.execute("DROP TABLE IF EXISTS reset_tokens");
            stmt.execute("DROP TABLE IF EXISTS users");

            // Users table
            String createUsersTable = """
                CREATE TABLE IF NOT EXISTS users (
                    user_id INTEGER PRIMARY KEY AUTOINCREMENT,
                    username TEXT NOT NULL UNIQUE,
                    email TEXT NOT NULL UNIQUE,
                    password_hash TEXT NOT NULL,
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                )
            """;
            stmt.execute(createUsersTable);

            // Reset Tokens table
            String createResetTokensTable = """
                CREATE TABLE IF NOT EXISTS reset_tokens (
                    token_id INTEGER PRIMARY KEY AUTOINCREMENT,
                    user_id INTEGER NOT NULL,
                    token TEXT UNIQUE NOT NULL,
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    expires_at TIMESTAMP NOT NULL,
                    used BOOLEAN DEFAULT FALSE,
                    FOREIGN KEY (user_id) REFERENCES users (user_id)
                        ON DELETE CASCADE
                )
            """;
            stmt.execute(createResetTokensTable);

            // Exercises table
            String createExercisesTable = """
                CREATE TABLE IF NOT EXISTS exercises (
                    exercise_id INTEGER PRIMARY KEY AUTOINCREMENT,
                    name TEXT NOT NULL UNIQUE,
                    muscle_group TEXT NOT NULL,
                    description TEXT,
                    equipment TEXT
                )
            """;
            stmt.execute(createExercisesTable);

            // Workout Routines table
            String createWorkoutRoutinesTable = """
                CREATE TABLE IF NOT EXISTS workout_routines (
                    routine_id INTEGER PRIMARY KEY AUTOINCREMENT,
                    user_id INTEGER NOT NULL,
                    name TEXT NOT NULL,
                    type TEXT NOT NULL,
                    FOREIGN KEY (user_id) REFERENCES users (user_id)
                        ON DELETE CASCADE
                )
            """;
            stmt.execute(createWorkoutRoutinesTable);

            // Workout Sessions table
            String createWorkoutSessionsTable = """
                CREATE TABLE IF NOT EXISTS workout_sessions (
                    session_id INTEGER PRIMARY KEY AUTOINCREMENT,
                    user_id INTEGER NOT NULL,
                    name TEXT NOT NULL,
                    date TEXT NOT NULL,
                    notes TEXT,
                    routine_id INTEGER,
                    FOREIGN KEY (user_id) REFERENCES users (user_id)
                        ON DELETE CASCADE,
                    FOREIGN KEY (routine_id) REFERENCES workout_routines (routine_id)
                        ON DELETE SET NULL
                )
            """;
            stmt.execute(createWorkoutSessionsTable);

            // Workouts table
            String createWorkoutsTable = """
                CREATE TABLE IF NOT EXISTS workouts (
                    workout_id INTEGER PRIMARY KEY AUTOINCREMENT,
                    user_id INTEGER NOT NULL,
                    session_id INTEGER,
                    exercise_id INTEGER NOT NULL,
                    sets INTEGER NOT NULL,
                    reps INTEGER NOT NULL,
                    weight REAL NOT NULL,
                    date TEXT NOT NULL,
                    notes TEXT,
                    FOREIGN KEY (user_id) REFERENCES users (user_id)
                        ON DELETE CASCADE,
                    FOREIGN KEY (exercise_id) REFERENCES exercises (exercise_id)
                        ON DELETE CASCADE,
                    FOREIGN KEY (session_id) REFERENCES workout_sessions (session_id)
                        ON DELETE SET NULL
                )
            """;
            stmt.execute(createWorkoutsTable);

            // Workout Schedules table
            String createWorkoutSchedulesTable = """
                CREATE TABLE IF NOT EXISTS workout_schedules (
                    schedule_id INTEGER PRIMARY KEY AUTOINCREMENT,
                    user_id INTEGER NOT NULL,
                    day_of_week INTEGER NOT NULL,
                    routine_id INTEGER NOT NULL,
                    FOREIGN KEY (user_id) REFERENCES users (user_id)
                        ON DELETE CASCADE,
                    FOREIGN KEY (routine_id) REFERENCES workout_routines (routine_id)
                        ON DELETE CASCADE,
                    UNIQUE(user_id, day_of_week)
                )
            """;
            stmt.execute(createWorkoutSchedulesTable);

            // Create indexes
            String[] indexes = {
                    "CREATE INDEX IF NOT EXISTS idx_workouts_user_id ON workouts (user_id)",
                    "CREATE INDEX IF NOT EXISTS idx_workouts_session_id ON workouts (session_id)",
                    "CREATE INDEX IF NOT EXISTS idx_workouts_exercise_id ON workouts (exercise_id)",
                    "CREATE INDEX IF NOT EXISTS idx_workout_sessions_user_id ON workout_sessions (user_id)",
                    "CREATE INDEX IF NOT EXISTS idx_workout_sessions_date ON workout_sessions (date)",
                    "CREATE INDEX IF NOT EXISTS idx_workout_sessions_routine_id ON workout_sessions (routine_id)",
                    "CREATE INDEX IF NOT EXISTS idx_workout_routines_user_id ON workout_routines (user_id)",
                    "CREATE INDEX IF NOT EXISTS idx_reset_tokens_user_id ON reset_tokens (user_id)",
                    "CREATE INDEX IF NOT EXISTS idx_reset_tokens_token ON reset_tokens (token)",
                    "CREATE INDEX IF NOT EXISTS idx_workout_schedules_user_id ON workout_schedules (user_id)",
                    "CREATE INDEX IF NOT EXISTS idx_workout_schedules_routine_id ON workout_schedules (routine_id)"
            };

            for (String index : indexes) {
                stmt.execute(index);
            }
        }
    }

    private static void insertDefaultExercises(Connection conn) throws SQLException {
        String sql = "INSERT OR IGNORE INTO exercises (name, muscle_group, description, equipment) VALUES (?, ?, ?, ?)";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            // Array of default exercises
            Object[][] exercises = {
                    {"Bench Press", "Chest", "Compound movement for chest development", "Barbell, Bench"},
                    {"Squat", "Legs", "Fundamental lower body compound movement", "Barbell, Squat Rack"},
                    {"Deadlift", "Back", "Full body compound movement", "Barbell"},
                    {"Pull-ups", "Back", "Upper body pulling movement", "Pull-up Bar"},
                    {"Shoulder Press", "Shoulders", "Overhead pressing movement", "Dumbbells/Barbell"},
                    {"Bicep Curls", "Arms", "Isolation exercise for biceps", "Dumbbells"},
                    {"Tricep Extensions", "Arms", "Isolation exercise for triceps", "Dumbbells/Cable Machine"},
                    {"Leg Press", "Legs", "Machine-based leg exercise", "Leg Press Machine"},
                    {"Lat Pulldown", "Back", "Machine-based back exercise", "Cable Machine"},
                    {"Romanian Deadlift", "Legs", "Hip-hinge movement for hamstrings", "Barbell"}
            };

            for (Object[] exercise : exercises) {
                pstmt.setString(1, (String) exercise[0]);
                pstmt.setString(2, (String) exercise[1]);
                pstmt.setString(3, (String) exercise[2]);
                pstmt.setString(4, (String) exercise[3]);
                pstmt.executeUpdate();
            }
        }
    }

    public static void clearDatabase() {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            // Disable foreign key checks temporarily
            stmt.execute("PRAGMA foreign_keys = OFF");

            // Delete all data from tables in correct order
            stmt.execute("DELETE FROM workouts");
            stmt.execute("DELETE FROM workout_sessions");
            stmt.execute("DELETE FROM workout_schedules");
            stmt.execute("DELETE FROM workout_routines");
            stmt.execute("DELETE FROM exercises");
            stmt.execute("DELETE FROM reset_tokens");
            stmt.execute("DELETE FROM users");

            // Re-enable foreign key checks
            stmt.execute("PRAGMA foreign_keys = ON");
        } catch (SQLException e) {
            System.err.println("Database clearing failed: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }
}
