package main;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import main.controllers.DatabaseController;
import main.controllers.WindowController;
import main.entities.Note;

import java.sql.SQLException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            int nextValidID = DatabaseController.getNextNoteValidID();
            Note.setIdGenerator(nextValidID);
        } catch (SQLException e) {
            // TODO: 23/12/2018 Display SQL fatal failure
            Platform.exit();
        }
        WindowController.openLoginWindow();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
