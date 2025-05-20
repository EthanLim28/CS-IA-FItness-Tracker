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
        // Instead of closing the main stage, just navigate to the previous view if possible, or to home as fallback
        if (previousStage != null) {
            previousStage.show();
            mainStage.hide();
        } else {
            navigateToHome();
        }
    }

    public void navigateToHome() {
        navigateTo("main");
    }

    public void navigateTo(String viewName) {
        navigateTo(viewName, null);
    }

    public void navigateTo(String viewName, Consumer<BaseController> controllerInitializer) {
        try {
            String fxmlPath = "/fxml/" + viewName + ".fxml";
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource(fxmlPath));
            Pane root = loader.load();
            
            BaseController controller = loader.getController();
            if (controller == null) {
                System.out.println("[DEBUG] NavigationController: loader.getController() returned null for viewName=" + viewName + ", fxmlPath=" + fxmlPath);
            } else {
                System.out.println("[DEBUG] NavigationController: Loaded controller class: " + controller.getClass().getName());
                if (!(controller instanceof BaseController)) {
                    System.out.println("[DEBUG] NavigationController: WARNING: Controller is not a BaseController! Actual class: " + controller.getClass().getName());
                }
            }
            if (controller != null) {
                try {
                    controller.getClass().getMethod("setNavigationController", NavigationController.class);
                    System.out.println("[DEBUG] NavigationController: setNavigationController method found, invoking...");
                    controller.getClass().getMethod("setNavigationController", NavigationController.class)
                        .invoke(controller, this);
                } catch (Exception e) {
                    System.out.println("[DEBUG] NavigationController: setNavigationController method NOT found or failed: " + e);
                }
                // Automatically call setStage(mainStage) if available
                try {
                    controller.getClass().getMethod("setStage", Stage.class);
                    System.out.println("[DEBUG] NavigationController: setStage method found, invoking...");
                    controller.getClass().getMethod("setStage", Stage.class)
                        .invoke(controller, mainStage);
                } catch (Exception e) {
                    System.out.println("[DEBUG] NavigationController: setStage method NOT found or failed: " + e);
                }
            }
            if (controllerInitializer != null) {
                controllerInitializer.accept(controller);
            }
            
            Scene scene = new Scene(root);
            mainStage.setScene(scene);
            mainStage.show();
        } catch (Exception e) {
            e.printStackTrace(); // Print detailed error if FXML/controller instantiation fails
            throw new RuntimeException("Failed to navigate to " + viewName, e);
        }
    }
}
