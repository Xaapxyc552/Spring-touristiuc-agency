package ua.task3.model;

import ua.task3.model.entities.Note;
import ua.task3.storage.Database;

import java.sql.SQLException;

public class Model {

    private Database database = Database.getInstance();

    public Model() {
    }

    public void addNewNoteInDb(String firstName,
                               String nickName,
                               String phoneNumber) throws NotUniqueLoginException {
        Note note = new Note(firstName, nickName, phoneNumber);
        try {
            database.addNoteToDb(note);
        } catch (SQLException e) {
            throw new NotUniqueLoginException(e);
        }

    }

    public String notebookToString() {
        return database.getNoteSet().toString();
    }


}
