package com.fitness.auth.models;

import java.time.LocalDate;

public class WorkoutSchedule {
    private int scheduleId;
    private int userId;
    private LocalDate date;
    private String workoutType;
    private String notes;

    public WorkoutSchedule(int scheduleId, int userId, LocalDate date, String workoutType, String notes) {
        this.scheduleId = scheduleId;
        this.userId = userId;
        this.date = date;
        this.workoutType = workoutType;
        this.notes = notes;
    }

    public int getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(int scheduleId) {
        this.scheduleId = scheduleId;
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
}
