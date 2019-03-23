package app.controllers;

import app.entities.User;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Logger;

public class LoginController extends Controller implements Initializable {

    // Logger
    // More info: http://www.vogella.com/tutorials/Logging/article.html
    private static final Logger LOGGER = Logger.getLogger(LoginController.class.getName());

    @FXML
    private TextField username_field;

    @FXML
    private PasswordField password_field;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    @FXML
    void loginAttempt() {
        LOGGER.info("Attempting to log in, user is: " + username_field.getText()
                + ", password is: " + password_field.getText());
        try {
            User autenticatedUser = DatabaseController.authenticateUser(
                    username_field.getText(),
                    password_field.getText()
            );
            NotesController notesController = WindowController.openNotesWindow();
            notesController.setUser(autenticatedUser);
            closeCurrentWindow();
        } catch (SecurityException e) {
            WindowController.higlightInvalidInput(password_field);
            LOGGER.warning("The password is invalid");
            // TODO: 19/12/2018 Inform user of wrong validation
        } catch (SQLException e) {
            WindowController.higlightInvalidInput(username_field);
            LOGGER.warning("Failed to retrieve user from SQL Database");
        } catch (IOException e) {
            LOGGER.warning("Failed to open window");
            e.printStackTrace();
        }
    }

    @FXML
    private void openCreateAccountWindow() {
        try {
            WindowController.openRegisterUserWindow();
        } catch (IOException e) {
            LOGGER.warning("Failed to open window");
            e.printStackTrace();
        }
        closeCurrentWindow();
    }

}
