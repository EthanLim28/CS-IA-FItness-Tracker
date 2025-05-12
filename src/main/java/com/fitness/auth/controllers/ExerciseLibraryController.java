package com.fitness.auth.controllers;

import com.fitness.auth.models.*;
import com.fitness.auth.util.DialogUtils;
import com.fitness.auth.util.FXMLUtils;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.sql.SQLException;
import java.util.List;

public class ExerciseLibraryController {
    @FXML private TableView<Exercise> exerciseTable;
    @FXML private TableColumn<Exercise, String> nameColumn;
    @FXML private TableColumn<Exercise, String> muscleGroupColumn;
    @FXML private TableColumn<Exercise, ExerciseCategory> categoryColumn;
    @FXML private Button addButton;
    @FXML private Button editButton;
    @FXML private Button deleteButton;

    private ExerciseDAO exerciseDAO;
    private Stage stage;

    @FXML
    public void initialize() {
        try {
            exerciseDAO = new ExerciseDAO(DatabaseHelper.getConnection());
            setupTableColumns();
            loadExercises();
            setupButtonHandlers();
        } catch (SQLException e) {
            DialogUtils.showError("Error", "Failed to initialize exercise library: " + e.getMessage());
        }
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    private void setupTableColumns() {
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        muscleGroupColumn.setCellValueFactory(cellData -> cellData.getValue().muscleGroupProperty());
        categoryColumn.setCellValueFactory(cellData -> cellData.getValue().categoryProperty());
    }

    private void loadExercises() {
        try {
            List<Exercise> exercises = exerciseDAO.getAllExercises();
            exerciseTable.getItems().setAll(exercises);
        } catch (SQLException e) {
            DialogUtils.showError("Error", "Failed to load exercises: " + e.getMessage());
        }
    }

    private void setupButtonHandlers() {
        addButton.setOnAction(event -> handleAddExercise());
        editButton.setOnAction(event -> handleEditExercise());
        deleteButton.setOnAction(event -> handleDeleteExercise());
        
        editButton.disableProperty().bind(exerciseTable.getSelectionModel().selectedItemProperty().isNull());
        deleteButton.disableProperty().bind(exerciseTable.getSelectionModel().selectedItemProperty().isNull());
    }

    private void handleAddExercise() {
        try {
            Stage dialogStage = FXMLUtils.createDialogStage(stage, "Add Exercise", "AddExerciseDialog.fxml");
            AddExerciseDialogController controller = FXMLUtils.getController(dialogStage);
            controller.setDialogStage(dialogStage);
            dialogStage.showAndWait();
            loadExercises();
        } catch (Exception e) {
            DialogUtils.showError("Error", "Could not open add exercise dialog: " + e.getMessage());
        }
    }

    private void handleEditExercise() {
        Exercise selectedExercise = exerciseTable.getSelectionModel().getSelectedItem();
        if (selectedExercise != null) {
            try {
                Stage dialogStage = FXMLUtils.createDialogStage(stage, "Edit Exercise", "AddExerciseDialog.fxml");
                AddExerciseDialogController controller = FXMLUtils.getController(dialogStage);
                controller.setDialogStage(dialogStage);
                dialogStage.showAndWait();
                loadExercises();
            } catch (Exception e) {
                DialogUtils.showError("Error", "Could not open edit exercise dialog: " + e.getMessage());
            }
        }
    }

    private void handleDeleteExercise() {
        Exercise selectedExercise = exerciseTable.getSelectionModel().getSelectedItem();
        if (selectedExercise != null) {
            try {
                exerciseDAO.deleteExercise(selectedExercise.getExerciseId());
                loadExercises();
                DialogUtils.showInformation("Success", "Exercise deleted successfully");
            } catch (SQLException e) {
                DialogUtils.showError("Error", "Failed to delete exercise: " + e.getMessage());
            }
        }
    }
}
