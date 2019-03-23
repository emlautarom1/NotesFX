package app.controllers;

import app.entities.Note;
import app.entities.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class DatabaseController {
    private static final Logger LOGGER = Logger.getLogger(DatabaseController.class.getName());

    private static Connection connection = null;
    private static final String driver = "org.sqlite.JDBC";
    private static final String URL = "jdbc:sqlite:notes.db";
    private static final String USERS_TABLE_NAME = "USERS";
    private static final String NOTES_TABLE_NAME = "NOTES";

    public static Connection getConnection() throws SQLException {
        if (connection == null) {
            LOGGER.info("Database connection is null, creating connection...");
            try {
                Class.forName(driver);
                connection = DriverManager.getConnection(URL);
            } catch (ClassNotFoundException e) {
                throw new SQLException("Failed to connect to database");
            }

            if (!databaseHasTables()) {
                LOGGER.info("Database is empty, creating tables...");
                createTables();
            }
        }
        return connection;
    }

    private static boolean databaseHasTables() {
        var tableUsersExist = false;
        var tableNotesExist = false;
        try {
            var rs = getConnection().getMetaData().getTables(null, null, USERS_TABLE_NAME, null);
            while (rs.next()) {
                String tName = rs.getString("TABLE_NAME");
                if (tName != null && tName.equals(USERS_TABLE_NAME)) {
                    LOGGER.info("Table USERS exist");
                    tableUsersExist = true;
                    break;
                }
            }
            rs = getConnection().getMetaData().getTables(null, null, NOTES_TABLE_NAME, null);
            while (rs.next()) {
                var tName = rs.getString("TABLE_NAME");
                if (tName != null && tName.equals(NOTES_TABLE_NAME)) {
                    LOGGER.info("Table NOTES exist");
                    tableNotesExist = true;
                    break;
                }
            }
        } catch (Exception e) {
            LOGGER.warning("Failed to check if tables exist");
            e.printStackTrace();
        }
        return (tableNotesExist && tableUsersExist);
    }

    private static void createTables() throws SQLException {
        var usersTable = "CREATE TABLE IF NOT EXISTS USERS (\n" + " USERNAME VARCHAR(256) PRIMARY KEY,"
                + " NAME VARCHAR(256) NOT NULL," + " PASSWORD VARCHAR(256) NOT NULL)";
        var usersTableStatement = getConnection().createStatement();
        usersTableStatement.execute(usersTable);
        LOGGER.info("Created user table.");

        var notesTable = "CREATE TABLE IF NOT EXISTS NOTES (\n" + " ID INTEGER PRIMARY KEY AUTOINCREMENT,"
                + " NOTE TEXT," + " USERNAME VARCHAR(256) NOT NULL," + " WRITEDATE DATE NOT NULL,"
                + " FOREIGN KEY(USERNAME) REFERENCES USERS(USERNAME))";

        var notesTableStatement = getConnection().createStatement();
        notesTableStatement.execute(notesTable);
        LOGGER.info("Created notes table.");
    }

    public static void registerValidatedUser(User user) throws SQLException {
        // User should be already validated.
        PreparedStatement statement = getConnection().prepareStatement("INSERT INTO USERS VALUES(?,?,?)");
        statement.setString(1, user.getUsername());
        statement.setString(2, user.getName());
        statement.setString(3, user.getPassword());

        statement.execute();
    }

    public static void insertNote(Note note) throws SQLException {
        PreparedStatement statement = getConnection()
                .prepareStatement("INSERT INTO NOTES(NOTE, USERNAME, WRITEDATE) VALUES(?,?,?)");
        statement.setString(1, note.getNote());
        statement.setString(2, note.getUsername());
        // Convert java.util.date to java.sql.date
        var convertedDate = new Date(note.getWriteDate().getTime());
        statement.setDate(3, convertedDate);

        statement.execute();
    }

    public static void deleteNote(Note note) throws SQLException {
        PreparedStatement statement = getConnection().prepareStatement("DELETE FROM NOTES WHERE ID = ?");
        if (note.getId() != 0) {
            statement.setInt(1, note.getId());
            statement.execute();
        } else {
            LOGGER.severe("Note id is invalid (0), failed to insert into database.");
        }
    }

    public static void updateNote(Note note) throws SQLException {
        PreparedStatement statement = getConnection().prepareStatement("UPDATE NOTES SET NOTE = ? WHERE ID = ?");
        statement.setString(1, note.getNote());
        statement.setInt(2, note.getId());

        statement.execute();
    }

    public static int getNextNoteValidID() throws SQLException {
        PreparedStatement statement = getConnection()
                .prepareStatement("SELECT seq from sqlite_sequence WHERE name = 'NOTES'");
        ResultSet queryResult = statement.executeQuery();
        if (!queryResult.next()) {
            LOGGER.info("Current max key is not set up yet");
            return 1;
        } else {
            return queryResult.getInt("seq") + 1;
        }
    }

    public static List<Note> getNotesFromUser(String username) throws SQLException {
        PreparedStatement statement = getConnection().prepareStatement("SELECT * FROM NOTES WHERE USERNAME = ?");
        statement.setString(1, username);
        var foundNotes = statement.executeQuery();

        List<Note> noteList = new ArrayList<>();
        while (foundNotes.next()) {
            var resultID = foundNotes.getInt("ID");
            var resultNote = foundNotes.getString("NOTE");
            var resultUsername = foundNotes.getString("USERNAME");
            java.util.Date resultDate = foundNotes.getDate("WRITEDATE");
            noteList.add(new Note(resultID, resultNote, resultUsername, resultDate));
        }
        if (!noteList.isEmpty()) {
            return noteList;
        } else {
            throw new SQLException("No notes found for username: " + username);
        }

    }

    public static User getUser(String username) throws SQLException {
        PreparedStatement statement = getConnection().prepareStatement("SELECT * FROM USERS WHERE USERNAME = ?");
        statement.setString(1, username);
        var foundUsers = statement.executeQuery();
        // From the first, and only expected result, build a User instance.
        var resultUsername = foundUsers.getString("USERNAME");
        var resultName = foundUsers.getString("NAME");
        var resultPassword = foundUsers.getString("PASSWORD");
        LOGGER.info("Found user info:" + "\n" + "   Name: " + resultName + "\n" + "   Username: " + resultUsername
                + "\n" + "   Password: " + resultPassword);
        return new User(resultName, resultUsername, resultPassword);
    }

    public static User authenticateUser(String username, String password) throws SQLException, SecurityException {
        var foundUser = getUser(username);
        if (foundUser.getPassword().equals(password)) {
            LOGGER.info("Username " + username + " was authenticated");
            return foundUser;
        } else {
            throw new SecurityException("Invalid password.");
        }
    }
}
