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

public class RegisterUserController extends Controller implements Initializable {
    private static final Logger LOGGER = Logger.getLogger(RegisterUserController.class.getName());

    @FXML
    private TextField name_field;

    @FXML
    private TextField username_field;

    @FXML
    private PasswordField password_field;


    @FXML
    void registerNewUser() {
        User userToRegister = new User(
                name_field.getText(),
                username_field.getText(),
                password_field.getText()
        );
        try {
            userToRegister.validateUser();
            LOGGER.info("User was validated");
            DatabaseController.registerValidatedUser(userToRegister);
            WindowController.displayInformation("User registered successfully.");
            LOGGER.info("New user added:" +
                    "\nName: " + userToRegister.getName() +
                    "\nUsername: " + userToRegister.getUsername() +
                    "\nPassword: " + userToRegister.getPassword()
            );
            WindowController.openLoginWindow();
            closeCurrentWindow();
        } catch (SQLException e) {
            WindowController.displayError("SQL Error: Failed to register a new User.");
            e.printStackTrace();
        } catch (Exception e) {
            WindowController.displayError
                    ("Validation Error: Username/Password are not valid. Use at least 8 characters.");
        }

    }

    @FXML
    void cancelCreateAccount() {
        if (WindowController.displayConfirmation
                ("Are you sure you want to cancel the operation?")) {
            try {
                WindowController.openLoginWindow();
                closeCurrentWindow();
            } catch (IOException e) {
                LOGGER.warning("Failed to open window");
                e.printStackTrace();
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        LOGGER.info("Initialized create account controller, URL: " + location.getPath());
    }
}
