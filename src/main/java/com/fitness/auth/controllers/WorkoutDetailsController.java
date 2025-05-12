package com.fitness.auth.controllers;

import com.fitness.auth.models.Workout;
import com.fitness.auth.models.WorkoutSet;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import java.time.format.DateTimeFormatter;

public class WorkoutDetailsController extends BaseController {
    @FXML private Label dateLabel;
    @FXML private Label typeLabel;
    @FXML private TextArea notesArea;
    @FXML private TableView<WorkoutSet> setsTable;
    @FXML private TableColumn<WorkoutSet, String> exerciseColumn;
    @FXML private TableColumn<WorkoutSet, Number> weightColumn;
    @FXML private TableColumn<WorkoutSet, Number> repsColumn;

    private Workout workout;

    @FXML
    public void initialize() {
        exerciseColumn.setCellValueFactory(cellData -> 
            cellData.getValue().getExercise().nameProperty());
        weightColumn.setCellValueFactory(cellData -> 
            cellData.getValue().weightProperty());
        repsColumn.setCellValueFactory(cellData -> 
            cellData.getValue().repsProperty());
    }

    public void setWorkout(Workout workout) {
        this.workout = workout;
        updateUI();
    }

    private void updateUI() {
        if (workout != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM d, yyyy");
            dateLabel.setText(workout.getDate().format(formatter));
            typeLabel.setText(workout.getWorkoutType());
            notesArea.setText(workout.getNotes());
            setsTable.setItems(workout.getSets());
        }
    }

    @FXML
    private void handleBack() {
        navigationController.navigateBack();
    }
}
