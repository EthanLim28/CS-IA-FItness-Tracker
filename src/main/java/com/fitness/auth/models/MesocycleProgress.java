package com.fitness.auth.models;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

public class MesocycleProgress {
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final List<DailyWorkout> workouts;
    private final ExerciseProgressionCalculator calculator;
    private int week;
    private String status;
    private boolean deload;
    private String notes;

    public MesocycleProgress(int userId, LocalDate startDate, LocalDate endDate, List<DailyWorkout> workouts, int week, String status, boolean deload, String notes) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.workouts = workouts.stream()
            .filter(w -> !w.getDate().isBefore(startDate) && !w.getDate().isAfter(endDate))
            .collect(Collectors.toList());
        this.calculator = new ExerciseProgressionCalculator(this.workouts);
        this.week = week;
        this.status = status;
        this.deload = deload;
        this.notes = notes;
    }

    public double getCompletionPercentage() {
        long totalDays = ChronoUnit.DAYS.between(startDate, endDate);
        long daysCompleted = ChronoUnit.DAYS.between(startDate, LocalDate.now());
        return Math.min(100.0, (daysCompleted * 100.0) / totalDays);
    }

    public boolean isDeloadWeekRecommended() {
        return calculator.isDeloadRecommended();
    }

    public List<Exercise> getExercisesNeedingDeload() {
        return calculator.getExercisesNeedingDeload();
    }

    public List<Exercise> getExercisesReadyForProgression() {
        return calculator.getExercisesReadyForProgression();
    }

    public double getAverageIntensity() {
        return calculator.getAverageIntensity();
    }

    public double getVolumeProgress() {
        return calculator.getVolumeProgress();
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public List<DailyWorkout> getWorkouts() {
        return workouts;
    }

    public int getWeek() {
        return week;
    }

    public void setWeek(int week) {
        this.week = week;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isDeload() {
        return deload;
    }

    public void setDeload(boolean deload) {
        this.deload = deload;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
