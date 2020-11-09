package ua.task3.model.entities;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that represents a list of {@link Note},
 * to easy store and represent data, contained by notes.
 *
 * @see Note
 */
public class Notebook {

    private List<Note> noteList = null;


    /**
     * Creates instance of class with list
     * with specified by {@param capacity} capacity.
     *
     * @param capacity
     */
    public Notebook(int capacity) {
        this.noteList = new ArrayList<>(capacity);
    }

    public void addNoteToList(Note note) {
        noteList.add(note);
    }

    public List<Note> getNoteList() {
        return noteList;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Notes: [\n");
        for (Note note : noteList) {
            sb.append(note.toString() + "\n");
        }
        return sb.toString();
    }
}
