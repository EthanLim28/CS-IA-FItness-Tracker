package com.fitness.auth.controllers;

import com.fitness.auth.models.*;
import com.fitness.auth.util.DialogUtils;
import com.fitness.auth.util.FXMLUtils;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.collections.ObservableList;
import javafx.scene.layout.GridPane;
import javafx.geometry.Insets;
import java.sql.SQLException;
import java.util.List;

import java.util.function.Consumer;

public class ExerciseLibraryController {
    // ... existing fields and methods ...

    @FXML
    private void handleAddExerciseToSession() {
        Exercise selected = exerciseTable.getSelectionModel().getSelectedItem();
        if (selected != null && onExerciseSelected != null) {
            onExerciseSelected.accept(selected);
        }
    }
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
    @FXML private TableView<Exercise> exerciseTable;
    @FXML private TableColumn<Exercise, String> nameColumn;
    @FXML private TableColumn<Exercise, String> muscleGroupColumn;
    
    @FXML private Button addButton;
    @FXML private Button editButton;
    @FXML private Button deleteButton;
    @FXML private Button createExerciseButton; // Make sure this exists in FXML

    @FXML private ComboBox<String> muscleGroupFilter;
    @FXML private TextField searchField;
    @FXML private Label detailsName;
    @FXML private Label detailsMuscleGroup;
    @FXML private Label detailsEquipment;
@FXML private Label selectedExerciseLabel;

@FXML private TableView<WorkoutSet> workoutSetTable;
@FXML private TableColumn<WorkoutSet, Number> setColumn;
@FXML private TableColumn<WorkoutSet, Number> weightColumn;
@FXML private TableColumn<WorkoutSet, Number> repsColumn;
@FXML private TableColumn<WorkoutSet, String> recommendationColumn;
@FXML private Button addSetButton;

private final ObservableList<WorkoutSet> workoutSets = javafx.collections.FXCollections.observableArrayList();

private enum SetCategory { WARMUP, HYPERTROPHY, STRENGTH, DROPSET }

    private List<Exercise> allExercises = new java.util.ArrayList<>();

    private ExerciseDAO exerciseDAO;
    private Stage stage;
    private Consumer<Exercise> onExerciseSelected;
    private Runnable onCreateExercise;


    @FXML
    public void initialize() {
        try {
            exerciseDAO = new ExerciseDAO(DatabaseHelper.getConnection());
            setupTableColumns();
            loadExercises();
            setupButtonHandlers();
            setupFilterHandlers();
            setupDetailsSection();
            setupWorkoutSetTable();
            if (backButton != null) backButton.setOnAction(e -> handleBack());
            if (homeButton != null) homeButton.setOnAction(e -> handleHome());
        } catch (SQLException e) {
            DialogUtils.showError("Error", "Failed to initialize exercise library: " + e.getMessage());
        }
    }

    public void setDialogStage(Stage stage) {
        this.stage = stage;
    }
    public void setStage(Stage stage) { this.stage = stage; }

    public void setOnExerciseSelected(Consumer<Exercise> callback) {
        this.onExerciseSelected = callback;
    }
    public void setOnCreateExercise(Runnable callback) {
        this.onCreateExercise = callback;
    }
    public void refreshExerciseList() {
        loadExercises();
    }

    private void setupFilterHandlers() {
        if (muscleGroupFilter != null) {
            muscleGroupFilter.getItems().clear();
            muscleGroupFilter.getItems().add("All");
            // Populate unique muscle groups
            try {
                List<String> muscleGroups = exerciseDAO.getAllMuscleGroups();
                for (String group : muscleGroups) {
                    if (!muscleGroupFilter.getItems().contains(group)) {
                        muscleGroupFilter.getItems().add(group);
                    }
                }
            } catch (SQLException e) {
                DialogUtils.showError("Error", "Failed to load muscle groups: " + e.getMessage());
            }
            muscleGroupFilter.setValue("All");
            muscleGroupFilter.setOnAction(e -> applyFilters());
        }
        if (searchField != null) {
            searchField.textProperty().addListener((obs, oldVal, newVal) -> applyFilters());
        }
    }

    private void applyFilters() {
        String selectedGroup = (muscleGroupFilter != null) ? muscleGroupFilter.getValue() : null;
        String searchText = (searchField != null) ? searchField.getText().toLowerCase() : "";
        List<Exercise> filtered = new java.util.ArrayList<>();
        for (Exercise ex : allExercises) {
            boolean matchesGroup = selectedGroup == null || selectedGroup.equals("All") || ex.getMuscleGroup().equals(selectedGroup);
            boolean matchesSearch = searchText.isEmpty() || ex.getName().toLowerCase().contains(searchText);
            if (matchesGroup && matchesSearch) {
                filtered.add(ex);
            }
        }
        exerciseTable.getItems().setAll(filtered);
    }

    private void setupDetailsSection() {
        if (exerciseTable != null) {
            exerciseTable.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
                showExerciseDetails(newVal);
            });
        }
    }

    private void showExerciseDetails(Exercise exercise) {
    if (selectedExerciseLabel != null) {
        if (exercise != null) {
            selectedExerciseLabel.setText("Exercise: " + exercise.getName());
        } else {
            selectedExerciseLabel.setText("Exercise: ");
        }
    }
    // Clear the set table for new selection
    workoutSets.clear();
        if (exercise == null) {
            if (detailsName != null) detailsName.setText("");
            if (detailsMuscleGroup != null) detailsMuscleGroup.setText("");
            if (detailsEquipment != null) detailsEquipment.setText("");
        } else {
            if (detailsName != null) detailsName.setText(exercise.getName());
            if (detailsMuscleGroup != null) detailsMuscleGroup.setText(exercise.getMuscleGroup());
            if (detailsEquipment != null) detailsEquipment.setText(exercise.getEquipment());
        }
    }

    private void setupTableColumns() {
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        muscleGroupColumn.setCellValueFactory(cellData -> cellData.getValue().muscleGroupProperty());
        
    }

    private void loadExercises() {
        try {
            allExercises = exerciseDAO.getAllExercises();
            exerciseTable.getItems().setAll(allExercises);
            applyFilters(); // Apply filters after loading
        } catch (SQLException e) {
            DialogUtils.showError("Error", "Failed to load exercises: " + e.getMessage());
        }
    }

    private void setupButtonHandlers() {
        if (addSetButton != null) addSetButton.setOnAction(e -> handleAddSet());
        if (addButton != null) addButton.setOnAction(e -> {
    DialogUtils.showInformation("Success", "Exercise added successfully!");
    if (navigationController != null) {
        navigationController.navigateTo("workout_session");
    } else if (stage != null) {
        stage.close();
    }
});
        if (editButton != null) editButton.setOnAction(e -> handleEditExercise());
        if (deleteButton != null) deleteButton.setOnAction(e -> handleDeleteExercise());
        if (createExerciseButton != null) createExerciseButton.setOnAction(e -> handleCreateExercise());
        if (editButton != null) editButton.disableProperty().bind(exerciseTable.getSelectionModel().selectedItemProperty().isNull());
        if (deleteButton != null) deleteButton.disableProperty().bind(exerciseTable.getSelectionModel().selectedItemProperty().isNull());
        // Double-click to select exercise (optional)
        if (exerciseTable != null) {
            exerciseTable.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2) {
                    Exercise selected = exerciseTable.getSelectionModel().getSelectedItem();
                    if (selected != null && onExerciseSelected != null) {
                        onExerciseSelected.accept(selected);
                    }
                }
            });
        }
    }

    private void handleEditExercise() {
        Exercise selectedExercise = exerciseTable.getSelectionModel().getSelectedItem();
        if (selectedExercise != null) {
            try {
                Stage dialogStage = FXMLUtils.createDialogStage(stage, "Edit Exercise", "fxml/create_exercise_dialog.fxml");
                com.fitness.auth.controllers.CreateExerciseDialogController controller = FXMLUtils.getController(dialogStage);
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

    private void handleCreateExercise() {
        try {
            Stage dialogStage = FXMLUtils.createDialogStage(stage, "Create Exercise", "fxml/create_exercise_dialog.fxml");
            com.fitness.auth.controllers.CreateExerciseDialogController controller = FXMLUtils.getController(dialogStage);
            controller.setDialogStage(dialogStage);
            controller.setExerciseDAO(exerciseDAO);
            controller.setOnExerciseCreated(this::loadExercises);
            dialogStage.showAndWait();
        } catch (Exception e) {
            DialogUtils.showError("Error", "Could not open create exercise dialog: " + e.getMessage());
        }
    }

    private void setupWorkoutSetTable() {
        if (workoutSetTable != null) {
            setColumn.setCellValueFactory(cellData -> cellData.getValue().setNumberProperty());
            weightColumn.setCellValueFactory(cellData -> cellData.getValue().weightProperty());
            repsColumn.setCellValueFactory(cellData -> cellData.getValue().repsProperty());
            recommendationColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(computeRecommendation(cellData.getValue())));
            workoutSetTable.setItems(workoutSets);
            recommendationColumn.setEditable(false);
        }
    }

    private void handleAddSet() {
        // Show dialog to enter weight, reps, set category
        Dialog<SetInput> dialog = new Dialog<>();
        dialog.setTitle("Add Set");
        dialog.setHeaderText(null);
        ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField weightField = new TextField();
        weightField.setPromptText("Weight (kg)");
        TextField repsField = new TextField();
        repsField.setPromptText("Reps");
        ComboBox<String> categoryBox = new ComboBox<>();
        categoryBox.getItems().addAll("Warmup", "Hypertrophy", "Strength", "Dropset");
        categoryBox.setPromptText("Set Category");

        grid.add(new Label("Weight (kg):"), 0, 0);
        grid.add(weightField, 1, 0);
        grid.add(new Label("Reps:"), 0, 1);
        grid.add(repsField, 1, 1);
        grid.add(new Label("Set Category:"), 0, 2);
        grid.add(categoryBox, 1, 2);

        dialog.getDialogPane().setContent(grid);
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                try {
                    double weight = Double.parseDouble(weightField.getText());
                    int reps = Integer.parseInt(repsField.getText());
                    String category = categoryBox.getValue();
                    if (category == null) return null;
                    return new SetInput(weight, reps, category);
                } catch (Exception ex) {
                    DialogUtils.showError("Input Error", "Please enter valid numbers for weight and reps.");
                    return null;
                }
            }
            return null;
        });

        dialog.showAndWait().ifPresent(input -> {
            int setNumber = workoutSets.size() + 1;
            WorkoutSet set = new WorkoutSet(null, setNumber, input.reps, input.weight, null);
            // Store category in notes for now (hidden)
            set.setNotes(input.category);
            workoutSets.add(set);
            workoutSetTable.refresh();
        });
    }

    private String computeRecommendation(WorkoutSet set) {
        String category = set.getNotes() != null ? set.getNotes().toLowerCase() : "";
        int reps = set.getReps();
        double weight = set.getWeight();
        switch (category) {
            case "warmup":
                return String.format("%.0fkg, %d reps", weight, reps);
            case "hypertrophy":
                return String.format("%.0fkg, %d reps", weight, reps + 1);
            case "strength":
                return String.format("%.1fkg, %d reps", weight + 2.5, reps);
            case "dropset":
                return String.format("%.0fkg, %d reps", weight, reps + 1);
            default:
                return String.format("%.0fkg, %d reps", weight, reps);
        }
    }

    private static class SetInput {
        double weight;
        int reps;
        String category;
        SetInput(double weight, int reps, String category) {
            this.weight = weight;
            this.reps = reps;
            this.category = category;
        }
    }
}
