package com.fitness.auth.models;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class WorkoutAnalytics {
    private final DailyWorkoutDAO dailyWorkoutDAO;

    public WorkoutAnalytics(DailyWorkoutDAO dailyWorkoutDAO) {
        this.dailyWorkoutDAO = dailyWorkoutDAO;
    }

    public Map<String, Long> getExerciseFrequencyByMuscleGroup(int userId) throws SQLException {
        List<DailyWorkout> workouts = dailyWorkoutDAO.getWorkoutHistory(userId);
        return workouts.stream()
                .flatMap(w -> w.getSets().stream())
                .filter(set -> set.getExercise() != null && set.getExercise().getMuscleGroup() != null)
                .map(set -> set.getExercise().getMuscleGroup())
                .collect(Collectors.groupingBy(
                        muscleGroup -> muscleGroup,
                        Collectors.counting()
                ));
    }

    public double getAverageWeightForExercise(int userId, int exerciseId) throws SQLException {
        List<DailyWorkout> workouts = dailyWorkoutDAO.getWorkoutHistory(userId);
        List<WorkoutSet> exerciseSets = workouts.stream()
                .flatMap(w -> w.getSets().stream())
                .filter(set -> set.getExercise() != null && 
                             set.getExercise().getExerciseId() == exerciseId)
                .collect(Collectors.toList());

        if (exerciseSets.isEmpty()) {
            return 0.0;
        }

        double totalWeight = exerciseSets.stream()
                .filter(set -> set.getWeight() > 0)
                .mapToDouble(WorkoutSet::getWeight)
                .sum();

        long totalSets = exerciseSets.stream()
                .filter(set -> set.getWeight() > 0)
                .count();

        return totalSets > 0 ? totalWeight / totalSets : 0.0;
    }

    public Map<Exercise, Double> getPersonalBests(int userId) throws SQLException {
        List<DailyWorkout> workouts = dailyWorkoutDAO.getWorkoutHistory(userId);
        
        return workouts.stream()
                .flatMap(w -> w.getSets().stream())
                .filter(set -> set.getExercise() != null)
                .collect(Collectors.groupingBy(
                    set -> set.getExercise(),
                    Collectors.mapping(
                        set -> set.getWeight(),
                        Collectors.maxBy(Double::compare)
                    )
                ))
                .entrySet().stream()
                .filter(e -> e.getValue().isPresent())
                .collect(Collectors.toMap(
                    Map.Entry::getKey,
                    e -> e.getValue().get()
                ));
    }

    public double calculateVolumeLoad(DailyWorkout workout) {
        if (workout == null || workout.getSets() == null) {
            return 0.0;
        }
        
        return workout.getSets().stream()
                .filter(set -> set != null && set.getWeight() > 0 && set.getReps() > 0)
                .mapToDouble(set -> set.getWeight() * set.getReps())
                .sum();
    }

    public double calculateTotalVolumeForPeriod(int userId, LocalDate startDate, LocalDate endDate) throws SQLException {
        if (startDate == null || endDate == null || startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Invalid date range");
        }

        List<DailyWorkout> workouts = dailyWorkoutDAO.getWorkoutHistory(userId);
        return workouts.stream()
                .filter(w -> w != null && w.getDate() != null)
                .filter(w -> !w.getDate().isBefore(startDate) && !w.getDate().isAfter(endDate))
                .mapToDouble(this::calculateVolumeLoad)
                .sum();
    }
}
