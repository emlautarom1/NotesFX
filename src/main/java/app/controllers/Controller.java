package app.controllers;

import javafx.stage.Stage;

class Controller {

    private Stage currentStage;

    void setCurrentStage(Stage currentStage) {
        this.currentStage = currentStage;
    }

    void closeCurrentWindow() {
        this.currentStage.close();
    }
}
