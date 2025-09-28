package controller;

import javafx.scene.control.ListCell;
import model.Note;

public class NoteListCell extends ListCell<Note> {
    @Override
    protected void updateItem(Note note, boolean empty) {
        super.updateItem(note, empty);

        if (empty || note == null) {
            setText(null);
        } else {
            // Display title and preview of content
            String preview = note.getContent().length() > 50
                ? note.getContent().substring(0, 50) + "..."
                : note.getContent();
            setText(note.getTitle() + "\n" + preview);
        }
    }
}

