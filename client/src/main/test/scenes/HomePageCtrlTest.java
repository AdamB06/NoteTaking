package scenes;

import commons.Note;
import client.scenes.HomePageCtrl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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

    @Test
    public void testCreateNote() {
        Note note = homePageCtrl.createNote();
        assertEquals(note.getTitle(), "");
        assertEquals(note.getContent(), "");
        long id = 1;
        assertEquals(note.getId(), id);
    }

    @Test
    public void testCreateNoteTwice() {
        Note noteOne = homePageCtrl.createNote();
        Note noteTwo = homePageCtrl.createNote();
        assertEquals(noteOne.getTitle(), "");
        assertEquals(noteOne.getContent(), "");
        long id = 1;
        assertEquals(noteOne.getId(), id);
        assertEquals(noteTwo.getTitle(), "");
        assertEquals(noteTwo.getContent(), "");
        long id = 2;
        assertEquals(noteTwo.getId(), id);
    }
}