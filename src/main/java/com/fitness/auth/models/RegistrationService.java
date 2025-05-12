package com.fitness.auth.models;

import com.fitness.auth.database.DatabaseConnection;
import org.mindrot.jbcrypt.BCrypt;
import java.sql.SQLException;

public class RegistrationService {
    private final UserDAO userDAO;

    public RegistrationService() {
        this.userDAO = new UserDAO(DatabaseConnection.getInstance());
    }

    public User register(String username, String email, String password) throws RegistrationException {
        try {
            // Check if username already exists
            if (userDAO.findByUsername(username).isPresent()) {
                throw new RegistrationException("Username already exists");
            }

            // Check if email already exists
            if (userDAO.findByEmail(email).isPresent()) {
                throw new RegistrationException("Email already exists");
            }

            // Validate password strength
            if (!isPasswordStrong(password)) {
                throw new RegistrationException("Password is not strong enough");
            }

            // Hash password
            String passwordHash = BCrypt.hashpw(password, BCrypt.gensalt());

            // Create user
            User user = new User(username, email, passwordHash);
            userDAO.createUser(user);

            return user;
        } catch (SQLException e) {
            throw new RegistrationException("Database error occurred during registration", e);
        }
    }

    private boolean isPasswordStrong(String password) {
        // Password must be at least 8 characters long
        if (password.length() < 8) {
            return false;
        }

        boolean hasLetter = false;
        boolean hasDigit = false;
        boolean hasSpecial = false;

        for (char c : password.toCharArray()) {
            if (Character.isLetter(c)) {
                hasLetter = true;
            } else if (Character.isDigit(c)) {
                hasDigit = true;
            } else {
                hasSpecial = true;
            }
        }

        // Password must contain at least one letter, one digit, and one special character
        return hasLetter && hasDigit && hasSpecial;
    }
}
