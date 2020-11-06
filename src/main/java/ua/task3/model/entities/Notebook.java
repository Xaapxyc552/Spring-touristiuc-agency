package ua.task3.model.entities;

import java.util.ArrayList;
import java.util.List;

public class Notebook {

    private List<Note> noteList =null;

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
        for (Note note:noteList) {
            sb.append(note.toString() + "\n");
        }
        return sb.toString();
    }
}
