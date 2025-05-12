package com.fitness.auth.models;

public class ScheduleEntry {
    private String day;
    private String workoutType;
    private int exerciseCount;
    private String notes;

    public ScheduleEntry(String day, String workoutType, int exerciseCount, String notes) {
        this.day = day;
        this.workoutType = workoutType;
        this.exerciseCount = exerciseCount;
        this.notes = notes;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getWorkoutType() {
        return workoutType;
    }

    public void setWorkoutType(String workoutType) {
        this.workoutType = workoutType;
    }

    public int getExerciseCount() {
        return exerciseCount;
    }

    public void setExerciseCount(int exerciseCount) {
        this.exerciseCount = exerciseCount;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
