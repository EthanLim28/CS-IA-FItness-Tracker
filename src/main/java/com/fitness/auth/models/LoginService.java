package com.fitness.auth.models;

import com.fitness.auth.database.DatabaseConnection;
import org.mindrot.jbcrypt.BCrypt;
import java.sql.SQLException;
import java.util.Optional;

public class LoginService {
    private final UserDAO userDAO;

    public LoginService() {
        this.userDAO = new UserDAO(DatabaseConnection.getInstance());
    }

    public User login(String username, String password) throws LoginException {
        try {
            Optional<User> userOptional = userDAO.findByUsername(username);
            
            if (userOptional.isEmpty()) {
                throw new LoginException("Invalid username or password");
            }

            User user = userOptional.get();
            
            if (!BCrypt.checkpw(password, user.getPasswordHash())) {
                throw new LoginException("Invalid username or password");
            }

            return user;
        } catch (SQLException e) {
            throw new LoginException("Database error occurred during login", e);
        }
    }
}
