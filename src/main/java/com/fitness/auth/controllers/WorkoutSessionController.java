package com.fitness.auth.controllers;

import com.fitness.auth.models.*;
import com.fitness.auth.util.DialogUtils;
import com.fitness.auth.util.FXMLUtils;
import com.fitness.auth.dialogs.SetTemplateDialog;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.converter.NumberStringConverter;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;

public class WorkoutSessionController {
    @FXML private VBox workoutContainer;
    @FXML private TextField workoutTypeField;
    @FXML private DatePicker workoutDatePicker;
    @FXML private TextArea notesArea;
    @FXML private Button addExerciseButton;
    @FXML private Button saveWorkoutButton;
    @FXML private Button cancelButton;
    @FXML private Button backButton;
    @FXML private Button homeButton;

    private DailyWorkout currentWorkout;
    private DailyWorkoutDAO workoutDAO;
    private Stage stage;
    private NavigationController navigationController;

    @FXML
    public void initialize() {
        try {
            workoutDAO = new DailyWorkoutDAO(DatabaseHelper.getConnection());
            workoutDatePicker.setValue(LocalDate.now());

            // Initialize addedExercises from currentWorkout's sets if not empty
            if (currentWorkout != null && currentWorkout.getWorkoutSets() != null) {
                for (WorkoutSet set : currentWorkout.getWorkoutSets()) {
                    if (set.getExercise() != null && !addedExercises.contains(set.getExercise())) {
                        addedExercises.add(set.getExercise());
                    }
                }
            }
            System.out.println("[DEBUG] initialize: addedExercises=" + addedExercises);
            refreshWorkoutDisplay();

            addExerciseButton.setOnAction(event -> handleAddExercise());
            saveWorkoutButton.setOnAction(event -> handleSaveWorkout());
            cancelButton.setOnAction(event -> handleCancel());

            if (backButton != null) {
                backButton.setOnAction(e -> handleBack());
            }
            if (homeButton != null) {
                homeButton.setOnAction(e -> handleHome());
            }
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

    // Maintain a list of exercises added to this session
    private final List<Exercise> addedExercises = new ArrayList<>();

    private void handleAddExercise() {
    System.out.println("[DEBUG] handleAddExercise called");
        try {
            // Open the Exercise Library dialog first
            Stage libraryStage = FXMLUtils.createDialogStage(stage, "Exercise Library", "exercise_library.fxml");
            ExerciseLibraryController libraryController = FXMLUtils.getController(libraryStage);
            libraryController.setDialogStage(libraryStage);
            libraryController.setOnExerciseSelected(selectedExercise -> {
                boolean alreadyAdded = false;
                for (Exercise ex : addedExercises) {
                    if (ex.getExerciseId() == selectedExercise.getExerciseId() && ex.getName().equals(selectedExercise.getName())) {
                        alreadyAdded = true;
                        break;
                    }
                }
                if (selectedExercise != null && !alreadyAdded) {
                System.out.println("[DEBUG] Adding exercise: " + selectedExercise.getName() + " (ID: " + selectedExercise.getExerciseId() + ")");
                    System.out.println("[DEBUG] Adding exercise: " + selectedExercise.getName());
                    addedExercises.add(selectedExercise);
                    refreshWorkoutDisplay();
                    System.out.println("[DEBUG] Called refreshWorkoutDisplay after adding exercise");
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
            e.printStackTrace();
            DialogUtils.showError("Error", "Could not open exercise library dialog: " + e.getMessage());
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
            }
            // else do nothing to avoid closing the app
        
        } catch (SQLException e) {
            DialogUtils.showError("Error", "Failed to save workout: " + e.getMessage());
        }
    }

    private void handleCancel() {
        if (navigationController != null) {
            navigationController.navigateBack();
        }
        // else do nothing to avoid closing the app
    
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
        System.out.println("[DEBUG] refreshWorkoutDisplay called. addedExercises.size() = " + addedExercises.size());
        for (Exercise ex : addedExercises) {
            System.out.println("[DEBUG] addedExercises contains: " + ex.getName() + " (ID: " + ex.getExerciseId() + ")");
        }
        workoutContainer.getChildren().clear();
        // Group sets by exercise
        List<WorkoutSet> sets = currentWorkout.getWorkoutSets();
        Map<Exercise, List<WorkoutSet>> exerciseSets = new LinkedHashMap<>();
        for (Exercise ex : addedExercises) {
            exerciseSets.put(ex, new ArrayList<>());
        }
        for (WorkoutSet set : sets) {
            if (set.getExercise() != null) {
                exerciseSets.computeIfAbsent(set.getExercise(), k -> new ArrayList<>()).add(set);
            }
        }
        for (Exercise exercise : addedExercises) {
            System.out.println("[DEBUG] Displaying exercise: " + exercise.getName() + " (ID: " + exercise.getExerciseId() + ")");
            List<WorkoutSet> exerciseSetList = exerciseSets.getOrDefault(exercise, new ArrayList<>());
            VBox exerciseBox = new VBox(6);
            Label exerciseLabel = new Label("Exercise: " + exercise.getName());
            TableView<WorkoutSet> setTable = new TableView<>();
            setTable.setEditable(true);
            setTable.setItems(FXCollections.observableArrayList(exerciseSetList));

            TableColumn<WorkoutSet, Number> setCol = new TableColumn<>("Set");
            setCol.setCellValueFactory(data -> data.getValue().setNumberProperty());

            TableColumn<WorkoutSet, Number> weightCol = new TableColumn<>("Weight (kg)");
            weightCol.setCellValueFactory(data -> data.getValue().weightProperty());
            weightCol.setCellFactory(TextFieldTableCell.forTableColumn(new javafx.util.converter.NumberStringConverter()));
            weightCol.setOnEditCommit(event -> {
                event.getRowValue().setWeight(event.getNewValue().doubleValue());
            });

            TableColumn<WorkoutSet, Number> repsCol = new TableColumn<>("Reps");
            repsCol.setCellValueFactory(data -> data.getValue().repsProperty());
            repsCol.setCellFactory(TextFieldTableCell.forTableColumn(new javafx.util.converter.NumberStringConverter()));
            repsCol.setOnEditCommit(event -> {
                event.getRowValue().setReps(event.getNewValue().intValue());
            });

            TableColumn<WorkoutSet, String> recCol = new TableColumn<>("Recommendation");
            recCol.setCellValueFactory(data -> {
                SetTemplate t = data.getValue().getSetTemplate();
                String rec = (t != null) ? (t.getWeightIncrement() + "kg, " + t.getRepRange() + " reps [" + t.getCategory() + "]") : "-";
                return new SimpleStringProperty(rec);
            });

            TableColumn<WorkoutSet, Void> removeCol = new TableColumn<>("Remove");
            removeCol.setCellFactory(col -> {
                TableCell<WorkoutSet, Void> cell = new TableCell<>() {
                    private final Button btn = new Button("Remove");
                    {
                        btn.setOnAction(e -> {
                            WorkoutSet set = getTableView().getItems().get(getIndex());
                            currentWorkout.removeSet(set);
                            refreshWorkoutDisplay();
                        });
                    }
                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        setGraphic(empty ? null : btn);
                    }
                };
                return cell;
            });

            setTable.getColumns().addAll(setCol, weightCol, repsCol, recCol, removeCol);
            setTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

            Button addSetButton = new Button("Add Set");
            addSetButton.setOnAction(e -> {
                SetTemplateDialog dialog = new SetTemplateDialog();
                dialog.showAndWait().ifPresent(template -> {
                    int nextSetNum = exerciseSetList.size() + 1;
                    WorkoutSet newSet = new WorkoutSet(exercise, nextSetNum, 0, 0.0, "", template);
                    currentWorkout.addSet(newSet);
                    if (!addedExercises.contains(exercise)) {
                        addedExercises.add(exercise);
                        System.out.println("[DEBUG] Added exercise to addedExercises from Add Set: " + exercise.getName());
                    }
                    System.out.println("[DEBUG] Added set for " + exercise.getName() + ", total sets now: " + (exerciseSetList.size() + 1));
                    refreshWorkoutDisplay();
                });
            });

            exerciseBox.getChildren().addAll(exerciseLabel, setTable, addSetButton);
            workoutContainer.getChildren().add(exerciseBox);
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

    @FXML
    private void handleBack() {
        if (navigationController != null) {
            navigationController.navigateBack();
        }
        // else do nothing to avoid closing the app
    
    }

    @FXML
    private void handleHome() {
        if (navigationController != null) {
            navigationController.navigateToHome();
        }
        // else do nothing to avoid closing the app
    
    }
}