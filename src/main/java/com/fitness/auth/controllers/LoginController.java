package com.fitness.auth.controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import com.fitness.auth.models.User;
import com.fitness.auth.models.UserDAO;
import com.fitness.auth.util.DialogUtils;
import com.fitness.auth.util.PasswordUtils;
import com.fitness.auth.session.SessionManager;
import com.fitness.auth.database.DatabaseConnection;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;

import java.sql.SQLException;
import java.util.Optional;

public class LoginController extends BaseController {
    private NavigationController navigationController;

    public void setNavigationController(NavigationController navigationController) {
        this.navigationController = navigationController;
    }
    @FXML private TextField loginIdentifierField;
    @FXML private PasswordField loginPasswordField;
    @FXML private javafx.scene.control.Label loginErrorLabel;
    @FXML private javafx.scene.control.Button loginButton;

    // Registration tab fields
    @FXML private TextField regUsernameField;
    @FXML private TextField regEmailField;
    @FXML private PasswordField regPasswordField;
    @FXML private PasswordField regConfirmPasswordField;
    @FXML private Label regErrorLabel;
    @FXML private javafx.scene.control.Button registerButton;
    @FXML private javafx.scene.control.TabPane tabPane;
    
    private UserDAO userDAO;
    private SessionManager sessionManager;
    
    @FXML
    public void initialize() {
        DatabaseConnection dbConnection = DatabaseConnection.getInstance();
        userDAO = new UserDAO(dbConnection);
        sessionManager = SessionManager.getInstance();

        if (loginButton != null) {
            loginButton.setOnAction(e -> handleLogin());
        }
        if (registerButton != null) {
            registerButton.setOnAction(e -> handleRegister());
        }
        if (regErrorLabel != null) {
            regErrorLabel.setVisible(false);
        }
    }
    
    @FXML
    private void handleLogin() {
        System.out.println("[DEBUG] handleLogin() called");
        if (loginErrorLabel != null) loginErrorLabel.setVisible(false);
        String identifier = loginIdentifierField.getText() != null ? loginIdentifierField.getText().trim() : null;
        String password = loginPasswordField.getText();
        System.out.println("[DEBUG] identifier: '" + identifier + "', password: '" + (password != null ? "[HIDDEN]" : "null") + "'");

        if (identifier.isEmpty() || password.isEmpty()) {
            if (loginErrorLabel != null) {
                loginErrorLabel.setText("Please enter both email/username and password.");
                loginErrorLabel.setVisible(true);
            } else {
                DialogUtils.showError("Error", "Please enter both email/username and password.");
            }
            return;
        }

        try {
            System.out.println("[DEBUG] Attempting to find user by email: " + identifier);
            Optional<User> userOpt = userDAO.findByEmail(identifier);
            if (userOpt.isEmpty()) {
                System.out.println("[DEBUG] No user found by email, trying username: " + identifier);
                userOpt = userDAO.findByUsername(identifier);
            }
            if (userOpt.isPresent() && PasswordUtils.verifyPassword(password, userOpt.get().getPasswordHash())) {
                User user = userOpt.get();
                System.out.println("[DEBUG] Login successful, navigating to dashboard");
                sessionManager.setCurrentUser(user);
                try {
                    // Load main.fxml and set navigationController on MainController
                    try {
                        FXMLLoader mainLoader = new FXMLLoader(getClass().getResource("/fxml/main.fxml"));
                        Parent mainRoot = mainLoader.load();
                        MainController mainController = mainLoader.getController();
                        mainController.setNavigationController(navigationController);
                        // Set the scene on the current stage
                        Stage stage = (Stage) loginButton.getScene().getWindow();
                        stage.setScene(new Scene(mainRoot));
                    } catch (Exception e) {
                        System.out.println("[DEBUG] Failed to load main.fxml after login: " + e.getMessage());
                        e.printStackTrace();
                    }
                } catch (Exception navEx) {
                    System.out.println("[DEBUG] Navigation failed: " + navEx.getMessage());
                    navEx.printStackTrace();
                }
            } else {
                System.out.println("[DEBUG] Invalid credentials");
                if (loginErrorLabel != null) {
                    loginErrorLabel.setText("Invalid email/username or password.");
                    loginErrorLabel.setVisible(true);
                } else {
                    DialogUtils.showError("Login Failed", "Invalid email/username or password.");
                }
            }
        } catch (SQLException e) {
            System.out.println("[DEBUG] Database error during login: " + e.getMessage());
            if (loginErrorLabel != null) {
                loginErrorLabel.setText("Database error: " + e.getMessage());
                loginErrorLabel.setVisible(true);
            } else {
                DialogUtils.showError("Error", "Database error: " + e.getMessage());
            }
        }
    }
    
    @FXML
    public void handleRegister() {
        if (regErrorLabel != null) regErrorLabel.setVisible(false);
        String username = regUsernameField.getText().trim();
        String email = regEmailField.getText().trim();
        String password = regPasswordField.getText();
        String confirmPassword = regConfirmPasswordField.getText();

        if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            regErrorLabel.setText("Please fill in all fields.");
            regErrorLabel.setVisible(true);
            return;
        }
        if (!password.equals(confirmPassword)) {
            regErrorLabel.setText("Passwords do not match.");
            regErrorLabel.setVisible(true);
            return;
        }
        if (password.length() < 8 || !password.matches(".*\\d.*") || !password.matches(".*[A-Za-z].*")) {
            regErrorLabel.setText("Password does not meet requirements.");
            regErrorLabel.setVisible(true);
            return;
        }
        try {
            if (userDAO.findByEmail(email).isPresent()) {
                regErrorLabel.setText("Email already registered.");
                regErrorLabel.setVisible(true);
                return;
            }
            String passwordHash = com.fitness.auth.util.PasswordUtils.hashPassword(password);
            User newUser = new User(username, email, passwordHash);
            userDAO.createUser(newUser);
            // Show success dialog
            com.fitness.auth.util.DialogUtils.showInformation("Success", "Account created! Please log in.");
            // Clear fields and switch to login tab
            regUsernameField.clear();
            regEmailField.clear();
            regPasswordField.clear();
            regConfirmPasswordField.clear();
            regErrorLabel.setVisible(false);
            if (tabPane != null) {
                tabPane.getSelectionModel().select(0); // Switch to login tab
            }
        } catch (Exception e) {
            regErrorLabel.setText("Database error: " + e.getMessage());
            regErrorLabel.setVisible(true);
        }
    }
    
    @FXML
    private void handleForgotPassword() {
        navigationController.navigateTo("/fxml/forgot-password.fxml");
    }
}