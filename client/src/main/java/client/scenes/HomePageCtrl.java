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

    private TitleCtrl titleCtrl;
    private Parser parser;
    private HtmlRenderer renderer;
    private final PrimaryCtrl pc;

    /**
     * Constructor for HomePageCtrl.
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
     * @param url the URL
     * @param resourceBundle the ResourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addListener();
        webView.getEngine().loadContent("");
        titleCtrl = new TitleCtrl(titleField, editButton, pc);
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
     * @param markdownText the Markdown text to be converted
     * @return the HTML text
     */
    public String markdownConverter(String markdownText) {
        Node text = parser.parse(markdownText);
        return renderer.render(text);
    }

    /**
     * Updates the WebView with the given HTML text.
     * @param htmlText the HTML text to be displayed
     */
    public void updateWebView(String htmlText) {
        webView.getEngine().loadContent(htmlText);
    }
}