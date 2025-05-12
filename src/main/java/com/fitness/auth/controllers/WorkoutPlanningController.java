package com.fitness.auth.controllers;

import com.fitness.auth.models.*;
import com.fitness.auth.util.DialogUtils;
import com.fitness.auth.util.FXMLUtils;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Optional;

public class WorkoutPlanningController {
    @FXML private DatePicker datePicker;
    @FXML private TextField workoutTypeField;
    @FXML private ListView<WorkoutSet> exerciseList;
    @FXML private Button addExerciseButton;
    @FXML private Button saveButton;
    @FXML private Button cancelButton;

    private Stage stage;
    private DailyWorkout workout;
    private DailyWorkoutDAO workoutDAO;

    @FXML
    public void initialize() {
        try {
            workoutDAO = new DailyWorkoutDAO(DatabaseHelper.getConnection());
            datePicker.setValue(LocalDate.now());
            setupButtonHandlers();
        } catch (SQLException e) {
            DialogUtils.showError("Error", "Failed to initialize workout planning: " + e.getMessage());
        }
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setUser(User user) {
        this.workout = new DailyWorkout(datePicker.getValue());
        this.workout.setUserId(user.getUserId());
    }

    private void setupButtonHandlers() {
        addExerciseButton.setOnAction(event -> handleAddExercise());
        saveButton.setOnAction(event -> handleSave());
        cancelButton.setOnAction(event -> handleCancel());
    }

    private void handleAddExercise() {
        try {
            Stage dialogStage = FXMLUtils.createDialogStage(stage, "Add Exercise", "AddExerciseDialog.fxml");
            AddExerciseDialogController controller = FXMLUtils.getController(dialogStage);
            controller.setDialogStage(dialogStage);
            controller.setWorkout(workout);
            controller.setOnExerciseAdded(this::refreshExerciseList);
            dialogStage.showAndWait();
        } catch (Exception e) {
            DialogUtils.showError("Error", "Could not open add exercise dialog: " + e.getMessage());
        }
    }

    private void handleSave() {
        if (!validateWorkout()) {
            return;
        }

        try {
            workout.setWorkoutType(workoutTypeField.getText().trim());
            workout.setDate(datePicker.getValue());
            workoutDAO.saveWorkout(workout);
            DialogUtils.showInformation("Success", "Workout plan saved successfully");
            stage.close();
        } catch (SQLException e) {
            DialogUtils.showError("Error", "Failed to save workout plan: " + e.getMessage());
        }
    }

    private void handleCancel() {
        stage.close();
    }

    private boolean validateWorkout() {
        if (workoutTypeField.getText().trim().isEmpty()) {
            DialogUtils.showError("Validation Error", "Please enter a workout type");
            return false;
        }

        if (datePicker.getValue() == null) {
            DialogUtils.showError("Validation Error", "Please select a date");
            return false;
        }

        if (workout.getWorkoutSets().isEmpty()) {
            DialogUtils.showError("Validation Error", "Please add at least one exercise");
            return false;
        }

        return true;
    }

    private void refreshExerciseList() {
        exerciseList.getItems().setAll(workout.getWorkoutSets());
    }

    public Optional<WorkoutSet> getSelectedSet() {
        return Optional.ofNullable(exerciseList.getSelectionModel().getSelectedItem());
    }
}
