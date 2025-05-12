package com.fitness.auth.models;

public enum ExerciseCategory {
    WARMUP("Warm-up"),
    HYPERTROPHY("Hypertrophy"),
    STRENGTH("Strength"),
    DROP_SET("Drop Set"),
    CARDIO("Cardio"),
    FLEXIBILITY("Flexibility"),
    COMPOUND("Compound"),
    ISOLATION("Isolation");

    private final String displayName;

    ExerciseCategory(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }

    public static ExerciseCategory fromString(String text) {
        for (ExerciseCategory category : ExerciseCategory.values()) {
            if (category.displayName.equalsIgnoreCase(text)) {
                return category;
            }
        }
        throw new IllegalArgumentException("No constant with text " + text + " found");
    }
}
