package com.fitness.auth.controllers;

import com.fitness.auth.models.*;
import com.fitness.auth.util.DialogUtils;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.sql.SQLException;

public class AddExerciseDialogController {
    @FXML private ComboBox<Exercise> exerciseComboBox;
    @FXML private TextField weightField;
    @FXML private TextField repsField;
    @FXML private TextArea notesArea;
    @FXML private Button addButton;
    @FXML private Button cancelButton;

    private DailyWorkout workout;
    private Stage dialogStage;
    private Runnable onExerciseAdded;

    @FXML
    public void initialize() {
        System.out.println("[DEBUG] AddExerciseDialogController.initialize() called: " + this.hashCode());
        addButton.setOnAction(event -> handleAddExercise());
        cancelButton.setOnAction(event -> handleCancel());
        loadExercises();
    }

    public void setWorkout(DailyWorkout workout) {
        this.workout = workout;
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setOnExerciseAdded(Runnable callback) {
        System.out.println("[DEBUG] setOnExerciseAdded called on instance: " + this.hashCode());
        this.onExerciseAdded = callback;
    }

    private void loadExercises() {
        try {
            ExerciseDAO exerciseDAO = new ExerciseDAO(DatabaseHelper.getConnection());
            exerciseComboBox.getItems().setAll(exerciseDAO.getAllExercises());
        } catch (SQLException e) {
            DialogUtils.showError("Error", "Failed to load exercises: " + e.getMessage());
        }
    }

    private void handleAddExercise() {
        System.out.println("[DEBUG] handleAddExercise called on instance: " + this.hashCode());
        if (!validateInput()) {
            return;
        }

        Exercise selectedExercise = exerciseComboBox.getValue();
        double weight = Double.parseDouble(weightField.getText());
        int reps = Integer.parseInt(repsField.getText());
        String notes = notesArea.getText().trim();

        WorkoutSet set = new WorkoutSet(selectedExercise, workout.getWorkoutSets().size() + 1, reps, weight, notes);
        workout.addSet(set);

        if (onExerciseAdded != null) {
            onExerciseAdded.run();
        }

        DialogUtils.showInformation("Success", "Exercise added successfully!");
        dialogStage.close();
        cleanup();
    }

    private void handleCancel() {
        dialogStage.close();
        cleanup();
    }

    private boolean validateInput() {

        if (exerciseComboBox.getValue() == null) {
            DialogUtils.showError("Validation Error", "Please select an exercise");
            return false;
        }

        try {
            double weight = Double.parseDouble(weightField.getText());
            if (weight <= 0) {
                DialogUtils.showError("Validation Error", "Weight must be greater than 0");
                return false;
            }
        } catch (NumberFormatException e) {
            DialogUtils.showError("Validation Error", "Please enter a valid weight");
            return false;
        }

        try {
            int reps = Integer.parseInt(repsField.getText());
            if (reps <= 0) {
                DialogUtils.showError("Validation Error", "Reps must be greater than 0");
                return false;
            }
        } catch (NumberFormatException e) {
            DialogUtils.showError("Validation Error", "Please enter a valid number of reps");
            return false;
        }

        return true;
    }

    /**
     * Cleanup/reset fields and listeners to avoid state leakage or duplicate handlers.
     */
    private void cleanup() {
        System.out.println("[DEBUG] cleanup called on instance: " + this.hashCode());
        if (exerciseComboBox != null) exerciseComboBox.getSelectionModel().clearSelection();
        if (weightField != null) weightField.clear();
        if (repsField != null) repsField.clear();
        if (notesArea != null) notesArea.clear();
        // Remove all listeners (if any were added dynamically)
        if (addButton != null) addButton.setOnAction(null);
        if (cancelButton != null) cancelButton.setOnAction(null);
        onExerciseAdded = null;
        workout = null;
        dialogStage = null;
    }
}
