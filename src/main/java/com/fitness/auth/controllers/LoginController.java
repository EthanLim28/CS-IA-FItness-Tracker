package com.fitness.auth.controllers;

import com.fitness.auth.models.User;
import com.fitness.auth.models.UserDAO;
import com.fitness.auth.util.DialogUtils;
import com.fitness.auth.util.PasswordUtils;
import com.fitness.auth.session.SessionManager;
import com.fitness.auth.database.DatabaseConnection;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.sql.SQLException;
import java.util.Optional;

public class LoginController extends BaseController {
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    
    private UserDAO userDAO;
    private SessionManager sessionManager;
    
    @FXML
    public void initialize() {
        DatabaseConnection dbConnection = DatabaseConnection.getInstance();
        userDAO = new UserDAO(dbConnection);
        sessionManager = SessionManager.getInstance();
    }
    
    @FXML
    private void handleLogin() {
        String email = emailField.getText().trim();
        String password = passwordField.getText();
        
        if (email.isEmpty() || password.isEmpty()) {
            DialogUtils.showError("Error", "Please enter both email and password.");
            return;
        }
        
        try {
            Optional<User> userOpt = userDAO.findByEmail(email);
            if (userOpt.isPresent() && PasswordUtils.verifyPassword(password, userOpt.get().getPasswordHash())) {
                User user = userOpt.get();
                sessionManager.setCurrentUser(user);
                navigationController.navigateTo("/fxml/dashboard.fxml");
            } else {
                DialogUtils.showError("Login Failed", "Invalid email or password.");
            }
        } catch (SQLException e) {
            DialogUtils.showError("Error", "Database error: " + e.getMessage());
        }
    }
    
    @FXML
    private void handleRegister() {
        navigationController.navigateTo("/fxml/register.fxml");
    }
    
    @FXML
    private void handleForgotPassword() {
        navigationController.navigateTo("/fxml/forgot-password.fxml");
    }
}