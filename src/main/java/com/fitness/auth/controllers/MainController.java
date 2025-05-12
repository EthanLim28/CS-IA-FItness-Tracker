package com.fitness.auth.controllers;

import com.fitness.auth.session.SessionManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

public class MainController extends BaseController {
    @FXML private VBox sidebarMenu;
    @FXML private Button dashboardButton;
    @FXML private Button workoutsButton;
    @FXML private Button planningButton;
    @FXML private Button progressButton;
    @FXML private Button profileButton;
    @FXML private Button logoutButton;
    
    private final SessionManager sessionManager;
    
    public MainController() {
        this.sessionManager = SessionManager.getInstance();
    }
    
    @FXML
    public void initialize() {
        setupNavigationHandlers();
        setupSessionListener();
    }
    
    private void setupNavigationHandlers() {
        dashboardButton.setOnAction(e -> navigateTo("/fxml/dashboard.fxml"));
        workoutsButton.setOnAction(e -> navigateTo("/fxml/workout-session.fxml"));
        planningButton.setOnAction(e -> navigateTo("/fxml/workout-planning.fxml"));
        progressButton.setOnAction(e -> navigateTo("/fxml/progress-tracking.fxml"));
        profileButton.setOnAction(e -> navigateTo("/fxml/user-profile.fxml"));
        logoutButton.setOnAction(e -> handleLogout());
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