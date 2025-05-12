package com.fitness.auth.models;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class WorkoutSession {
    private int sessionId;
    private final int userId;
    private String name;
    private LocalDateTime date;
    private String notes;
    private final List<Workout> workouts;
    private Integer routineId; // Optional, can be null

    public WorkoutSession(int userId, String name) {
        this.sessionId = 0; // Will be set after DB insert
        this.userId = userId;
        this.name = name;
        this.date = LocalDateTime.now();
        this.notes = "";
        this.workouts = new ArrayList<>();
        this.routineId = null;
    }

    public WorkoutSession(int sessionId, int userId, String name, LocalDateTime date, 
                         String notes, List<Workout> workouts, Integer routineId) {
        this.sessionId = sessionId;
        this.userId = userId;
        this.name = name;
        this.date = date;
        this.notes = notes;
        this.workouts = new ArrayList<>(workouts);
        this.routineId = routineId;
    }

    // Getters
    public int getSessionId() { return sessionId; }
    public int getUserId() { return userId; }
    public String getName() { return name; }
    public LocalDateTime getDate() { return date; }
    public String getNotes() { return notes; }
    public List<Workout> getWorkouts() { return new ArrayList<>(workouts); }
    public Integer getRoutineId() { return routineId; }

    // Setters
    public void setSessionId(int sessionId) { this.sessionId = sessionId; }
    public void setName(String name) { this.name = name; }
    public void setNotes(String notes) { this.notes = notes; }
    public void setRoutineId(Integer routineId) { this.routineId = routineId; }
    public void setDate(LocalDate date) { this.date = date.atStartOfDay(); }

    // Methods to manage workouts
    public void addWorkout(Workout workout) {
        workouts.add(workout);
    }

    public void removeWorkout(Workout workout) {
        workouts.remove(workout);
    }

    public void clearWorkouts() {
        workouts.clear();
    }

    @Override
    public String toString() {
        return String.format("Session[%d] %s on %s", sessionId, name, date.toLocalDate());
    }
}
