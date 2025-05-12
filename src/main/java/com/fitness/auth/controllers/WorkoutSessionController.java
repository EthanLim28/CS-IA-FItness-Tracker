package com.fitness.auth.controllers;

import com.fitness.auth.models.*;
import com.fitness.auth.util.DialogUtils;
import com.fitness.auth.util.FXMLUtils;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class WorkoutSessionController {
    @FXML private VBox workoutContainer;
    @FXML private TextField workoutTypeField;
    @FXML private DatePicker workoutDatePicker;
    @FXML private TextArea notesArea;
    @FXML private Button addExerciseButton;
    @FXML private Button saveWorkoutButton;
    @FXML private Button cancelButton;

    private DailyWorkout currentWorkout;
    private DailyWorkoutDAO workoutDAO;
    private Stage stage;
    private NavigationController navigationController;

    @FXML
    public void initialize() {
        try {
            workoutDAO = new DailyWorkoutDAO(DatabaseHelper.getConnection());
            workoutDatePicker.setValue(LocalDate.now());
            
            addExerciseButton.setOnAction(event -> handleAddExercise());
            saveWorkoutButton.setOnAction(event -> handleSaveWorkout());
            cancelButton.setOnAction(event -> handleCancel());
        } catch (SQLException e) {
            DialogUtils.showError("Error", "Failed to initialize workout session: " + e.getMessage());
        }
    }

    public void initData(User user) {
        this.currentWorkout = new DailyWorkout(
            0,
            user.getUserId(),
            workoutDatePicker.getValue(),
            workoutTypeField.getText(),
            new ArrayList<>(),
            notesArea.getText()
        );
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setNavigationController(NavigationController navigationController) {
        this.navigationController = navigationController;
    }

    private void handleAddExercise() {
        try {
            FXMLUtils.showDialog(
                "AddExerciseDialog.fxml",
                "Add Exercise",
                stage,
                (AddExerciseDialogController controller) -> {
                    controller.setWorkout(currentWorkout);
                    controller.setOnExerciseAdded(this::refreshWorkoutDisplay);
                }
            );
        } catch (Exception e) {
            DialogUtils.showError("Error", "Could not open add exercise dialog: " + e.getMessage());
        }
    }

    private void handleSaveWorkout() {
        if (!validateWorkout()) {
            return;
        }

        try {
            updateWorkoutFromInputs();
            workoutDAO.saveWorkout(currentWorkout);
            DialogUtils.showInformation("Success", "Workout saved successfully!");
            if (navigationController != null) {
                navigationController.navigateBack();
            } else {
                stage.close();
            }
        } catch (SQLException e) {
            DialogUtils.showError("Error", "Failed to save workout: " + e.getMessage());
        }
    }

    private void handleCancel() {
        if (navigationController != null) {
            navigationController.navigateBack();
        } else {
            stage.close();
        }
    }

    private boolean validateWorkout() {
        if (workoutTypeField.getText().trim().isEmpty()) {
            DialogUtils.showError("Validation Error", "Please enter a workout type");
            return false;
        }

        if (workoutDatePicker.getValue() == null) {
            DialogUtils.showError("Validation Error", "Please select a workout date");
            return false;
        }

        if (currentWorkout.getWorkoutSets().isEmpty()) {
            DialogUtils.showError("Validation Error", "Please add at least one exercise");
            return false;
        }

        return true;
    }

    private void updateWorkoutFromInputs() {
        currentWorkout.setWorkoutType(workoutTypeField.getText().trim());
        currentWorkout.setDate(workoutDatePicker.getValue());
        currentWorkout.setNotes(notesArea.getText().trim());
    }

    private void refreshWorkoutDisplay() {
        workoutContainer.getChildren().clear();
        List<WorkoutSet> sets = currentWorkout.getWorkoutSets();
        
        for (int i = 0; i < sets.size(); i++) {
            WorkoutSet set = sets.get(i);
            VBox setBox = createSetDisplay(set, i);
            workoutContainer.getChildren().add(setBox);
        }
    }

    private VBox createSetDisplay(WorkoutSet set, int index) {
        VBox setBox = new VBox(5);
        setBox.getStyleClass().add("workout-set");

        Label exerciseLabel = new Label(String.format("Exercise %d: %s", 
            index + 1, set.getExercise().getName()));
        Label detailsLabel = new Label(String.format("Weight: %.1f kg, Reps: %d", 
            set.getWeight(), set.getReps()));
        
        Button removeButton = new Button("Remove");
        removeButton.setOnAction(e -> {
            currentWorkout.removeSet(set);
            refreshWorkoutDisplay();
        });

        setBox.getChildren().addAll(exerciseLabel, detailsLabel, removeButton);
        return setBox;
    }
}