package client.scenes;

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
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.web.WebView;
import javafx.scene.image.Image;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

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
    private TextField searchBox;
    @FXML
    private TextField titleField;
    @FXML
    private TextArea notesBodyArea;
    @FXML
    private Button editButton;

    @FXML
    private ComboBox<HBox> languageComboBox;

    @FXML
    private Image englishFlag;
    @FXML
    private Image dutchFlag;
    @FXML
    private Image spanishFlag;


    private Image[] flags = new Image[3];
    private String[] languages = {"en", "nl", "es"};

    private LanguageController lc;

    private boolean isEditText;
    private String path = "flags/";
    private String defaultLanguage = languages[0];
    
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

        this.lc = new LanguageController();
    }

    /**
     * Initializes the HomePageCtrl.
     *
     * @param url            the URL
     * @param resourceBundle the ResourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        lc.loadLanguage("en");

        titleField.setEditable(false);
        addListener();
        webView.getEngine().loadContent("");
        initializeEdit();

        englishFlag = new Image(path + "uk_flag.png");
        dutchFlag = new Image(path + "nl_flag.png");
        spanishFlag = new Image(path + "es_flag.png");

        flags = new Image[] {englishFlag, dutchFlag, spanishFlag};

        loadAllFlags();
        languageComboBox.setOnAction(this::loadLanguage);
        initializeFilteringOfNotes();
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
            if(currentNote.get() != null) {
                currentNote.get().setTitle(input);
            }
        });
        notesBodyArea.setOnKeyTyped(event -> {
            String input = notesBodyArea.getText(); // Get the current input from the TextField
            if(currentNote.get() != null) {
                currentNote.get().setContent(input);
            }
        });

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

    /**
     * This method filters the notes by accessing the title and content of it
     * @param searchBoxQuery
     * @param noteList
     * @return return the notes that match the searchBoxQuery
     */
    public List<Note> filterNotes(String searchBoxQuery, List<Note> noteList){
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
     * When the remove note button is pressed,
     * this sends a command to the server to delete the current note.
     */
    public void deleteNote() {
        //TODO get current note
        Note note = new Note("", "");
        Injector injector = createInjector(new MyModule());
        String status = injector.getInstance(ServerUtils.class).deleteNote(note);
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
                pc.editTitle(titleField.getText());

                titleField.setEditable(false); // Disable editing after saving
                titleField.setEditable(false);

                editButton.setText(lc.getEditText());
                isEditText = true;
            }
        });
    }

    /**
     * loads a certain language on changing the value in the ComboBox
     * @param actionEvent additional event data
     */
    private void loadLanguage(ActionEvent actionEvent) {
        HBox flag = languageComboBox.getValue();
        String language = hBox2Language();

        lc.loadLanguage(language);

        if(isEditText)
            editButton.setText(lc.getEditText());
        else
            editButton.setText(lc.getSaveText());
    }

    /**
     * Converts a given HBox from the ComboBox to the selected language
     * @return the language String code
     */
    private String hBox2Language(){
        HBox selectedItem = languageComboBox.getSelectionModel().getSelectedItem();

        Image flag = null;

        ImageView imageView = (ImageView) selectedItem.getChildren().getFirst();
        flag = imageView.getImage();

        int k = 0;

        // This does work and fixes the flags disappearing
        // but prints an error in the console, does not stop the program

        //languageComboBox.getItems().clear();
        //loadAllFlags();

        for(Image i : flags){
            if(i.getUrl().equals(flag.getUrl())){
                return languages[k];
            }
            k++;
        }
        return defaultLanguage;
    }

    /**
     * loads all the flags in the ComboBox
     */
    private void loadAllFlags(){

        languageComboBox.getItems().addAll(
                createFlagItem(englishFlag),
                createFlagItem(dutchFlag),
                createFlagItem(spanishFlag)
        );
    }

    /**
     * Creates a flag item from a given image used in the ComboBox
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
