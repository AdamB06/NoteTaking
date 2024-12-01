package client.scenes;

import jakarta.inject.Inject;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.web.WebView;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class HomePageCtrl implements Initializable {
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
    private PrimaryCtrl pc;

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
     * Makes sure you can edit the text in the textfield, will select
     * everything to make editing more convenient.
     */
    @FXML
    public void initializeEdit() {
        editButton.setText("Edit");

        editButton.setOnAction(actionEvent -> {
            if (editButton.getText().equals("Edit")) {  //makes sure the button displays edit
                titleField.setEditable(true); //Makes sure you can edit
                titleField.requestFocus(); //Focuses on text field
                titleField.selectAll(); //Selects everything in the text field
                editButton.setText("Save"); //changes button to save

                //Saving function
            } else if (editButton.getText().equals("Save")) {
                pc.editTitle(titleField.getText());
                titleField.setEditable(false);
                editButton.setText("Edit");
            }
        });
    }

    /**
     * Makes sure that you can set a value to Edit button
     * since its an private class
     * @param editButton button that dislays edit on GUI
     */
    public void setEditButton(Button editButton) {
        this.editButton = editButton;
    }

    /**
     * Makes sure you can set a value to the titlefield
     * @param titleField The textbox next to the edit button
     */
    public void setTitleField(TextField titleField) {
        this.titleField = titleField;
    }
}
