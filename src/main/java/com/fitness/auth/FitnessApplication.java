package com.fitness.auth;

import com.fitness.auth.models.DatabaseHelper;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.net.URL;

public class FitnessApplication extends Application {
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        // Initialize database
        DatabaseHelper.initializeDatabase();

        // Load the login view
        FXMLLoader loader = new FXMLLoader();
        URL loginFxml = getClass().getResource("/fxml/login.fxml");
        if (loginFxml == null) {
            throw new RuntimeException("Cannot find /fxml/login.fxml");
        }
        loader.setLocation(loginFxml);
        Parent root = loader.load();
        
        // Create scene and add stylesheet
        Scene scene = new Scene(root);
        URL cssFile = getClass().getResource("/styles/main.css");
        if (cssFile != null) {
            scene.getStylesheets().add(cssFile.toExternalForm());
        }
        
        // Set up the primary stage
        primaryStage.setTitle("Fitness Tracker");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
