package com.fitness.auth.controllers;

import javafx.stage.Stage;

/**
 * Base controller class that provides common functionality for all controllers.
 * All controllers in the application should extend this class.
 */
public abstract class BaseController {
    protected Stage stage;
    protected NavigationController navigationController;

    /**
     * Sets the stage for this controller.
     * @param stage The JavaFX stage
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Sets the navigation controller for this controller.
     * @param navigationController The navigation controller
     */
    public void setNavigationController(NavigationController navigationController) {
        this.navigationController = navigationController;
    }

    /**
     * Called when the controller is being initialized.
     * Subclasses should override this method to perform their initialization.
     */
    protected void initialize() {
        // Default empty implementation
    }

    /**
     * Called when the controller's view is being shown.
     * Subclasses can override this to perform actions when their view becomes visible.
     */
    protected void onShow() {
        // Default empty implementation
    }

    /**
     * Called when the controller's view is being hidden.
     * Subclasses can override this to perform cleanup when their view is hidden.
     */
    protected void onHide() {
        // Default empty implementation
    }

    /**
     * Closes the current stage.
     */
    protected void closeStage() {
        if (stage != null) {
            stage.close();
        }
    }

    /**
     * Navigates back to the previous screen if a navigation controller is set.
     * Otherwise, closes the current stage.
     */
    protected void navigateBack() {
        if (navigationController != null) {
            navigationController.navigateBack();
        } else {
            closeStage();
        }
    }
}