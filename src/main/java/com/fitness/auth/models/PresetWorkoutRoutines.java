package com.fitness.auth.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PresetWorkoutRoutines {
    private static final Map<String, List<String>> presetRoutines = new HashMap<>();

    static {
        // Push Pull Legs (PPL)
        presetRoutines.put("Push Day", List.of(
            "Bench Press",
            "Overhead Press",
            "Incline Dumbbell Press",
            "Lateral Raises",
            "Tricep Pushdowns",
            "Dips"
        ));

        presetRoutines.put("Pull Day", List.of(
            "Deadlift",
            "Pull-ups",
            "Barbell Rows",
            "Face Pulls",
            "Bicep Curls",
            "Hammer Curls"
        ));

        presetRoutines.put("Legs Day", List.of(
            "Squat",
            "Romanian Deadlift",
            "Leg Press",
            "Leg Extensions",
            "Leg Curls",
            "Calf Raises"
        ));

        // Upper Lower Split
        presetRoutines.put("Upper Body", List.of(
            "Bench Press",
            "Pull-ups",
            "Overhead Press",
            "Rows",
            "Lateral Raises",
            "Tricep Extensions",
            "Bicep Curls"
        ));

        presetRoutines.put("Lower Body", List.of(
            "Squat",
            "Romanian Deadlift",
            "Leg Press",
            "Hip Thrusts",
            "Leg Extensions",
            "Leg Curls",
            "Standing Calf Raises"
        ));

        // Full Body
        presetRoutines.put("Full Body", List.of(
            "Squat",
            "Bench Press",
            "Deadlift",
            "Overhead Press",
            "Pull-ups",
            "Dips"
        ));
    }

    public static List<String> getRoutineExercises(String routineName) {
        return new ArrayList<>(presetRoutines.getOrDefault(routineName, new ArrayList<>()));
    }

    public static List<String> getAvailableRoutines() {
        return new ArrayList<>(presetRoutines.keySet());
    }

    public static boolean hasRoutine(String routineName) {
        return presetRoutines.containsKey(routineName);
    }

    public static void addCustomRoutine(String routineName, List<String> exercises) {
        presetRoutines.put(routineName, new ArrayList<>(exercises));
    }

    public static void removeRoutine(String routineName) {
        presetRoutines.remove(routineName);
    }

    public static void addExerciseToRoutine(String routineName, String exercise) {
        if (presetRoutines.containsKey(routineName)) {
            presetRoutines.get(routineName).add(exercise);
        }
    }

    public static void removeExerciseFromRoutine(String routineName, String exercise) {
        if (presetRoutines.containsKey(routineName)) {
            presetRoutines.get(routineName).remove(exercise);
        }
    }
}
