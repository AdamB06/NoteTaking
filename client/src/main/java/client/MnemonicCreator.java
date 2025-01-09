package client;

import commons.Note;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyCombination.Modifier;
import javafx.scene.input.Mnemonic;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.util.Optional;

public class MnemonicCreator {

    private final Modifier controlKey = KeyCombination.ALT_ANY;

    private Scene scene;

    private int currentNoteIndex = 0;
    private ListView<Note> currentNotesView;

    /**
     * Initializes all given buttons and assigns them a mnemonic
     *
     * @param editSave     edit and save button
     * @param add          add button
     * @param delete       delete button
     * @param refresh      refresh button
     * @param searchBar    search bar for titles and notes
     * @param noteListView list view for notes
     */
    public void initialize(Button editSave, Button add,
                           Button delete, Button refresh,
                           TextField searchBar, ListView<Note> noteListView) {
        scene = getCurrentScene();

        Mnemonic editMN = create(editSave, KeyCode.E);
        Mnemonic addMN = create(add, KeyCode.A);
        Mnemonic deleteMN = create(delete, KeyCode.D);
        Mnemonic refreshMN = create(refresh, KeyCode.R);

        scene.addMnemonic(editMN);
        scene.addMnemonic(addMN);
        scene.addMnemonic(deleteMN);
        scene.addMnemonic(refreshMN);

        updateNotesView(noteListView);
        setKeyEvents(searchBar);
    }

    public void updateIndex(int index){
        currentNoteIndex = index;
    }

    /**
     * updates the note list view
     *
     * @param noteListView the new notes list view
     */
    private void updateNotesView(ListView<Note> noteListView) {
        currentNotesView = noteListView;

        int size = currentNotesView.getItems().size();

        // make sure it is never bigger than the actual size of the note collection size
        // i.e. removing notes will decrease the size and set the index out of bounds
        if (size == 0)
            currentNoteIndex = 0;
        else
            currentNoteIndex = currentNoteIndex % size;
    }

    /**
     * adds all key events
     *
     * @param searchBar the search bar
     */
    private void setKeyEvents(TextField searchBar) {
        scene.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode().equals(KeyCode.ESCAPE))
                searchBar.requestFocus();

            // cycling through available notes
            if (keyEvent.getCode().equals(KeyCode.DIGIT1)) {
                cycleNotes(-1);
            }
            if (keyEvent.getCode().equals(KeyCode.DIGIT2)) {
                cycleNotes(1);
            }
            if (keyEvent.getCode().equals(KeyCode.R)) {
                currentNotesView.getItems().remove(2);
            }
        });
    }

    /**
     * cycles through the notes
     * @param direction the direction
     */
    private void cycleNotes(int direction) {
        int size = currentNotesView.getItems().size();
        int next = currentNoteIndex + direction;

        if (size == 0) {
            currentNoteIndex = 0;
            return;
        }

        if (next < 0)
            next = size - 1;
        else if (next >= size)
            next = 0;

        currentNoteIndex = next;
        currentNotesView.getSelectionModel().select(currentNoteIndex);
    }

    /**
     * Creates a specific mnemonic
     *
     * @param n       the Node to create the mnemonic for
     * @param primary the primary key for the button name (secondary is the alt key by default)
     * @return the created mnemonic
     */
    private Mnemonic create(Node n, KeyCode primary){
        KeyCombination kc = new KeyCodeCombination(primary, controlKey);
        return new Mnemonic(n, kc);
    }

    /**
     * Get the current scene
     *
     * @return the current scene
     */
    private Scene getCurrentScene() {
        Optional<Window> optionalWindow = Stage.getWindows().stream()
                .filter(Window::isShowing)
                .findFirst();

        Window open;
        open = optionalWindow.orElse(null);

        if (open == null) {
            System.out.println("window does not exist");
            return null;
        }

        return open.getScene();
    }
}
