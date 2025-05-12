package com.fitness.auth.controllers;

import com.fitness.auth.models.*;
import com.fitness.auth.util.DialogUtils;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.Dialog;
import javafx.scene.control.ButtonType; // Removed duplicate and unused imports

public class DashboardController extends BaseController {
    @FXML private Label welcomeLabel;
    @FXML private Label streakLabel;
    @FXML private VBox recentWorkoutsContainer;
    @FXML private LineChart<String, Number> progressChart;
    @FXML private Button startWorkoutButton;
    @FXML private Button planWorkoutButton;
    @FXML private Button viewProgressButton;
    @FXML private Button resetDatabaseButton;

    private User currentUser;
    private WorkoutDAO workoutDAO;
    private WorkoutScheduleDAO scheduleDAO;

    @FXML
    public void initialize() {
        // Setup reset database button
        if (resetDatabaseButton != null) {
            resetDatabaseButton.setOnAction(e -> handleResetDatabase());
        }
        try {
            workoutDAO = new WorkoutDAO(DatabaseHelper.getConnection());
            scheduleDAO = new WorkoutScheduleDAO(DatabaseHelper.getConnection());
            setupButtonHandlers();
        } catch (SQLException e) {
            DialogUtils.showError("Error", "Failed to initialize dashboard: " + e.getMessage());
        }
    }

    public void setUser(User user) {
        this.currentUser = user;
        welcomeLabel.setText("Welcome back, " + user.getUsername() + "!");
        loadDashboardData();
    }

    private void setupButtonHandlers() {
        if (resetDatabaseButton != null) {
            resetDatabaseButton.setOnAction(e -> handleResetDatabase());
        }
        startWorkoutButton.setOnAction(event -> handleStartWorkout());
        planWorkoutButton.setOnAction(event -> handlePlanWorkout());
        viewProgressButton.setOnAction(event -> handleViewProgress());
    }

    private void loadDashboardData() {
        loadWorkoutStreak();
        loadRecentWorkouts();
        loadProgressChart();
        loadUpcomingWorkouts();
    }

    private void loadWorkoutStreak() {
        try {
            LocalDate today = LocalDate.now();
            LocalDate monthAgo = today.minusMonths(1);
            List<Workout> recentWorkouts = workoutDAO.getWorkoutsByDateRange(
                currentUser.getUserId(), monthAgo, today);
            
            int streak = calculateStreak(recentWorkouts);
            streakLabel.setText(streak + " day streak!");
        } catch (SQLException e) {
            DialogUtils.showError("Error", "Failed to load workout streak: " + e.getMessage());
        }
    }

    private void loadRecentWorkouts() {
        try {
            List<Workout> recentWorkouts = workoutDAO.getWorkoutsByUserId(currentUser.getUserId());
            recentWorkoutsContainer.getChildren().clear();
            
            for (int i = 0; i < Math.min(5, recentWorkouts.size()); i++) {
                Workout workout = recentWorkouts.get(i);
                VBox workoutBox = createWorkoutSummaryBox(workout);
                recentWorkoutsContainer.getChildren().add(workoutBox);
            }
        } catch (SQLException e) {
            DialogUtils.showError("Error", "Failed to load recent workouts: " + e.getMessage());
        }
    }

    private void loadProgressChart() {
        // Clear existing data
        progressChart.getData().clear();

        // TODO: Add chart data based on workout history
        // This will depend on what metrics we want to track (e.g., weight lifted, workout duration)
    }

    private void loadUpcomingWorkouts() {
        try {
            LocalDate today = LocalDate.now();
            LocalDate nextWeek = today.plusWeeks(1);
            List<WorkoutSchedule> upcomingWorkouts = scheduleDAO.getScheduledWorkouts(
                currentUser.getUserId(), today, nextWeek);
            
            // Create and add upcoming workout boxes to a container
            for (WorkoutSchedule schedule : upcomingWorkouts) {
                VBox scheduleBox = new VBox(5);
                scheduleBox.getStyleClass().add("schedule-box");
                
                Label dateLabel = new Label(schedule.getDate().format(
                    DateTimeFormatter.ofPattern("MMMM d, yyyy")));
                Label typeLabel = new Label(schedule.getWorkoutType());
                
                scheduleBox.getChildren().addAll(dateLabel, typeLabel);
                // Add to your UI container for upcoming workouts
                // upcomingWorkoutsContainer.getChildren().add(scheduleBox);
            }
        } catch (SQLException e) {
            DialogUtils.showError("Error", "Failed to load upcoming workouts: " + e.getMessage());
        }
    }

    private VBox createWorkoutSummaryBox(Workout workout) {
        VBox box = new VBox(5);
        box.getStyleClass().add("workout-summary-box");

        Label dateLabel = new Label(workout.getDate().toString());
        Label typeLabel = new Label(workout.getWorkoutType());
        
        Button viewButton = new Button("View Details");
        viewButton.setOnAction(e -> handleViewWorkoutDetails(workout));

        box.getChildren().addAll(dateLabel, typeLabel, viewButton);
        return box;
    }

    private int calculateStreak(List<Workout> workouts) {
        if (workouts.isEmpty()) return 0;

        int streak = 1;
        LocalDate lastDate = workouts.get(0).getDate();
        
        for (int i = 1; i < workouts.size(); i++) {
            LocalDate currentDate = workouts.get(i).getDate();
            if (lastDate.minusDays(1).equals(currentDate)) {
                streak++;
                lastDate = currentDate;
            } else {
                break;
            }
        }
        
        return streak;
    }

    private void handleStartWorkout() {
        try {
            navigationController.navigateTo("WorkoutSession");
        } catch (Exception e) {
            DialogUtils.showError("Error", "Could not start workout: " + e.getMessage());
        }
    }

    private void handlePlanWorkout() {
        try {
            navigationController.navigateTo("WorkoutPlanning");
        } catch (Exception e) {
            DialogUtils.showError("Error", "Could not open workout planning: " + e.getMessage());
        }
    }

    private void handleViewProgress() {
        try {
            navigationController.navigateTo("ProgressTracking");
        } catch (Exception e) {
            DialogUtils.showError("Error", "Could not view progress: " + e.getMessage());
        }
    }

    private void handleViewWorkoutDetails(Workout workout) {
        try {
            navigationController.navigateTo("WorkoutDetails", controller -> {
                ((WorkoutDetailsController) controller).setWorkout(workout);
            });
        } catch (Exception e) {
            DialogUtils.showError("Error", "Could not view workout details: " + e.getMessage());
        }
    }

    private void handleResetDatabase() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/dashboard_reset_confirm.fxml"));
            VBox confirmBox = loader.load();
            Button confirmButton = (Button) confirmBox.lookup("#confirmResetButton");
            Button cancelButton = (Button) confirmBox.lookup("#cancelResetButton");

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("Confirm Database Reset");
            dialog.getDialogPane().setContent(confirmBox);
            dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
            dialog.setResizable(false);

            confirmButton.setOnAction(ev -> {
                try {
                    DatabaseHelper.clearDatabase();
                    dialog.close();
                    DialogUtils.showInformation("Database Reset", "All data has been cleared. The application will now reload.");
                    loadDashboardData();
                } catch (Exception ex) {
                    DialogUtils.showError("Error", "Failed to reset database: " + ex.getMessage());
                }
            });
            cancelButton.setOnAction(ev -> dialog.close());

            dialog.showAndWait();
        } catch (Exception ex) {
            DialogUtils.showError("Error", "Failed to show reset confirmation: " + ex.getMessage());
        }
    }
}