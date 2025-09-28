package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import model.Note;
import model.Notebook;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class NotebookController implements Initializable {

    @FXML private TextField titleField;
    @FXML private TextArea contentArea;
    @FXML private Button addButton;
    @FXML private Button updateButton;
    @FXML private Button cancelButton;
    @FXML private Button editButton;
    @FXML private Button deleteButton;
    @FXML private ListView<Note> notesListView;

    private Notebook notebook;
    private ObservableList<Note> observableNotes;
    private Note selectedNote = null;
    private boolean isEditing = false;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialize model and ObservableList
        notebook = new Notebook();
        observableNotes = FXCollections.observableArrayList();

        // Connect ListView to ObservableList
        notesListView.setItems(observableNotes);

        // Initially disable edit and delete buttons
        editButton.setDisable(true);
        deleteButton.setDisable(true);

        // Add selection listener to ListView
        notesListView.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldValue, newValue) -> {
                selectedNote = newValue;
                boolean hasSelection = newValue != null;
                editButton.setDisable(!hasSelection);
                deleteButton.setDisable(!hasSelection);
            }
        );
    }

    @FXML
    private void addNote() {
        String title = titleField.getText().trim();
        String content = contentArea.getText().trim();

        if (title.isEmpty() || content.isEmpty()) {
            showAlert("Validation Error", "Both title and content are required!");
            return;
        }

        Note note = new Note(title, content);
        notebook.addNote(note);
        observableNotes.add(note);

        clearFields();
        showAlert("Success", "Note added successfully!");
    }

    @FXML
    private void editNote() {
        if (selectedNote != null) {
            titleField.setText(selectedNote.getTitle());
            contentArea.setText(selectedNote.getContent());

            // Switch to edit mode
            isEditing = true;
            addButton.setVisible(false);
            updateButton.setVisible(true);
            cancelButton.setVisible(true);
        }
    }

    @FXML
    private void updateNote() {
        if (selectedNote != null) {
            String newTitle = titleField.getText().trim();
            String newContent = contentArea.getText().trim();

            if (newTitle.isEmpty() || newContent.isEmpty()) {
                showAlert("Validation Error", "Both title and content are required!");
                return;
            }

            // Update the note
            notebook.updateNote(selectedNote, newTitle, newContent);

            // Refresh ListView to show updated note
            notesListView.refresh();

            exitEditMode();
            showAlert("Success", "Note updated successfully!");
        }
    }

    @FXML
    private void cancelEdit() {
        exitEditMode();
        clearFields();
    }

    @FXML
    private void deleteNote() {
        if (selectedNote != null) {
            // Show confirmation dialog
            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setTitle("Delete Note");
            confirmationAlert.setHeaderText("Are you sure you want to delete this note?");
            confirmationAlert.setContentText("\"" + selectedNote.getTitle() + "\" will be permanently deleted.");

            Optional<ButtonType> result = confirmationAlert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                notebook.removeNote(selectedNote);
                observableNotes.remove(selectedNote);

                // If we're editing this note, exit edit mode
                if (isEditing) {
                    exitEditMode();
                    clearFields();
                }

                showAlert("Success", "Note deleted successfully!");
            }
        }
    }

    private void exitEditMode() {
        isEditing = false;
        addButton.setVisible(true);
        updateButton.setVisible(false);
        cancelButton.setVisible(false);
        notesListView.getSelectionModel().clearSelection();
    }

    private void clearFields() {
        titleField.clear();
        contentArea.clear();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
