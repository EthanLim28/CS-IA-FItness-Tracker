package com.fitness.auth.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.function.Consumer;

public class FXMLUtils {
    public static Stage createDialogStage(Stage parentStage, String title, String fxmlPath) throws IOException {
        String resourcePath = "/fxml/" + fxmlPath;
        System.out.println("[DEBUG] Attempting to load FXML: " + resourcePath);
        java.net.URL resourceUrl = FXMLUtils.class.getResource(resourcePath);
        System.out.println("[DEBUG] Resource URL: " + resourceUrl);
        if (resourceUrl == null) {
            throw new IOException("FXML resource not found: " + resourcePath);
        }
        FXMLLoader loader = new FXMLLoader(resourceUrl);
        Scene scene = new Scene(loader.load());
        // Set the loader as userData so getController works
        scene.setUserData(loader);
        
        Stage dialogStage = new Stage();
        dialogStage.setTitle(title);
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(parentStage);
        dialogStage.setScene(scene);
        
        return dialogStage;
    }

    public static <T> T getController(Stage dialogStage) {
        Scene scene = dialogStage.getScene();
        FXMLLoader loader = (FXMLLoader) scene.getUserData();
        return loader.getController();
    }

    public static <T> void showDialog(String fxmlPath, String title, Stage parentStage, Consumer<T> controllerInitializer) throws IOException {
        Stage dialogStage = createDialogStage(parentStage, title, fxmlPath.toLowerCase());
        T controller = getController(dialogStage);
        
        if (controller != null) {
            controllerInitializer.accept(controller);
        }
        
        dialogStage.showAndWait();
    }
}
