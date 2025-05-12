package com.fitness.auth.models;

import java.util.ArrayList;
import java.util.List;

public class WorkoutRoutine {
    private int routineId;
    private final int userId;
    private String name;
    private String type;
    private final List<Exercise> exercises;

    public WorkoutRoutine(int userId, String name, String type) {
        this.routineId = 0; // Will be set after DB insert
        this.userId = userId;
        this.name = name;
        this.type = type;
        this.exercises = new ArrayList<>();
    }

    public WorkoutRoutine(int routineId, int userId, String name, String type, List<Exercise> exercises) {
        this.routineId = routineId;
        this.userId = userId;
        this.name = name;
        this.type = type;
        this.exercises = new ArrayList<>(exercises);
    }

    // Getters
    public int getRoutineId() { return routineId; }
    public int getUserId() { return userId; }
    public String getName() { return name; }
    public String getType() { return type; }
    public List<Exercise> getExercises() { return new ArrayList<>(exercises); }

    // Setters
    public void setRoutineId(int routineId) { this.routineId = routineId; }
    public void setName(String name) { this.name = name; }
    public void setType(String type) { this.type = type; }

    // Methods to manage exercises
    public void addExercise(Exercise exercise) {
        exercises.add(exercise);
    }

    public void removeExercise(Exercise exercise) {
        exercises.remove(exercise);
    }

    public void clearExercises() {
        exercises.clear();
    }

    @Override
    public String toString() {
        return "WorkoutRoutine{" +
                "routineId=" + routineId +
                ", userId=" + userId +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", exercises=" + exercises.size() +
                '}';
    }
}
