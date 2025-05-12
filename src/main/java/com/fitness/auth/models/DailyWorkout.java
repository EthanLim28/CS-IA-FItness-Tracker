package com.fitness.auth.models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DailyWorkout {
    private int workoutId;
    private int userId;
    private LocalDate date;
    private String workoutType;
    private ObservableList<WorkoutSet> workoutSets;
    private String notes;

    // Constructor for new workouts
    public DailyWorkout(int userId, LocalDate date) {
        this(0, userId, date, "", FXCollections.observableArrayList(new ArrayList<>()), "");
    }

    // Constructor for loading from database
    public DailyWorkout(int workoutId, int userId, LocalDate date, String workoutType, List<WorkoutSet> sets, String notes) {
        this.workoutId = workoutId;
        this.userId = userId;
        this.date = date;
        this.workoutType = workoutType != null ? workoutType : "";
        this.workoutSets = FXCollections.observableArrayList(sets != null ? sets : new ArrayList<>());
        this.notes = notes != null ? notes : "";
    }

    // Constructor for loading from database (without notes)
    public DailyWorkout(int workoutId, int userId, LocalDate date, String workoutType, List<WorkoutSet> sets) {
        this(workoutId, userId, date, workoutType, sets, "");
    }

    // Constructor with just date parameter
    public DailyWorkout(LocalDate date) {
        this(0, 0, date, "", FXCollections.observableArrayList(new ArrayList<>()), "");
    }

    public int getWorkoutId() {
        return workoutId;
    }

    public void setWorkoutId(int workoutId) {
        this.workoutId = workoutId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getWorkoutType() {
        return workoutType;
    }

    public void setWorkoutType(String workoutType) {
        this.workoutType = workoutType != null ? workoutType : "";
    }

    public ObservableList<WorkoutSet> getWorkoutSets() {
        return workoutSets;
    }

    public List<WorkoutSet> getSets() {
        return workoutSets;
    }

    public void setWorkoutSets(List<WorkoutSet> sets) {
        this.workoutSets.setAll(sets);
    }

    public void addSet(WorkoutSet set) {
        if (set != null) {
            this.workoutSets.add(set);
        }
    }

    public void addSets(List<WorkoutSet> newSets) {
        if (newSets != null) {
            newSets.forEach(this::addSet);
        }
    }

    public void removeSet(WorkoutSet set) {
        if (set != null) {
            this.workoutSets.remove(set);
        }
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes != null ? notes : "";
    }

    @Override
    public String toString() {
        return String.format("Workout on %s (%s)", date, workoutType);
    }
}
