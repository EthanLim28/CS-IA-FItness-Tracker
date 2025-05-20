package com.fitness.auth.controllers;

import com.fitness.auth.models.User;

import com.fitness.auth.session.SessionManager;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import java.io.IOException;

public class MainController extends BaseController {
    @FXML private VBox sidebarMenu;

    @FXML private Button startWorkoutButton;
    @FXML private Button planWorkoutButton;
    @FXML private Button viewProgressButton;
    @FXML private Button logoutButton;
    @FXML private Button resetDatabaseButton;
    @FXML private Label usernameLabel;
    private final SessionManager sessionManager = SessionManager.getInstance();
    @FXML private ListView<?> pastWorkoutsList;

    // Inject NavigationController if possible
    @FXML private NavigationController navigationController;
    public void setNavigationController(NavigationController navigationController) {
        this.navigationController = navigationController;
        System.out.println("[DEBUG] setNavigationController called: navigationController set to " + navigationController);

    }
    

    

    
    @FXML
    public void initialize() {
        setupNavigationHandlers();
        setupSessionListener();
        updateUsernameLabel();
        sessionManager.currentUserProperty().addListener((obs, oldUser, newUser) -> updateUsernameLabel());
    }

    private void updateUsernameLabel() {
        User user = sessionManager.getCurrentUser();
        if (user != null) {
            usernameLabel.setText("Welcome, " + user.getUsername());
        } else {
            usernameLabel.setText("Welcome");
        }
    }
    
    private void setupNavigationHandlers() {
    if (startWorkoutButton != null) {
        startWorkoutButton.setOnAction(e -> {
    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/workout_session.fxml"));
        Parent root = loader.load();
        WorkoutSessionController controller = loader.getController();
        Stage stage = (Stage) startWorkoutButton.getScene().getWindow();
        controller.setStage(stage);
        // If you have a navigationController instance, pass it
        if (this.navigationController != null) {
            controller.setNavigationController(this.navigationController);
        }
        stage.setScene(new Scene(root));
    } catch (IOException ex) {
        ex.printStackTrace();
        System.err.println("[ERROR] Failed to load workout_session.fxml");
    }
});
    }
    if (planWorkoutButton != null) {
        planWorkoutButton.setOnAction(e -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/workout_planning.fxml"));
                Parent root = loader.load();
                Stage stage = (Stage) planWorkoutButton.getScene().getWindow();
                stage.setScene(new Scene(root));
            } catch (IOException ex) {
                ex.printStackTrace();
                System.err.println("[ERROR] Failed to load workout_planning.fxml");
            }
        });
    }
    if (viewProgressButton != null) {
        viewProgressButton.setOnAction(e -> {
            if (this.navigationController != null) {
                this.navigationController.navigateTo("progress_tracking");
            } else {
                System.err.println("[ERROR] NavigationController is null: cannot navigate to progress_tracking");
            }
        });
    }
    if (resetDatabaseButton != null) {
        resetDatabaseButton.setOnAction(e -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/dashboard_reset_confirm.fxml"));
                Parent root = loader.load();
                Stage stage = (Stage) resetDatabaseButton.getScene().getWindow();
                stage.setScene(new Scene(root));
            } catch (IOException ex) {
                ex.printStackTrace();
                System.err.println("[ERROR] Failed to load dashboard_reset_confirm.fxml");
            }
        });
    }
    if (logoutButton != null) {
        logoutButton.setOnAction(e -> handleLogout());
    }
}
    
    private void setupSessionListener() {
        sessionManager.currentUserProperty().addListener((obs, oldUser, newUser) -> {
            if (newUser == null) {
                // User logged out, navigate to login screen
                navigateTo("/fxml/login.fxml");
            }
        });
    }
    
    private void handleLogout() {
        sessionManager.clearSession();
    }
    
    private void navigateTo(String fxmlPath) {
        navigationController.navigateTo(fxmlPath);
    }
}