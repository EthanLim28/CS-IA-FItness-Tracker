package com.fitness.auth.models;

import java.util.*;
import java.util.stream.Collectors;

public class ExerciseProgressionCalculator {
    private final List<DailyWorkout> workouts;
    private final Map<Exercise, List<WorkoutSet>> exerciseSets;
    private static final double DELOAD_THRESHOLD = 0.9; // 90% of max weight
    private static final double PROGRESSION_THRESHOLD = 1.1; // 110% of starting weight

    public ExerciseProgressionCalculator(List<DailyWorkout> workouts) {
        this.workouts = workouts;
        this.exerciseSets = new HashMap<>();
        processWorkouts();
    }

    private void processWorkouts() {
        for (DailyWorkout workout : workouts) {
            for (WorkoutSet set : workout.getSets()) {
                Exercise exercise = set.getExercise();
                exerciseSets.computeIfAbsent(exercise, k -> new ArrayList<>()).add(set);
            }
        }
    }

    public boolean isDeloadRecommended() {
        return !getExercisesNeedingDeload().isEmpty();
    }

    public List<Exercise> getExercisesNeedingDeload() {
        return exerciseSets.entrySet().stream()
            .filter(entry -> needsDeload(entry.getValue()))
            .map(Map.Entry::getKey)
            .collect(Collectors.toList());
    }

    private boolean needsDeload(List<WorkoutSet> sets) {
        if (sets.size() < 3) return false;
        
        double maxWeight = sets.stream()
            .mapToDouble(WorkoutSet::getWeight)
            .max()
            .orElse(0.0);
            
        double recentAverage = sets.subList(Math.max(0, sets.size() - 3), sets.size()).stream()
            .mapToDouble(WorkoutSet::getWeight)
            .average()
            .orElse(0.0);
            
        return recentAverage < (maxWeight * DELOAD_THRESHOLD);
    }

    public List<Exercise> getExercisesReadyForProgression() {
        return exerciseSets.entrySet().stream()
            .filter(entry -> readyForProgression(entry.getValue()))
            .map(Map.Entry::getKey)
            .collect(Collectors.toList());
    }

    private boolean readyForProgression(List<WorkoutSet> sets) {
        if (sets.size() < 3) return false;
        
        double startingWeight = sets.get(0).getWeight();
        double recentAverage = sets.subList(Math.max(0, sets.size() - 3), sets.size()).stream()
            .mapToDouble(WorkoutSet::getWeight)
            .average()
            .orElse(0.0);
            
        return recentAverage > (startingWeight * PROGRESSION_THRESHOLD);
    }

    public double getAverageIntensity() {
        return exerciseSets.values().stream()
            .flatMap(List::stream)
            .mapToDouble(set -> set.getWeight() / getMaxWeightForExercise(set.getExercise()))
            .average()
            .orElse(0.0);
    }

    private double getMaxWeightForExercise(Exercise exercise) {
        return exerciseSets.getOrDefault(exercise, Collections.emptyList()).stream()
            .mapToDouble(WorkoutSet::getWeight)
            .max()
            .orElse(1.0); // Avoid division by zero
    }

    public double getVolumeProgress() {
        if (workouts.isEmpty()) return 0.0;
        
        double firstVolume = calculateVolumeForWorkout(workouts.get(0));
        double lastVolume = calculateVolumeForWorkout(workouts.get(workouts.size() - 1));
        
        if (firstVolume == 0.0) return 0.0;
        return ((lastVolume - firstVolume) / firstVolume) * 100.0;
    }

    private double calculateVolumeForWorkout(DailyWorkout workout) {
        return workout.getSets().stream()
            .mapToDouble(set -> set.getWeight() * set.getReps())
            .sum();
    }
}
