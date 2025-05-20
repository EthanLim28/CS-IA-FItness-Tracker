package com.fitness.auth.models;

import javafx.beans.property.*;

public class Exercise {
    private final IntegerProperty exerciseId;
    private final StringProperty name;
    private final StringProperty description;
    private final StringProperty muscleGroup;
    
    private final StringProperty equipment;

    public Exercise() {
        this(0, "", "", "", "");
    }

    public Exercise(String name, String description, String muscleGroup, String equipment) {
        this(0, name, description, muscleGroup, equipment);
    }

    public Exercise(int exerciseId, String name, String description, String muscleGroup, String equipment) {
        this.exerciseId = new SimpleIntegerProperty(exerciseId);
        this.name = new SimpleStringProperty(name);
        this.description = new SimpleStringProperty(description);
        this.muscleGroup = new SimpleStringProperty(muscleGroup);
        
        this.equipment = new SimpleStringProperty(equipment);
    }

    public int getExerciseId() {
        return exerciseId.get();
    }

    public IntegerProperty exerciseIdProperty() {
        return exerciseId;
    }

    public void setExerciseId(int exerciseId) {
        this.exerciseId.set(exerciseId);
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getDescription() {
        return description.get();
    }

    public StringProperty descriptionProperty() {
        return description;
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public String getMuscleGroup() {
        return muscleGroup.get();
    }

    public StringProperty muscleGroupProperty() {
        return muscleGroup;
    }

    public void setMuscleGroup(String muscleGroup) {
        this.muscleGroup.set(muscleGroup);
    }

    public String getEquipment() {
        return equipment.get();
    }

    public StringProperty equipmentProperty() {
        return equipment;
    }

    public void setEquipment(String equipment) {
        this.equipment.set(equipment);
    }

    @Override
    public String toString() {
        return name.get();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Exercise exercise = (Exercise) o;
        return getExerciseId() == exercise.getExerciseId();
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(getExerciseId());
    }
}
