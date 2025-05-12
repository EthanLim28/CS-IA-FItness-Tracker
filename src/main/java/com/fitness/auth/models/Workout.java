package com.fitness.auth.models;

import java.time.LocalDate;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Workout {
    private int workoutId;
    private int userId;
    private LocalDate date;
    private String workoutType;
    private String notes;
    private ObservableList<WorkoutSet> sets;

    public Workout(int workoutId, int userId, LocalDate date, String workoutType, String notes) {
        this.workoutId = workoutId;
        this.userId = userId;
        this.date = date;
        this.workoutType = workoutType;
        this.notes = notes;
        this.sets = FXCollections.observableArrayList();
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
        this.workoutType = workoutType;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public ObservableList<WorkoutSet> getSets() {
        return sets;
    }

    public void setSets(ObservableList<WorkoutSet> sets) {
        this.sets = sets;
    }

    public void addSet(WorkoutSet set) {
        sets.add(set);
    }

    public void removeSet(WorkoutSet set) {
        sets.remove(set);
    }
}
