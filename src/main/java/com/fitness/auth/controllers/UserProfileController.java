package com.fitness.auth.controllers;

import com.fitness.auth.models.*;
import com.fitness.auth.database.DatabaseConnection;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import javafx.stage.Stage;
import org.mindrot.jbcrypt.BCrypt;

public class UserProfileController {
    @FXML private TextField usernameField;
    @FXML private TextField emailField;
    @FXML private Label joinDateLabel;
    @FXML private PasswordField currentPasswordField;
    @FXML private PasswordField newPasswordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private Button editButton;
    @FXML private Button saveButton;
    @FXML private Button backButton;
    @FXML private Button homeButton;
    @FXML private Label errorLabel;

    private User currentUser;
    private UserDAO userDAO;
    private Stage stage;
    private NavigationController navigationController;

    @FXML
    public void initialize() {
        userDAO = new UserDAO(DatabaseConnection.getInstance());
        editButton.setOnAction(event -> handleEditProfile());
        saveButton.setOnAction(event -> handleSaveChanges());
        backButton.setOnAction(event -> handleBack());
        homeButton.setOnAction(event -> handleHome());
        setFieldsEditable(false);
        saveButton.setDisable(true);
    }

    public void initData(User user) {
        this.currentUser = user;
        loadUserData();
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setNavigationController(NavigationController navigationController) {
        this.navigationController = navigationController;
    }

    private void handleBack() {
        if (navigationController != null) {
            navigationController.navigateBack();
        } else {
            stage.close();
        }
    }

    private void handleHome() {
        if (navigationController != null) {
            navigationController.navigateToHome();
        } else {
            stage.close();
        }
    }

    private void loadUserData() {
        if (currentUser != null) {
            usernameField.setText(currentUser.getUsername());
            emailField.setText(currentUser.getEmail());
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy");
            joinDateLabel.setText(currentUser.getCreatedAt().format(formatter));
            clearPasswordFields();
        }
    }

    private void handleEditProfile() {
        setFieldsEditable(true);
        saveButton.setDisable(false);
        editButton.setDisable(true);
    }

    private void handleSaveChanges() {
        if (validateInput()) {
            try {
                saveChanges();
                setFieldsEditable(false);
                saveButton.setDisable(true);
                editButton.setDisable(false);
                showMessage("Changes saved successfully!", false);
            } catch (SQLException e) {
                showMessage("Error saving changes: " + e.getMessage(), true);
            }
        }
    }

    private boolean validateInput() {
        // Validate email if changed
        String newEmail = emailField.getText().trim();
        if (!newEmail.equals(currentUser.getEmail())) {
            if (!newEmail.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                showMessage("Invalid email format", true);
                return false;
            }
            try {
                if (userDAO.isEmailTaken(newEmail)) {
                    showMessage("Email is already in use", true);
                    return false;
                }
            } catch (SQLException e) {
                showMessage("Error checking email: " + e.getMessage(), true);
                return false;
            }
        }

        // Validate password if changing
        boolean changingPassword = !currentPasswordField.getText().isEmpty()
                || !newPasswordField.getText().isEmpty()
                || !confirmPasswordField.getText().isEmpty();

        if (changingPassword) {
            if (!BCrypt.checkpw(currentPasswordField.getText(), currentUser.getPasswordHash())) {
                showMessage("Current password is incorrect", true);
                return false;
            }

            if (newPasswordField.getText().length() < 8) {
                showMessage("New password must be at least 8 characters long", true);
                return false;
            }

            if (!newPasswordField.getText().matches(".*[0-9].*")) {
                showMessage("New password must contain at least one number", true);
                return false;
            }

            if (!newPasswordField.getText().matches(".*[a-zA-Z].*")) {
                showMessage("New password must contain at least one letter", true);
                return false;
            }

            if (!newPasswordField.getText().equals(confirmPasswordField.getText())) {
                showMessage("New passwords do not match", true);
                return false;
            }
        }

        return true;
    }

    private void saveChanges() throws SQLException {
        // Update email if changed
        String newEmail = emailField.getText().trim();
        if (!newEmail.equals(currentUser.getEmail())) {
            userDAO.updateEmail(currentUser.getUserId(), newEmail);
            currentUser.setEmail(newEmail);
        }

        // Update password if changed
        if (!currentPasswordField.getText().isEmpty()) {
            String hashedPassword = BCrypt.hashpw(newPasswordField.getText(), BCrypt.gensalt());
            userDAO.updatePassword(currentUser.getUserId(), hashedPassword);
            currentUser.setPasswordHash(hashedPassword);
        }

        clearPasswordFields();
    }

    private void setFieldsEditable(boolean editable) {
        emailField.setEditable(editable);
        currentPasswordField.setDisable(!editable);
        newPasswordField.setDisable(!editable);
        confirmPasswordField.setDisable(!editable);
    }

    private void clearPasswordFields() {
        currentPasswordField.clear();
        newPasswordField.clear();
        confirmPasswordField.clear();
    }

    private void showMessage(String message, boolean isError) {
        errorLabel.setText(message);
        errorLabel.setTextFill(isError ? Color.RED : Color.GREEN);
        errorLabel.setVisible(true);
    }
}
