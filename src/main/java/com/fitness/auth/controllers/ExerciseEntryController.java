package com.fitness.auth.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import com.fitness.auth.models.Exercise;
import com.fitness.auth.models.WorkoutSet;
import com.fitness.auth.util.DialogUtils;

public class ExerciseEntryController {
    @FXML private TableView<WorkoutSet> setsTable;
    @FXML private TableColumn<WorkoutSet, Integer> repsColumn;
    @FXML private TableColumn<WorkoutSet, Double> weightColumn;
    @FXML private TableColumn<WorkoutSet, String> notesColumn;
    @FXML private TextField repsField;
    @FXML private TextField weightField;
    @FXML private TextField notesField;
    @FXML private Button addButton;
    @FXML private Button removeButton;

    private Exercise exercise;
    private ObservableList<WorkoutSet> workoutSets;

    @FXML
    private void initialize() {
        workoutSets = FXCollections.observableArrayList();
        setsTable.setItems(workoutSets);

        repsColumn.setCellValueFactory(new PropertyValueFactory<>("reps"));
        weightColumn.setCellValueFactory(new PropertyValueFactory<>("weight"));
        notesColumn.setCellValueFactory(new PropertyValueFactory<>("notes"));

        setsTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            removeButton.setDisable(newSelection == null);
        });
    }

    public void setExercise(Exercise exercise) {
        this.exercise = exercise;
    }

    @FXML
    private void handleAddSet() {
        try {
            int reps = Integer.parseInt(repsField.getText());
            double weight = Double.parseDouble(weightField.getText());
            String notes = notesField.getText();

            // Create a new set with the next set number
            int nextSetNumber = workoutSets.size() + 1;
            WorkoutSet set = new WorkoutSet(exercise, nextSetNumber, reps, weight, notes);
            workoutSets.add(set);

            // Clear input fields
            repsField.clear();
            weightField.clear();
            notesField.clear();
            repsField.requestFocus();
        } catch (NumberFormatException e) {
            DialogUtils.showError("Input Error", "Please enter valid numbers for reps and weight");
        }
    }

    @FXML
    private void handleRemoveSet() {
        WorkoutSet selectedSet = setsTable.getSelectionModel().getSelectedItem();
        if (selectedSet != null) {
            workoutSets.remove(selectedSet);
            // Reorder remaining sets
            for (int i = 0; i < workoutSets.size(); i++) {
                workoutSets.get(i).setSetNumber(i + 1);
            }
        }
    }

    public ObservableList<WorkoutSet> getWorkoutSets() {
        return workoutSets;
    }
}
