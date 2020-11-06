package ua.task3.model;

import ua.task3.model.entities.Note;
import ua.task3.model.entities.Notebook;

public class Model {

    private final Notebook notebook;

    public Model() {
         notebook = new Notebook(20);
    }

    public void addNewNoteInNotebook(String firstName,
                                     String nickName,
                                     String phoneNumber) {
        Note note = new Note(firstName,nickName,phoneNumber);
        notebook.addNoteToList(note);

    }

    public String notebookToString() {
        return notebook.toString();
    }


}
