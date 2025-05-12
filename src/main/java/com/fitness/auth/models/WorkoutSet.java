package com.fitness.auth.models;

import javafx.beans.property.*;

public class WorkoutSet {
    private final ObjectProperty<Exercise> exercise;
    private final IntegerProperty exerciseId;
    private final IntegerProperty reps;
    private final DoubleProperty weight;
    private final IntegerProperty setNumber;
    private final BooleanProperty completed;
    private final StringProperty notes;

    // Constructor for database loading
    public WorkoutSet(int exerciseId, int reps, double weight, int setNumber, boolean completed) {
        this.exercise = new SimpleObjectProperty<>();
        this.exerciseId = new SimpleIntegerProperty(exerciseId);
        this.reps = new SimpleIntegerProperty(reps);
        this.weight = new SimpleDoubleProperty(weight);
        this.setNumber = new SimpleIntegerProperty(setNumber);
        this.completed = new SimpleBooleanProperty(completed);
        this.notes = new SimpleStringProperty("");
    }

    // Constructor for UI usage
    public WorkoutSet(Exercise exercise, int setNumber, int reps, double weight, String notes) {
        this.exercise = new SimpleObjectProperty<>(exercise);
        this.exerciseId = new SimpleIntegerProperty(exercise.getExerciseId());
        this.setNumber = new SimpleIntegerProperty(setNumber);
        this.reps = new SimpleIntegerProperty(reps);
        this.weight = new SimpleDoubleProperty(weight);
        this.notes = new SimpleStringProperty(notes != null ? notes : "");
        this.completed = new SimpleBooleanProperty(false);
    }

    // Constructor for UI usage with nullable types
    public WorkoutSet(Exercise exercise, int setNumber, Integer reps, Double weight, String notes) {
        this(exercise, setNumber, reps != null ? reps : 0, weight != null ? weight : 0.0, notes);
    }

    public Exercise getExercise() {
        return exercise.get();
    }

    public void setExercise(Exercise value) {
        exercise.set(value);
        exerciseId.set(value.getExerciseId());
    }

    public ObjectProperty<Exercise> exerciseProperty() {
        return exercise;
    }

    public int getExerciseId() {
        return exerciseId.get();
    }

    public void setExerciseId(int value) {
        exerciseId.set(value);
    }

    public IntegerProperty exerciseIdProperty() {
        return exerciseId;
    }

    public int getReps() {
        return reps.get();
    }

    public void setReps(int value) {
        reps.set(value);
    }

    public IntegerProperty repsProperty() {
        return reps;
    }

    public double getWeight() {
        return weight.get();
    }

    public void setWeight(double value) {
        weight.set(value);
    }

    public DoubleProperty weightProperty() {
        return weight;
    }

    public int getSetNumber() {
        return setNumber.get();
    }

    public void setSetNumber(int value) {
        setNumber.set(value);
    }

    public IntegerProperty setNumberProperty() {
        return setNumber;
    }

    public boolean isCompleted() {
        return completed.get();
    }

    public void setCompleted(boolean value) {
        completed.set(value);
    }

    public BooleanProperty completedProperty() {
        return completed;
    }

    public String getNotes() {
        return notes.get();
    }

    public void setNotes(String value) {
        notes.set(value != null ? value : "");
    }

    public StringProperty notesProperty() {
        return notes;
    }
}
