package main.controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import main.Main;

import java.io.IOException;

public class WindowController {
    /**
     * @param layout layout file in .fxml
     * @param title  Tilte of the window to be created
     * @return Object to be casted to a proper controller. Use to send information between controllers
     * @throws IOException if the window cannot be created.
     */
    private static Object openNewWindow(String layout, String title) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("layouts/" + layout));
        Parent root = fxmlLoader.load();
        Stage stage = new Stage();
        stage.setTitle(title);
        stage.setScene(new Scene(root));
        // Force FXML min sizes (width/height)
        stage.setMinWidth(root.minWidth(-1));
        stage.setMinHeight(root.minHeight(-1));
        stage.show();
        return fxmlLoader.getController();
    }

    public static LoginController openLoginWindow() throws IOException {
        return (LoginController) openNewWindow("loginMenu.fxml", "Login");
    }

    public static RegisterUserController openRegisterUserWindow() throws IOException{
        return (RegisterUserController) openNewWindow("registerNewUserMenu.fxml", "Register User");
    }

    public static NotesController openNotesWindow() throws IOException {
        return (NotesController) openNewWindow("notesMenu.fxml", "Notes");
    }

    public static boolean displayConfirmation(String question) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText(question);
        alert.setHeaderText(null);
        alert.showAndWait();
        return (alert.getResult() == ButtonType.OK);
    }

    public static void displayError(String content){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(content);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}
