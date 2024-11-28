package client.scenes;

import jakarta.inject.Inject;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.web.WebView;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

import java.net.URL;
import java.util.ResourceBundle;

public class HomePageCtrl implements Initializable {
    private PrimaryCtrl pc;

    @FXML
    private TextArea notesBodyArea;
    @FXML
    private WebView webView;

    private Parser parser;
    private HtmlRenderer renderer;

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
    }

    /**
     * Adds a listener to the notesBodyArea.
     */
    public void addListener() {
        if (notesBodyArea != null) {
            notesBodyArea.textProperty()
                    .addListener((observable, oldValue, markdownText) -> {
                        markdownConverter(markdownText);
                    });
        } else {
            System.out.println("TextArea not initialized!");
        }
    }

    /**
     * Converts Markdown text to HTML and updates the WebView.
     * @param markdownText the Markdown text to be converted
     */
    public void markdownConverter(String markdownText) {
        Node text = parser.parse(markdownText);
        String htmlText = renderer.render(text);
        updateWebView(htmlText);
    }

    /**
     * Updates the WebView with the given HTML text.
     * @param htmlText the HTML text to be displayed
     */
    public void updateWebView(String htmlText) {
        webView.getEngine().loadContent(htmlText);
    }
}