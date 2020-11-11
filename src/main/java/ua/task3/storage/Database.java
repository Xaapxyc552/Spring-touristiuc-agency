package ua.task3.storage;

import ua.task3.model.entities.Note;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class Database {
    private static final Database INSTANCE = new Database();
    private final Set<Note> noteSet = new HashSet<>(20);

    private Database() {
        this.noteSet.add(new Note("John","Sassad","380675814289"));
        this.noteSet.add(new Note("Василий","Kozak33","380385498246"));
        this.noteSet.add(new Note("Andrew","Andrew123","380628563485"));
    }

    public static Database getInstance() {
        return INSTANCE;
    }

    public boolean addNoteToDb(Note note) throws SQLException {
        if (noteSet.stream().anyMatch(
                elem -> elem.getNickName().equals(note.getNickName()))) {
            throw new SQLException("Such nickname already exists in DB.");
        }
        return noteSet.add(note);
    }

    public Set<Note> getNoteSet() {
        return noteSet;
    }
}
