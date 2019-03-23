package app.controllers;

import app.entities.Note;
import app.entities.User;
import app.utils.CustomDateFormat;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Logger;

public class NotesController extends Controller implements Initializable {
    // For DialogBox check: https://code.makery.ch/blog/javafx-dialogs-official/
    // For observable filtered list check: https://code.makery.ch/blog/javafx-8-tableview-sorting-filtering/

    private static final Logger LOGGER = Logger.getLogger(NotesController.class.getName());

    /* Set user from outside of Controller */
    // Private property. Should not be edited nor used.
    private final ObjectProperty<User> user = new SimpleObjectProperty<>();

    // Private getter. Should not be accesed directly.
    private ObjectProperty<User> userObjectProperty() {
        return user;
    }

    private User getUser() {
        return userObjectProperty().get();
    }

    // Package-private set user. Other app.controllers can use this to set User.
    final void setUser(User user) {
        userObjectProperty().setValue(user);
        // Once the user is set, call user is loaded.
        userIsLoaded();
    }

    private final ObservableList<Note> noteObservableList = FXCollections.observableArrayList();

    @FXML
    private TextField search_field;

    @FXML
    private TableView<Note> notes_table;

    @FXML
    private TableColumn<Note, Date> date_column;

    @FXML
    private TableColumn<Note, String> note_column;

    @FXML
    private MenuItem edit_note_menu_item;

    @FXML
    private MenuItem delete_note_menu_item;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // On initialize only set up table layout.
        // DO NOT load notes yet since user may not be loaded.
        setUpTableContent();
        setUpTableDeselect();
        setUpMenuItemsAvailability();
    }

    @FXML
    private void handleFilterDisplayedNotes() {
        notes_table.setItems(
                new FilteredList<>(
                        noteObservableList, // Source
                        note -> note.getNote().toUpperCase().contains(
                                search_field.getText().toUpperCase()
                        ) // Filter
                )
        );
        if (notes_table.getItems().isEmpty()) {
            LOGGER.warning("Table is empty");
        }
    }

    @FXML
    void handleCreateNewNote() {
        Optional<String> result = displayNewNoteDialog();
        result.ifPresent(noteText -> {
            try {
                insertNoteAndUpdateView(noteText);
            } catch (SQLException e) {
                WindowController.displayError("SQL Failure: Failed to insert your Note.");
                LOGGER.warning("Failed to insert note");
            }
        });
    }

    @FXML
    void handleDeleteSelectedNotes() {
        Note selectedNote = notes_table.getSelectionModel().getSelectedItem();
        if (WindowController.displayConfirmation
                ("Are you sure you want to delete this Note?")
        ) {
            try {
                deleteNoteAndUpdateView(selectedNote);
                LOGGER.info("Deleting note: " + selectedNote.getNote() + " with ID: " + selectedNote.getId());
            } catch (SQLException e) {
                WindowController.displayError("SQL Failure: Could not delete your Note.");
                LOGGER.warning("Failed to delete note: " + selectedNote.getNote() + " with ID: " + selectedNote.getId());
                e.printStackTrace();
            }
        }
    }

    @FXML
    void editSelectedNote() {
        Note selectedNote = notes_table.getSelectionModel().getSelectedItem();
        Optional<String> newNoteContent = displayEditNoteDialog(selectedNote.getNote());
        if (newNoteContent.isPresent() &&
                !newNoteContent.get().equals(selectedNote.getNote())
        ) {
            try {
                updateNoteAndUpdateView(selectedNote, newNoteContent.get());
            } catch (SQLException e) {
                WindowController.displayError("SQL Error: Failed to update your Note.");
                LOGGER.warning("Failed to update note: " + selectedNote.getNote() + " with ID: " + selectedNote.getId());
            }
        }
    }


    @FXML
    private void handleExitApplication() {
        if (WindowController.displayConfirmation("Are you sure you want to close the application?"))
            Platform.exit();
    }

    @FXML
    private void handleCloseSesion() {
        if (WindowController.displayConfirmation
                ("Are you sure you want to close your sesion?")
        ) {
            try {
                WindowController.openLoginWindow();
                closeCurrentWindow();
            } catch (IOException e) {
                LOGGER.warning("Failed to open window");
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void displayVersionInfo() {
        WindowController.displayInformation("Notes-FX: Version 1.2.\nBuilt in 11 + JavaFX.\nArgentina.");
    }

    private Optional<String> displayNewNoteDialog() {
        TextInputDialog dialog = new TextInputDialog();
        WindowController.styleDialog(dialog);
        dialog.setTitle("New Note");
        dialog.setHeaderText("Create a new Note");
        dialog.setContentText("Please enter your note:");
        return dialog.showAndWait();
    }

    private Optional<String> displayEditNoteDialog(String originalNote) {
        TextInputDialog dialog = new TextInputDialog(originalNote);
        dialog.setTitle("Edit Note");
        dialog.setHeaderText("Edit a Note");
        dialog.setContentText("Please enter your note:");
        return dialog.showAndWait();
    }

    private void userIsLoaded() {
        loadNotesToTable();
        LOGGER.info("Notes for user " + getUser().getUsername() + " where loaded.");
    }

    private void setUpTableContent() {
        date_column.setCellValueFactory(new PropertyValueFactory<>("writeDate"));
        date_column.setCellFactory(
                column -> new TableCell<>() {
                    @Override
                    protected void updateItem(Date item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty) {
                            setText(null);
                        } else {
                            setText(CustomDateFormat.formatDate(item));
                        }
                    }
                }
        );
        note_column.setCellValueFactory(new PropertyValueFactory<>("note"));
        // Start by displaying all notes
        notes_table.setItems(
                new FilteredList<>(noteObservableList, note -> true)
        );
    }

    private void setUpTableDeselect() {
        notes_table.setRowFactory(tableView -> {
            TableRow<Note> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (row.isEmpty())
                    notes_table.getSelectionModel().clearSelection();
            });
            return row;
        });
    }

    private void setUpMenuItemsAvailability() {
        edit_note_menu_item.setDisable(true);
        delete_note_menu_item.setDisable(true);

        notes_table.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> {
                    boolean disable = newSelection == null;
                    edit_note_menu_item.setDisable(disable);
                    delete_note_menu_item.setDisable(disable);
                }
        );
    }

    private void loadNotesToTable() {
        try {
            noteObservableList.addAll(
                    DatabaseController.getNotesFromUser(getUser().getUsername())
            );
        } catch (SQLException e) {
            WindowController.displayError("SQL Error: Failed to retrieve Notes from SQL Database.");
            LOGGER.warning("Failed to retrieve Notes from SQL Database.");
        }
    }

    private void insertNoteAndUpdateView(String noteText) throws SQLException {
        Note note = new Note(noteText, getUser().getUsername());
        DatabaseController.insertNote(note);
        noteObservableList.add(note);
        notes_table.refresh();
    }

    private void deleteNoteAndUpdateView(Note note) throws SQLException {
        DatabaseController.deleteNote(note);
        noteObservableList.remove(note);
        notes_table.refresh();
    }

    private void updateNoteAndUpdateView(Note note, String newNoteContent) throws SQLException {
        noteObservableList.remove(note);
        note.setNote(newNoteContent);
        DatabaseController.updateNote(note);
        noteObservableList.add(note);
        notes_table.refresh();
    }
}
