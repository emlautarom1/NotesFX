package main;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import main.controllers.DatabaseController;
import main.controllers.WindowController;
import main.entities.Note;

import java.sql.SQLException;

public class Main extends Application {

    private void initNotesID() throws SQLException {
        int nextValidID = DatabaseController.getNextNoteValidID();
        Note.setIdGenerator(nextValidID);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            initNotesID();
            WindowController.openLoginWindow();
        } catch (SQLException e) {
            WindowController.displayError("Fatal App failure. Closing app...");
            Platform.exit();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
