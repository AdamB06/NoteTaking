package client.scenes;

import client.MyModule;
import client.utils.ServerUtils;
import com.google.inject.Injector;
import commons.Collection;
import commons.Note;
import jakarta.inject.Inject;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.web.WebView;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;


import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static com.google.inject.Guice.createInjector;

public class HomePageCtrl implements Initializable {
    private final PrimaryCtrl pc;

    @FXML
    private WebView webView;
    @FXML
    private ListView<String> noteListView;
    //TODO: not sure whether I need "TextField titleOfNote" yet for the initialize method or should I stick with titleField
    @FXML
    private TextField searchBox;
    @FXML
    private TextField titleField;
    @FXML
    private TextArea notesBodyArea;
    @FXML
    private Button editButton;

    //Collection
    private final List<Collection> collections = new ArrayList<>();
    private Collection currentCollection;

    //CollectionFXML
    //TODO: When we have added the drop down menu
    // to include a variable SplitMenuButton with a name "collectionMEnuButton"

    private final Parser parser;
    private final HtmlRenderer renderer;
    private final SimpleObjectProperty<Note> currentNote = new SimpleObjectProperty<>();
    //With the variable below, we store the FULL list of notes and never change it
    private List<Note> notes;
    //The list of titles of notes that are filtered after usage of searchbar
    private List<String> filteredTitles;
    private List<Note> filteredNotes = new ArrayList<>();

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

        //To disable the property of titleOfNote whose contents are null
        titleField.disableProperty().bind(Bindings.createBooleanBinding(
                () -> currentNote.get() == null, currentNote
                ));

        //To disable the property of titlePfNote whose notes are null
        notesBodyArea.disableProperty().bind(Bindings.createBooleanBinding(
                () -> currentNote.get() == null, currentNote
        ));
        //TODO: After making the first TODO, we include "the name" to be disableProperty...

        //this ensures that the only way to access the searchbar is by clicking on it,
        // rather than using the arrows of the keyboard
        searchBox.setFocusTraversable(false);

        //Adding a listener to handle live selection changes on the listView,
        //which presents all the notes that match the search based on title or content
        noteListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) ->{
            if(newValue != null){
                //Find the notes that match the search based on title
                currentNote.set(searchNotesByTitle(newValue));

                if(currentNote.get() != null){
                    updatingPanel(false);
                }
            }
        });

        //With the addition of a listener, we can get accurate real-time input to the search bar
        searchBox.textProperty().addListener((observable, oldValue, newValue) -> {
            //When the search box is cleared, it is logical for us to get all the notes,
            //as that means that there are no requirements
            if (newValue == null || newValue.trim().isEmpty()) {
                resetFilteredList();
            } else {
                //When the search bar is not empty (it has a string inside) we will perform an immediate
                //filtering to find out what notes match the string, by looking at their title and content
                filterNotes(newValue, notes);
            }

            // Update the ListView with the filtered titles
            refreshNotes();
        });

        titleField.setOnKeyTyped(event -> {
            String input = titleField.getText(); // Get the current input from the TextField
            if(currentNote.get() != null) {
                currentNote.get().setTitle(input);
                updateNoteList();
            }
        });
        notesBodyArea.setOnKeyTyped(event -> {
            String input = notesBodyArea.getText(); // Get the current input from the TextField
            if(currentNote.get() != null) {
                currentNote.get().setContent(input);
                //refreshPreviewText();
            }
        });

    }

    /**
     * With this method we manage to filter the notes that match the search with their title
     * @param title The title of the note
     * @return returns the Note that matches the title, but if it doesn't find anything then null
     */
    //TODO: Need to find a way to implement the method filters the content as well.
    private Note searchNotesByTitle(String title){
        for(Note note : notes){
            if(note.getTitle().equals(title)){
                return note;
            }
        }
        return null;
    }

    /**
     * When the search box is empty we reset the filtered list to the list of the full notes
     */
    private void resetFilteredList(){
        filteredTitles.clear();
        filteredNotes.clear();
        for(Note note : currentCollection != null ? currentCollection.getNotes() : notes) {
            filteredTitles.add(note.getTitle());
            filteredNotes.add(note);
        }
    }

    public void updatingPanel(boolean toSetNoteToNull){
        if(toSetNoteToNull){
            currentNote.set(null);
            titleField.setText(null);
            notesBodyArea.setText(null);
            refreshNotes();
            return;
        }
        titleField.setText(currentNote.get().getTitle());
        notesBodyArea.setText(currentNote.get().getContent());
    }

    private void updateNoteList() {
        //First, we clear all the notes in the list
        noteListView.getItems().clear();

        //Filling the list with the titles that are left after filtering
        filteredTitles = new ArrayList<>();
        for (Note note : currentCollection != null ? currentCollection.getNotes() : filteredNotes) {
            filteredTitles.add(note.getTitle());
        }

        //As a last step we put all the filtered titles of notes into the note list view
        noteListView.getItems().addAll(filteredTitles);
    }

    public static List<Note> filterNotes(String searchBoxQuery, List<Note> noteList){
        List<Note> returnNotes = new ArrayList<>();

        String fixedSearchQuery = searchBoxQuery.toLowerCase().trim();

        for(Note note : noteList) {
            if(note.getTitle().toLowerCase().contains(fixedSearchQuery) ||
            note.getContent().toLowerCase().contains(fixedSearchQuery)){
                returnNotes.add(note);
            }
        }
        return returnNotes;
    }

    public void refreshNotes(){
        updateNoteList();
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
                pc.editTitle(titleField.getText());
                titleField.setEditable(false); // Disable editing after saving
                titleField.setEditable(false);
                editButton.setText("Edit");
            }
        });
    }


}
