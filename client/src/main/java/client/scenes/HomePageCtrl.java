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
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

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

    public void addKeyPressed() {
        keyCount++;
        if (saveTask != null) {
            saveTask.cancel();
        }
        if (keyCount >= 10) {
            keyCount = 0;
            System.out.println("keyCount >= 10");
            Injector injector = createInjector(new MyModule());
            String status = injector.getInstance(ServerUtils.class).saveChanges(notesBodyArea.getText());
        }
        else {
            saveTask = new TimerTask() {
                @Override
                public void run() {
                    keyCount = 0;
                    System.out.println("Timer");
                    Injector injector = createInjector(new MyModule());
                    String status = injector.getInstance(ServerUtils.class).saveChanges(notesBodyArea.getText());
                }
            };
            timer.schedule(saveTask, 5000);
        }
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
