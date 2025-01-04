package client.scenes;

import client.ClientConfig;
import client.LanguageController;
import client.MyModule;
import client.utils.ServerUtils;
import com.google.inject.Injector;
import commons.Collection;
import commons.Note;
import jakarta.inject.Inject;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.web.WebView;
import javafx.scene.image.Image;
import org.commonmark.Extension;
import org.commonmark.ext.gfm.tables.TablesExtension;
import org.commonmark.ext.autolink.AutolinkExtension;
import org.commonmark.Extension;
import org.commonmark.ext.autolink.AutolinkExtension;
import org.commonmark.ext.gfm.tables.TablesExtension;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import static com.google.inject.Guice.createInjector;

public class HomePageCtrl implements Initializable {
    private final PrimaryCtrl pc;

    @FXML
    private TextArea notesBodyArea;
    @FXML
    private WebView webView;
    @FXML
    private TextField titleField;
    @FXML
    private Button editButton;
    @FXML
    private ListView<Note> notesListView;
    @FXML
    private TextField searchBox;
    @FXML
    private ComboBox<HBox> languageComboBox;

    @FXML
    private Image englishFlag;
    @FXML
    private Image dutchFlag;
    @FXML
    private Image spanishFlag;

    private final String[] languages = {"en", "nl", "es"};

    private final LanguageController lc;

    private boolean isEditText;
    private final String path = "flags/";
    private final String defaultLanguage;
    private boolean isLoadingLanguage = false;

    //Collection
    private Collection currentCollection;

    private final Parser parser;
    private final HtmlRenderer renderer;
    private final SimpleObjectProperty<Note> currentNote = new SimpleObjectProperty<>();
    //With the variable below, we store the FULL list of notes and never change it
    private List<Note> notes = new ArrayList<>();
    //The list of titles of notes that are filtered after usage of searchbar
    private List<String> filteredTitles = new ArrayList<>();
    private List<Note> filteredNotes = new ArrayList<>();

    private static int keyCount = 0;
    private Timer timer = null;
    private TimerTask saveTask = null;
    private String original;
    private Injector injector;
    private ServerUtils serverUtils;
    private final ClientConfig config;
    /**
     * Constructor for HomePageCtrl.
     * @param pc the PrimaryCtrl instance to be injected
     * @param serverUtils the ServerUtils instance to be injected
     */
    @Inject
    public HomePageCtrl(PrimaryCtrl pc, ServerUtils serverUtils) {
        this.pc = pc;
        this.config = ClientConfig.loadConfig();
        List<Extension> extensions = List.of(TablesExtension.create(), AutolinkExtension.create());
        this.parser = Parser.builder().extensions(extensions).build();
        this.renderer = HtmlRenderer.builder().extensions(extensions).build();
        this.lc = new LanguageController();
        injector = createInjector(new MyModule());
        // Load preferred language and server URL from config
        defaultLanguage = config.getPreferredLanguage();
        // Use the loaded configuration
        this.serverUtils = serverUtils;
    }

    /**
     * Initializes the HomePageCtrl.
     *
     * @param url            the URL
     * @param resourceBundle the ResourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        lc.loadLanguage(defaultLanguage);

        titleField.setEditable(false);
        addListener();
        webView.getEngine().setUserStyleSheetLocation(getClass()
                .getResource("/configuration/WebViewConfig.css").toString());
        initializeEdit();
        original = notesBodyArea.getText();
        refreshNotes();

        englishFlag = new Image(path + "uk_flag.png");
        dutchFlag = new Image(path + "nl_flag.png");
        spanishFlag = new Image(path + "es_flag.png");

        loadAllFlags(Arrays.asList(languages).indexOf(defaultLanguage));
        languageComboBox.setOnAction(this::loadLanguage);

        initializeFilteringOfNotes();
        setupNotesListView();
    }


    /**
     * Loads the CSS file from the given path.
     * @param path The path of the css file
     * @return The contents of the css file
     */
    private String loadCssFile(String path) {
        try {
            return new String(Files.readAllBytes(Paths.get(path)));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Loads the chosen language from the ComboBox.
     * @param event the event that triggers the language change
     */
    private void loadLanguage(ActionEvent event) {
        if (isLoadingLanguage)
            return;

        isLoadingLanguage = true;

        int i = getCurrentLanguage();

        String language = languages[i];
        lc.loadLanguage(language);
        editButton.setText(isEditText ? lc.getEditText() : lc.getSaveText());

        loadAllFlags(i);
        config.setPreferredLanguage(language);

        isLoadingLanguage = false;
    }

    /**
     * With this method we manage to filter the notes that match the search with their title
     *
     * @param title The title of the note
     * @return returns the Note that matches the title, but if it doesn't find anything then null
     */
    //TODO: Need to find a way to implement the method filters the content as well.
    private Note searchNotesByTitle(String title) {
        for (Note note : notes) {
            if (note.getTitle().equals(title)) {
                return note;
            }
        }
        return null;
    }

    /**
     * Initializes the filtering of notes.
     */
    public void initializeFilteringOfNotes(){
        //TODO: After making the first TODO, we include "the name" to be disableProperty...

        //this ensures that the only way to access the searchbar is by clicking on it,
        // rather than using the arrows of the keyboard
        searchBox.setFocusTraversable(false);

        //With the addition of a listener, we can get accurate real-time input to the search bar
        searchBox.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null || newValue.trim().isEmpty()) {
                resetFilteredList();
            } else {
                filterNotes(newValue, notes);
            }
        });

        titleField.setOnKeyTyped(event -> {
            String input = titleField.getText(); // Get the current input from the TextField
            if (currentNote.get() != null) {
                currentNote.get().setTitle(input);
            }
        });
        notesBodyArea.setOnKeyTyped(event -> {
            String input = notesBodyArea.getText(); // Get the current input from the TextField
            if (currentNote.get() != null) {
                currentNote.get().setContent(input);
            }
        });

    }

    /**
     * When the search box is empty we reset the filtered list to the list of the full notes
     */
    private void resetFilteredList() {
        filteredTitles.clear();
        filteredNotes.clear();
        for (Note note : currentCollection != null ? currentCollection.getNotes() : notes) {
            filteredTitles.add(note.getTitle());
            filteredNotes.add(note);
        }
    }

    /**
     * This method filters the notes by accessing the title and content of it
     *
     * @param searchBoxQuery
     * @param noteList
     * @return return the notes that match the searchBoxQuery
     */
    public List<Note> filterNotes(String searchBoxQuery, List<Note> noteList) {
        List<Note> returnNotes = new ArrayList<>();

        String fixedSearchQuery = searchBoxQuery.toLowerCase().trim();

        for (Note note : noteList) {
            if (note.getTitle().toLowerCase().contains(fixedSearchQuery) ||
                    note.getContent().toLowerCase().contains(fixedSearchQuery)) {
                returnNotes.add(note);
            }
        }
        return returnNotes;
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
        try{
            Node text = parser.parse(markdownText);
            return renderer.render(text);
        } catch (Exception e){
            return "<html><body><h2>Error</h2><p>" + e.getMessage() + "</p></body></html>";
        }
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
        int counter = 1;
        String uniqueTitle = "New Note Title " + counter;
        while(serverUtils.isTitleDuplicate(uniqueTitle)){
            uniqueTitle = "New Note Title " + counter;
            counter++;
        }
        Note note = new Note(uniqueTitle, "New Note Content");
        Note createdNote = serverUtils.sendNote(note);

        if (createdNote != null) {
            notesListView.getItems().add(createdNote);
            System.out.println("Note created with ID: " + createdNote.getId());
            return createdNote;
        } else {
            System.err.println("Failed to create note.");
            return null;
        }

    }

    /**
     * Sets up the notes ListView.
     */
    private void setupNotesListView() {
        // Custom cell factory to show only titles
        notesListView.setCellFactory(listView -> new ListCell<Note>() {
            @Override
            protected void updateItem(Note note, boolean empty) {
                super.updateItem(note, empty);
                if (empty || note == null) {
                    setText(null);
                } else {
                    setText(note.getTitle());
                }
            }
        });

        notesListView.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldNote, newNote) -> {
                    if (newNote != null) {
                        titleField.setText(newNote.getTitle());
                        titleField.setEditable(false);
                        notesBodyArea.setText(newNote.getContent());
                    } else {
                        titleField.clear();
                        notesBodyArea.clear();
                    }
                });
    }
    /**
     * When the remove note button is pressed,
     * this sends a command to the server to delete the current note.
     */
    public void deleteNote() {
        Note selectedNote = notesListView.getSelectionModel()
                .getSelectedItem(); // Fetch selected note
        if (selectedNote != null) {
            String status = serverUtils.deleteNote(selectedNote);

            if ("Successful".equals(status)) {
                refreshNotes(); // Refresh the ListView
            } else {
                System.err.println("Failed to delete the note.");
            }
        } else {
            System.out.println("No note selected for deletion.");
        }
    }

    /**
     * When a key is pressed this calls getChanges to get the changes to the text
     * and then calls the saving function of the text.
     */
    public void addKeyPressed() {
        keyCount++;
        Note selectedNote = notesListView.getSelectionModel().getSelectedItem();
        long noteId = selectedNote.getId();
        if (saveTask != null) {
            saveTask.cancel();
        }
        if (keyCount >= 10) {
            keyCount = 0;
            String edited = notesBodyArea.getText();
            Map<String, Object> changes = getChanges(original, edited);
            original = edited;
            String status = serverUtils.saveChanges(noteId, changes);
        }
        else {
            timer = new Timer(true);
            saveTask = new TimerTask() {
                @Override
                public void run() {
                    keyCount = 0;
                    String edited = notesBodyArea.getText();
                    Map<String, Object> changes = getChanges(original, edited);
                    original = edited;
                    String status = serverUtils
                            .saveChanges(noteId, changes);
                }
            };
            timer.schedule(saveTask, 5000);
        }
    }

    /**
     * Compares two strings and finds the difference and where it is
     *
     * @param original the not edited string
     * @param edited   the edited string
     * @return a map of what needs to be changed and where
     */
    public Map<String, Object> getChanges(String original, String edited) {
        int startIndex = 0;

        while (startIndex < original.length() && startIndex < edited.length()
                && original.charAt(startIndex) == edited.charAt(startIndex)) {
            startIndex++;
        }
        int endIndexOriginal = original.length() - 1;
        int endIndexEdited = edited.length() - 1;

        while (endIndexOriginal >= startIndex && endIndexEdited >= startIndex
                && original.charAt(endIndexOriginal) == edited.charAt(endIndexEdited)) {
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
        isEditText = true;
        editButton.setText(lc.getEditText());

        editButton.setOnAction(actionEvent -> {
            if (editButton.getText().equals(
                    lc.getEditText())) {  //makes sure the button displays edit

                titleField.setEditable(true); //Makes sure you can edit
                titleField.requestFocus(); //Focuses on text field
                titleField.selectAll(); //Selects everything in the text field

                editButton.setText(lc.getSaveText());
                isEditText = false;
            }
            //Saving function
            else if (editButton.getText()
                    .equals(lc.getSaveText())) {
                Note selectedNote = notesListView.getSelectionModel().getSelectedItem();
                if(selectedNote != null){
                    if(serverUtils.updateNoteTitle(selectedNote.getId()
                            , titleField.getText()).equals(titleField.getText())){
                        selectedNote.setTitle(titleField.getText());

                        int selectedIndex = notesListView.getSelectionModel().getSelectedIndex();
                        notesListView.getItems().set(selectedIndex, selectedNote);
                        notesListView.refresh();
                    }
                    else {
                        System.err.println("Title is a duplicate, not valid");
                    }
                }
                titleField.setEditable(false);

                editButton.setText(lc.getEditText());
                isEditText = true;
            }
        });
    }
    /**
     * Refreshes the notes in the ListView.
     */
    public void refreshNotes() {
        List<Note> notes = serverUtils.getNotes();

        if (notesListView != null) {
            notesListView.getItems().clear();
            notesListView.getItems().addAll(notes);
        } else {
            System.err.println("ListView not initialized!");
        }
    }

    /**
     * @return the selected language in the dropdown
     */
    private int getCurrentLanguage() {
        for (int i = 0; i < 3; i++) {
            if (languageComboBox.getSelectionModel().isSelected(i)) {
                return i;
            }
        }
        return 0;
    }

    /**
     * loads all the flags in the ComboBox
     */
    private void loadAllFlags(int index) {
        languageComboBox.getItems().clear();
        languageComboBox.getItems().addAll(
                createFlagItem(englishFlag),
                createFlagItem(dutchFlag),
                createFlagItem(spanishFlag)
        );
        languageComboBox.getSelectionModel().select(index);
    }

    /**
     * Creates a flag item from a given image used in the ComboBox
     *
     * @param image the Image to convert
     * @return the created HBox item containing the ImageView
     */
    private HBox createFlagItem(Image image) {
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(50);
        imageView.setFitHeight(25);

        return new HBox(10, imageView);
    }
}
