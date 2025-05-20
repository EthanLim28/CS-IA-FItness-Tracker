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
    private NavigationController navigationController;
    @FXML private Button backButton;
    @FXML private Button homeButton;

    public void setNavigationController(NavigationController navigationController) {
        this.navigationController = navigationController;
    }

    @FXML
    private void handleBack() {
        if (navigationController != null) {
            navigationController.navigateBack();
        } else if (stage != null) {
            stage.close();
        }
    }

    @FXML
    private void handleHome() {
        if (navigationController != null) {
            navigationController.navigateToHome();
        } else if (stage != null) {
            stage.close();
        }
    }
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
            if (backButton != null) backButton.setOnAction(e -> handleBack());
            if (homeButton != null) homeButton.setOnAction(e -> handleHome());
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
            // Open the Exercise Library dialog first
            Stage libraryStage = FXMLUtils.createDialogStage(stage, "Exercise Library", "exercise_library.fxml");
            ExerciseLibraryController libraryController = FXMLUtils.getController(libraryStage);
            libraryController.setDialogStage(libraryStage);
            libraryController.setOnExerciseSelected(selectedExercise -> {
                // Add selected exercise to workout
                if (selectedExercise != null) {
                    // Create a new WorkoutSet for the selected exercise with default values
                    WorkoutSet newSet = new WorkoutSet(selectedExercise, workout.getWorkoutSets().size() + 1, 0, 0.0, "");
                    workout.addSet(newSet);
                    refreshExerciseList();
                    libraryStage.close();
                }
            });
            libraryController.setOnCreateExercise(() -> {
                // Open the Create Exercise dialog
                try {
                    Stage createStage = FXMLUtils.createDialogStage(libraryStage, "Create Exercise", "add_exercise_dialog.fxml");
                    AddExerciseDialogController addController = FXMLUtils.getController(createStage);
                    addController.setDialogStage(createStage);
                    addController.setOnExerciseAdded(() -> {
                        // After creating, refresh the library and keep it open
                        libraryController.refreshExerciseList();
                        createStage.close();
                    });
                    createStage.showAndWait();
                } catch (Exception ce) {
                    DialogUtils.showError("Error", "Could not open create exercise dialog: " + ce.getMessage());
                }
            });
            libraryStage.showAndWait();
        } catch (Exception e) {
            DialogUtils.showError("Error", "Could not open exercise library dialog: " + e.getMessage());
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
