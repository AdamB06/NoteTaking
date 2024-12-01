package scenes.test.scenes;

import client.scenes.HomePageCtrl;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import javafx.application.Platform;

import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.*;

public class HomePageCtrlTest {
    private String notesBodyArea;
    private HomePageCtrl homePageCtrl;


    @BeforeEach
    public void setUp() {
        notesBodyArea = "";
        homePageCtrl = new HomePageCtrl(null);
    }


    @Test
    public void testHeaderMarkdownConverter() {
        notesBodyArea = "# Hello World";
        String htmlText = homePageCtrl.markdownConverter(notesBodyArea);
        assertEquals("<h1>Hello World</h1>\n", htmlText);
    }

    @Test
    public void testEmptyMarkdownConverter() {
        String htmlText = homePageCtrl.markdownConverter(notesBodyArea);
        assertEquals("", htmlText);
    }

    @Test
    public void testSimpleTextMarkdownConverter() {
        notesBodyArea = "This is simple text to be tested.";
        String htmlText = homePageCtrl.markdownConverter(notesBodyArea);
        assertEquals("<p>This is simple text to be tested.</p>\n", htmlText);
    }

    @Test
    public void testSubHeaderMarkdownConverter() {
        notesBodyArea = "## Subheader";
        String htmlText = homePageCtrl.markdownConverter(notesBodyArea);
        assertEquals("<h2>Subheader</h2>\n", htmlText);
    }

    @Test
    public void testBoldItalicMarkdownConverter() {
        notesBodyArea = "***Bold And Italic Text***";
        String htmlText = homePageCtrl.markdownConverter(notesBodyArea);
        assertEquals("<p><em><strong>Bold And Italic Text</strong></em></p>\n", htmlText);
    }

    @Test
    public void testMultipleParagraphMarkdownConverter() {
        notesBodyArea = "First paragraph.\n\nSecond paragraph.";
        String htmlText = homePageCtrl.markdownConverter(notesBodyArea);
        assertEquals("<p>First paragraph.</p>\n<p>Second paragraph.</p>\n", htmlText);
    }

    @Test
    public void testUninitializedTextArea() {
        assertThrows(IllegalStateException.class, homePageCtrl::addListener);
    }
gi

}