package com.fitness.auth.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.function.Consumer;

public class FXMLUtils {
    public static Stage createDialogStage(Stage parentStage, String title, String fxmlPath) throws IOException {
        FXMLLoader loader = new FXMLLoader(FXMLUtils.class.getResource("/fxml/" + fxmlPath));
        Scene scene = new Scene(loader.load());
        
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
        Stage dialogStage = createDialogStage(parentStage, title, fxmlPath);
        T controller = getController(dialogStage);
        
        if (controller != null) {
            controllerInitializer.accept(controller);
        }
        
        dialogStage.showAndWait();
    }
}
