package app.entities;

import java.util.Date;

public class Note {

    private static int idGenerator;

    private final int id;
    private String note;
    private final String username;
    private final Date writeDate;

    // App constructor
    public Note(String note, String username) {
        this.id = idGenerator ++;
        this.note = note;
        this.username = username;
        this.writeDate = new Date();
    }

    // Database constructor
    public Note(int id, String note, String username, Date writeDate) {
        this.id = id;
        this.note = note;
        this.username = username;
        this.writeDate = writeDate;
    }

    public static void setIdGenerator(int idGenerator) {
        Note.idGenerator = idGenerator;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getUsername() {
        return username;
    }

    public Date getWriteDate() {
        return writeDate;
    }

    public int getId() {
        return id;
    }
}
