package To_Do_FX;

import main.controllers.DatabaseController;
import main.entities.Note;
import main.entities.User;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DatabaseControllerTest {
    private static final String TEST_USERNAME = "lautaroem1";
    private static final String TEST_NAME = "LAUTARO EMANUEL";
    private static final String TEST_PASSWORD = "MYPASSWORD";

    @Test
    public void create_database_and_tables() {
        try {
            Connection c = DatabaseController.getConnection();
            ResultSet dbTables = c.createStatement().executeQuery("SELECT name FROM sqlite_master WHERE type='table';");
            List<String> tableNames = new ArrayList<>();
            while (dbTables.next()) {
                tableNames.add(dbTables.getString(1));
            }
            List<String> expectedTables = Arrays.asList("USERS", "NOTES", "sqlite_sequence");
            assertEquals(expectedTables, tableNames);
        } catch (SQLException e) {
            e.printStackTrace();
            fail("Catched SQL Exception: " + e.getMessage());
        }
    }

    @Test
    public void insert_validated_user_into_database() {
        User test = new User(TEST_NAME, TEST_USERNAME, TEST_PASSWORD);
        try {
            DatabaseController.registerValidatedUser(test);
        } catch (SQLException e) {
            e.printStackTrace();
            fail("Catched SQL Exception: " + e.getMessage());
        }
    }

    @Test
    public void insert_note_into_database() {
        // User should be already in database
        try {
            Note.setIdGenerator(DatabaseController.getNextNoteValidID());
            Note note = new Note("This is a test note", TEST_USERNAME);
            DatabaseController.insertNote(note);
        } catch (SQLException e) {
            fail("Catched SQL Exception: " + e.getMessage());
        }
    }

    @Test
    public void get_notes_from_test_user() {
        try {
            List<Note> queryNoteList = DatabaseController.getNotesFromUser(TEST_USERNAME);
            if (queryNoteList != null && !queryNoteList.isEmpty()) {
                for (Note n : queryNoteList) {
                    System.out.println("Note: " + n.getNote());
                    System.out.println("User: " + n.getUsername());
                    System.out.println("Writedate: " + n.getWriteDate());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            fail("Catched SQL Exception: " + e.getMessage());
        }
    }

    @Test
    public void get_user() {
        try {
            User result = DatabaseController.getUser(TEST_USERNAME);
            assertEquals(TEST_NAME, result.getName());
            assertEquals(TEST_USERNAME, result.getUsername());
            assertEquals(TEST_PASSWORD, result.getPassword());
        } catch (SQLException e) {
            e.printStackTrace();
            fail("Catched SQL Exception: " + e.getMessage());
        }
    }

    @Test
    public void update_note_content() {
        try {
            Note.setIdGenerator(DatabaseController.getNextNoteValidID());
            Note n = new Note("Modify this note!", TEST_USERNAME);
            DatabaseController.insertNote(n);

            n.setNote("Note modified!");

            DatabaseController.updateNote(n);
        } catch (SQLException e) {
            e.printStackTrace();
            fail("Catched SQL Exception: " + e.getMessage());
        }
    }
}