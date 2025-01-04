package scenes.test.scenes;

import client.scenes.HomePageCtrl;
import commons.Note;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class HomePageCtrlTest {
    private String notesBodyArea;
    private HomePageCtrl homePageCtrl;


    @BeforeEach
    public void setUp() {
        notesBodyArea = "";
        homePageCtrl = new HomePageCtrl(null, null);
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

    // These are the tests that I imported these test methods from the other test class that is know deleted, they
    // are in comments due to the fact that these will break the pipeline due to not being able to connect to the
    // server
      //TODO: Fixing the two tests
//    @Test
//    public void testCreateNote() {
//        Note note = homePageCtrl.createNote();
//        assertEquals("New Note", note.getTitle());
//        assertEquals("", note.getContent());
//    }
//
//    @Test
//    public void testCreateNoteTwice() {
//        Note noteOne = homePageCtrl.createNote();
//        Note noteTwo = homePageCtrl.createNote();
//        assertEquals("New Note", noteOne.getTitle());
//        assertEquals("", noteOne.getContent());
//        assertEquals("New Note", noteTwo.getTitle());
//        assertEquals("", noteTwo.getContent());
//    }

    @Test
    public void testSearch(){
        String[] titles = {"Title1", "Title2", "Title3", "Title4"};

        // Contents for the notes
        String[] contents = {"Content1", "Content2", "Content3", "Content4"};

        // Create and store the notes
        List<Note> testList = new ArrayList<>();
        for (int i = 0; i < titles.length; i++) {
            Note note = new Note(titles[i], contents[i]);
            testList.add(note);
        }
        List<Note> expectedList = new ArrayList<>();
        expectedList.addAll(testList);
        assertEquals(expectedList, homePageCtrl.filterNotes("title", testList));
    }

    @Test
    public void testSearchSecond(){
        String[] titles = {"Title1", "Title2", "Title3", "Title4"};

        // Contents for the notes
        String[] contents = {"Content1", "Content2", "Content3", "Content4"};

        // Create and store the notes
        List<Note> testList = new ArrayList<>();
        for (int i = 0; i < titles.length; i++) {
            Note note = new Note(titles[i], contents[i]);
            testList.add(note);
        }
        List<Note> expectedList = new ArrayList<>();
        expectedList.addAll(testList);
        assertEquals(expectedList, homePageCtrl.filterNotes("content", testList));
    }

    @Test
    public void testSearchThird(){
        String[] titles = {"Title1", "Title2", "Title3", "Title4"};

        // Contents for the notes
        String[] contents = {"Content1", "Content2", "Content3", "Content4"};

        // Create and store the notes
        List<Note> testList = new ArrayList<>();
        for (int i = 0; i < titles.length; i++) {
            Note note = new Note(titles[i], contents[i]);
            testList.add(note);
        }
        List<Note> expectedList = new ArrayList<>();
        expectedList.addAll(testList);
        assertNotEquals(expectedList, homePageCtrl.filterNotes("nothingInCommonString", testList));
    }

    @Test
    public void testGetChanges() {
        String original = "Hello World";
        String modified = "Hello There World";
        Map<String, Object> changes = new HashMap<>();
        changes.put("operation", "Replace");
        changes.put("startIndex", 6);
        changes.put("endIndex", 6);
        changes.put("newText", "There ");
        assertEquals(changes, homePageCtrl.getChanges(original, modified));
    }

    @Test
    public void testGetChangesReplace() {
        String original = "Hello World";
        String modified = "Hi World";
        Map<String, Object> changes = new HashMap<>();
        changes.put("operation", "Replace");
        changes.put("startIndex", 1);
        changes.put("endIndex", 5);
        changes.put("newText", "i");
        assertEquals(changes, homePageCtrl.getChanges(original, modified));
    }

}