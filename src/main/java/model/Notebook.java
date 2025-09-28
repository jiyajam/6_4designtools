package model;

import java.util.ArrayList;
import java.util.List;

public class Notebook {
    private List<Note> notes;

    public Notebook() {
        this.notes = new ArrayList<>();
    }

    public void addNote(Note note) {
        notes.add(note);
    }

    public void removeNote(Note note) {
        notes.remove(note);
    }

    public List<Note> getNotes() {
        return notes;
    }

    public void updateNote(Note oldNote, String newTitle, String newContent) {
        oldNote.setTitle(newTitle);
        oldNote.setContent(newContent);
    }
}
