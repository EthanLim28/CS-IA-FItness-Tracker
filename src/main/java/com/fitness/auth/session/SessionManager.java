package com.fitness.auth.session;

import com.fitness.auth.models.User;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class SessionManager {
    private static SessionManager instance;
    private final ObjectProperty<User> currentUser = new SimpleObjectProperty<>();
    
    private SessionManager() {}
    
    public static synchronized SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }
    
    public User getCurrentUser() {
        return currentUser.get();
    }
    
    public void setCurrentUser(User user) {
        currentUser.set(user);
    }
    
    public ObjectProperty<User> currentUserProperty() {
        return currentUser;
    }
    
    public void clearSession() {
        currentUser.set(null);
    }
    
    public boolean isLoggedIn() {
        return currentUser.get() != null;
    }
}
