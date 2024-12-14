package client.scenes;

import client.MyModule;
import client.utils.ServerUtils;
import com.google.inject.Injector;
import commons.Note;
import jakarta.inject.Inject;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.web.WebView;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;


import java.net.URL;
import java.util.*;

import static com.google.inject.Guice.createInjector;

public class HomePageCtrl implements Initializable {
    private PrimaryCtrl pc;

    @FXML
    private TextArea notesBodyArea;
    @FXML
    private WebView webView;
    @FXML
    private TextField titleField;
    @FXML
    private Button editButton;


    private Parser parser;
    private HtmlRenderer renderer;

    private static int keyCount = 0;
    private Timer timer = new Timer();
    private TimerTask saveTask = null;
    private String original;

    /**
     * Constructor for HomePageCtrl.
     *
     * @param pc the PrimaryCtrl instance to be injected
     */
    @Inject
    public HomePageCtrl(PrimaryCtrl pc) {
        this.pc = pc;
        this.parser = Parser.builder().build();
        this.renderer = HtmlRenderer.builder().build();
    }

    /**
     * Initializes the HomePageCtrl.
     *
     * @param url            the URL
     * @param resourceBundle the ResourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        titleField.setEditable(false);
        addListener();
        webView.getEngine().loadContent("");
        initializeEdit();
        original = notesBodyArea.getText();
    }

    /**
     * Adds a listener to the notesBodyArea.
     */
    public void addListener() {
        if (notesBodyArea != null) {
            notesBodyArea.textProperty()
                    .addListener((observable, oldValue, markdownText) -> {
                        updateWebView(markdownConverter(markdownText));
                    });
        } else {
            throw new IllegalStateException("TextArea is not initialized!");
        }
    }

    /**
     * Converts Markdown text to HTML and updates the WebView.
     *
     * @param markdownText the Markdown text to be converted
     * @return the HTML text
     */
    public String markdownConverter(String markdownText) {
        Node text = parser.parse(markdownText);
        return renderer.render(text);
    }

    /**
     * Updates the WebView with the given HTML text.
     *
     * @param htmlText the HTML text to be displayed
     */
    public void updateWebView(String htmlText) {
        webView.getEngine().loadContent(htmlText);
    }

    /**
     * When the add note button is pressed this sends a command to the server to create a note.
     *
     * @return the note that was created
     */
    public Note createNote() {
        Note note = new Note("", "");
        Injector injector = createInjector(new MyModule());
        return injector.getInstance(ServerUtils.class).sendNote(note);
    }

    /**
     * When the remove note button is pressed this sends a command to the server to delete the current note.
     */
    public void deleteNote() {
        //TODO get current note
        Note note = new Note("", "");
        Injector injector = createInjector(new MyModule());
        String status = injector.getInstance(ServerUtils.class).deleteNote(note);
    }

    /**
     * When a key is pressed this calls getChanges to get the changes to the text and then calls the saving function of the text.
     */
    public void addKeyPressed() {
        keyCount++;
        long noteId = 0; //TODO get current note id
        if (saveTask != null) {
            saveTask.cancel();
        }
        if (keyCount >= 10) {
            keyCount = 0;
            String edited = notesBodyArea.getText();
            Map<String, Object> changes = getChanges(original, edited);
            original = edited;
            Injector injector = createInjector(new MyModule());
            String status = injector.getInstance(ServerUtils.class).saveChanges(noteId, changes);
        }
        else {
            saveTask = new TimerTask() {
                @Override
                public void run() {
                    keyCount = 0;
                    String edited = notesBodyArea.getText();
                    Map<String, Object> changes = getChanges(original, edited);
                    original = edited;
                    Injector injector = createInjector(new MyModule());
                    String status = injector.getInstance(ServerUtils.class).saveChanges(noteId, changes);
                }
            };
            timer.schedule(saveTask, 5000);
        }
    }

    /**
     * Compares two strings and finds the difference and where it is
     * @param original the not edited string
     * @param edited the edited string
     * @return a map of what needs to be changed and where
     */
    public Map<String, Object> getChanges(String original, String edited) {
        int startIndex = 0;

        while (startIndex < original.length() && startIndex < edited.length() && original.charAt(startIndex) == edited.charAt(startIndex)) {
            startIndex++;
        }
        int endIndexOriginal = original.length() - 1;
        int endIndexEdited = edited.length() - 1;

        while (endIndexOriginal >= startIndex && endIndexEdited >= startIndex && original.charAt(endIndexOriginal) == edited.charAt(endIndexEdited)) {
            endIndexOriginal--;
            endIndexEdited--;
        }
        String newText = edited.substring(startIndex, endIndexEdited + 1);

        Map<String, Object> changes = new HashMap<>();
        changes.put("operation", "Replace");
        changes.put("startIndex", startIndex);
        changes.put("endIndex", endIndexOriginal + 1);
        changes.put("newText", newText);
        return changes;
    }

    /**
     * Initializes the edit button.
     */
    public void initializeEdit() {
        editButton.setText("Edit");

        editButton.setOnAction(actionEvent -> {
            if (editButton.getText().equals("Edit")) {  //makes sure the button displays edit
                titleField.setEditable(true); //Makes sure you can edit
                titleField.requestFocus(); //Focuses on text field
                titleField.selectAll(); //Selects everything in the text field
                editButton.setText("Save");
            }
            //Saving function
            else if (editButton.getText().equals("Save")) {
                long noteId = 0; //TODO: replace with getting the current note id
                pc.editTitle(titleField.getText());
                titleField.setEditable(false); // Disable editing after saving
                Injector injector = createInjector(new MyModule());
                injector.getInstance(ServerUtils.class)
                        .updateNoteTitle(noteId, titleField.getText());
                editButton.setText("Edit");
            }
        });
    }
}
