package com.fitness.auth.controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.function.Consumer;

public class NavigationController {
    private Stage mainStage;
    private Stage previousStage;

    public NavigationController(Stage mainStage) {
        this.mainStage = mainStage;
    }

    public void setPreviousStage(Stage stage) {
        this.previousStage = stage;
    }

    public void navigateBack() {
        if (previousStage != null) {
            previousStage.show();
        }
        mainStage.close();
    }

    public void navigateToHome() {
        navigateTo("Dashboard");
    }

    public void navigateTo(String viewName) {
        navigateTo(viewName, null);
    }

    public void navigateTo(String viewName, Consumer<BaseController> controllerInitializer) {
        try {
            String fxmlPath = "/fxml/" + viewName + ".fxml";
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Pane root = loader.load();
            
            BaseController controller = loader.getController();
            if (controllerInitializer != null) {
                controllerInitializer.accept(controller);
            }
            
            Scene scene = new Scene(root);
            mainStage.setScene(scene);
            mainStage.show();
        } catch (IOException e) {
            throw new RuntimeException("Failed to navigate to " + viewName, e);
        }
    }
}
