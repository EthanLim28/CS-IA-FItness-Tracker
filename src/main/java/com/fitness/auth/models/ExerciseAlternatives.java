package com.fitness.auth.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExerciseAlternatives {
    private static final Map<String, List<String>> alternativeExercises = new HashMap<>();

    static {
        // Chest exercises
        alternativeExercises.put("Bench Press", List.of(
            "Dumbbell Press",
            "Push-Ups",
            "Incline Bench Press",
            "Machine Chest Press"
        ));

        // Back exercises
        alternativeExercises.put("Pull-ups", List.of(
            "Lat Pulldown",
            "Assisted Pull-ups",
            "Inverted Rows",
            "Machine Pull-downs"
        ));

        // Leg exercises
        alternativeExercises.put("Squat", List.of(
            "Leg Press",
            "Bulgarian Split Squats",
            "Hack Squat",
            "Goblet Squat"
        ));

        // Shoulder exercises
        alternativeExercises.put("Overhead Press", List.of(
            "Dumbbell Shoulder Press",
            "Arnold Press",
            "Machine Shoulder Press",
            "Pike Push-ups"
        ));
    }

    public static List<String> getAlternatives(String exerciseName) {
        return alternativeExercises.getOrDefault(exerciseName, new ArrayList<>());
    }

    public static void addAlternative(String mainExercise, String alternativeExercise) {
        alternativeExercises.computeIfAbsent(mainExercise, k -> new ArrayList<>())
                          .add(alternativeExercise);
    }

    public static boolean hasAlternatives(String exerciseName) {
        return alternativeExercises.containsKey(exerciseName) && 
               !alternativeExercises.get(exerciseName).isEmpty();
    }

    public static Map<String, List<String>> getAllAlternatives() {
        return new HashMap<>(alternativeExercises);
    }

    public static void removeAlternative(String mainExercise, String alternativeExercise) {
        if (alternativeExercises.containsKey(mainExercise)) {
            alternativeExercises.get(mainExercise).remove(alternativeExercise);
        }
    }
}
