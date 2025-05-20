package com.fitness.auth.controllers;

import com.fitness.auth.models.ExerciseCategory;
import javafx.fxml.FXML;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javafx.stage.Stage;

public class ProgressTrackingController extends BaseController {
    public ProgressTrackingController() {
        System.out.println("[DEBUG] ProgressTrackingController: Constructor called");
    }
    @FXML private Button backButton;
    @FXML private Button homeButton;


    @FXML
    private void handleBack() {
        System.out.println("[DEBUG] ProgressTrackingController: handleBack called. navigationController=" + navigationController + ", stage=" + stage);
        if (navigationController != null) {
            navigationController.navigateBack();
        } else if (stage != null) {
            stage.close();
        }
    } // Uses base class fields

    @FXML
    private void handleHome() {
        System.out.println("[DEBUG] ProgressTrackingController: handleHome called. navigationController=" + navigationController + ", stage=" + stage);
        if (navigationController != null) {
            navigationController.navigateToHome();
        } else if (stage != null) {
            stage.close();
        }
    } // Uses base class fields
    @FXML private TextField exerciseIdField;
    @FXML private ComboBox<SetOption> setIdComboBox;
    @FXML private Button searchButton;
    @FXML private DatePicker fromDatePicker;
    @FXML private DatePicker toDatePicker;
    @FXML private LineChart<String, Number> progressChart;
    @FXML private CategoryAxis xAxis;
    @FXML private NumberAxis yAxis;
    @FXML private TableView<WorkoutData> historyTable;
    @FXML private TableColumn<WorkoutData, LocalDate> dateColumn;
    @FXML private TableColumn<WorkoutData, Double> weightColumn;
    @FXML private TableColumn<WorkoutData, Integer> repsColumn;

    public static class SetOption {
        private final String setId;
        private final ExerciseCategory category;

        public SetOption(String setId, ExerciseCategory category) {
            this.setId = setId;
            this.category = category;
        }

        @Override
        public String toString() {
            return setId + " " + category;
        }

        public String getSetId() { return setId; }
        public ExerciseCategory getCategory() { return category; }
    }

    public static class WorkoutData {
        private final LocalDate date;
        private final double weight;
        private final int reps;

        public WorkoutData(LocalDate date, double weight, int reps) {
            this.date = date;
            this.weight = weight;
            this.reps = reps;
        }

        public LocalDate getDate() { return date; }
        public double getWeight() { return weight; }
        public int getReps() { return reps; }
    }


    @FXML
    public void initialize() {
        if (backButton != null) backButton.setOnAction(e -> handleBack());
        if (homeButton != null) homeButton.setOnAction(e -> handleHome());
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        weightColumn.setCellValueFactory(new PropertyValueFactory<>("weight"));
        repsColumn.setCellValueFactory(new PropertyValueFactory<>("reps"));

        String centerStyle = "-fx-alignment: CENTER;";
        dateColumn.setStyle(centerStyle);
        weightColumn.setStyle(centerStyle);
        repsColumn.setStyle(centerStyle);

        progressChart.setTitle("Exercise Progress");
        xAxis.setLabel("Date");
        yAxis.setLabel("Weight × Reps");

        setupListeners();
    }

    private void setupListeners() {
        searchButton.setOnAction(event -> handleSearch());

        exerciseIdField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.trim().isEmpty()) {
                updateSetIdComboBox(newValue.trim());
            }
        });

        setIdComboBox.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        updateChartAndTable();
                    }
                }
        );

        fromDatePicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                updateChartAndTable();
            }
        });

        toDatePicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                updateChartAndTable();
            }
        });
    }

    private void handleSearch() {
        String exerciseId = exerciseIdField.getText().trim();
        if (exerciseId.isEmpty()) {
            showAlert("Error", "Please enter an Exercise ID");
            return;
        }

        updateSetIdComboBox(exerciseId);
    }

    private void updateSetIdComboBox(String exerciseId) {
        setIdComboBox.getItems().clear();

        // Temporary mock data - replace with actual database query
        List<SetOption> options = new ArrayList<>();
        options.add(new SetOption(exerciseId + ".1", ExerciseCategory.WARMUP));
        options.add(new SetOption(exerciseId + ".2", ExerciseCategory.STRENGTH));
        options.add(new SetOption(exerciseId + ".3", ExerciseCategory.HYPERTROPHY));

        setIdComboBox.getItems().addAll(options);
    }

    private void updateChartAndTable() {
        SetOption selectedSet = setIdComboBox.getValue();
        if (selectedSet == null) return;

        updateChart(selectedSet);
        updateTable(selectedSet);
    }

    private void updateChart(SetOption selectedSet) {
        progressChart.getData().clear();

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName(selectedSet.getSetId());

        List<WorkoutData> data = getMockWorkoutData(selectedSet.getSetId());

        for (WorkoutData workout : data) {
            series.getData().add(new XYChart.Data<>(
                    workout.getDate().toString(),
                    workout.getWeight() * workout.getReps()
            ));
        }

        progressChart.getData().add(series);
    }

    private void updateTable(SetOption selectedSet) {
        historyTable.getItems().clear();
        List<WorkoutData> data = getMockWorkoutData(selectedSet.getSetId());
        historyTable.getItems().addAll(data);
    }

    private List<WorkoutData> getMockWorkoutData(String setId) {
        List<WorkoutData> data = new ArrayList<>();
        LocalDate today = LocalDate.now();

        data.add(new WorkoutData(today.minusDays(14), 60.0, 10));
        data.add(new WorkoutData(today.minusDays(7), 65.0, 10));
        data.add(new WorkoutData(today, 70.0, 10));

        return data;
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
