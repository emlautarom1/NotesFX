package app;

import app.controllers.DatabaseController;
import app.controllers.WindowController;
import app.entities.Note;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

import java.sql.SQLException;

public class Startup extends Application {

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
