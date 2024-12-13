package client.scenes;

import client.LanguageController;
import client.MyModule;
import client.utils.ServerUtils;
import com.google.inject.Injector;
import commons.Note;
import jakarta.inject.Inject;
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
import java.util.ResourceBundle;

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


    private Parser parser;
    private HtmlRenderer renderer;
    private LanguageController lc;

    private boolean isEditText;
    private String path = "flags/";
    private String defaultLanguage = languages[0];

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
            else if (editButton.getText()
                    .equals(lc.getSaveText())) {
                long noteId = 0; //TODO: replace with getting the current note id
                pc.editTitle(titleField.getText());

                titleField.setEditable(false); // Disable editing after saving
                Injector injector = createInjector(new MyModule());
                injector.getInstance(ServerUtils.class)
                        .updateNoteTitle(noteId, titleField.getText());
                editButton.setText("Edit");
                titleField.setEditable(false);

                editButton.setText(lc.getEditText());
                isEditText = true;
            }
        });
    }

    private void loadLanguage(ActionEvent actionEvent) {
        HBox flag = languageComboBox.getValue();
        String language = hBox2Language(flag);

        lc.loadLanguage(language);

        if(isEditText)
            editButton.setText(lc.getEditText());
        else
            editButton.setText(lc.getSaveText());
    }

    private String hBox2Language(HBox hBox){
        HBox selectedItem = languageComboBox.getSelectionModel().getSelectedItem();

        Image flag = null;

        ImageView imageView = (ImageView) selectedItem.getChildren().get(0);
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

    private void loadAllFlags(){

        languageComboBox.getItems().addAll(
                createFlagItem(englishFlag),
                createFlagItem(dutchFlag),
                createFlagItem(spanishFlag)
        );
    }

    private HBox createFlagItem(Image image) {
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(50);
        imageView.setFitHeight(25);

        return new HBox(10, imageView);
    }
}
