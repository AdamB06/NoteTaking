package scenes.test.scenes;

import client.scenes.HomePageCtrl;
import client.services.AutoSaveService;
import client.services.MarkdownService;
import client.services.NoteService;
import client.utils.ServerUtils;
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
    private MarkdownService markdownService;
    private NoteService noteService;
    private AutoSaveService autoSaveService;


    @BeforeEach
    public void setUp() {
        notesBodyArea = "";
        homePageCtrl = new HomePageCtrl(null, null, null, null);
        markdownService = new MarkdownService();
        noteService = new NoteService(null, null);
        autoSaveService = new AutoSaveService(null,noteService);
    }

    @Test
    public void testHeaderMarkdownConverter() {
        notesBodyArea = "# Hello World";
        String htmlText = markdownService.convertToHtml(notesBodyArea);
        assertEquals("<h1>Hello World</h1>\n", htmlText);
    }

    @Test
    public void testEmptyMarkdownConverter() {
        String htmlText = markdownService.convertToHtml(notesBodyArea);
        assertEquals("", htmlText);
    }

    @Test
    public void testSimpleTextMarkdownConverter() {
        notesBodyArea = "This is simple text to be tested.";
        String htmlText = markdownService.convertToHtml(notesBodyArea);
        assertEquals("<p>This is simple text to be tested.</p>\n", htmlText);
    }

    @Test
    public void testSubHeaderMarkdownConverter() {
        notesBodyArea = "## Subheader";
        String htmlText = markdownService.convertToHtml(notesBodyArea);
        assertEquals("<h2>Subheader</h2>\n", htmlText);
    }

    @Test
    public void testBoldItalicMarkdownConverter() {
        notesBodyArea = "***Bold And Italic Text***";
        String htmlText = markdownService.convertToHtml(notesBodyArea);
        assertEquals("<p><em><strong>Bold And Italic Text</strong></em></p>\n", htmlText);
    }

    @Test
    public void testMultipleParagraphMarkdownConverter() {
        notesBodyArea = "First paragraph.\n\nSecond paragraph.";
        String htmlText = markdownService.convertToHtml(notesBodyArea);
        assertEquals("<p>First paragraph.</p>\n<p>Second paragraph.</p>\n", htmlText);
    }

    @Test
    public void testUninitializedTextArea() {
        assertThrows(IllegalStateException.class, homePageCtrl::addListener);
    }

    @Test
    public void testSearch(){
        String[] titles = {"Title1", "Title2", "Title3", "Title4"};

        // Contents for the notes
        String[] contents = {"Content1", "Content2", "Content3", "Content4"};

        // Create and store the notes
        List<Note> testList = new ArrayList<>();
        for (int i = 0; i < titles.length; i++) {
            Note note = new Note(titles[i], contents[i], 0, "");
            testList.add(note);
        }
        List<Note> expectedList = new ArrayList<>();
        expectedList.addAll(testList);
        assertEquals(expectedList, noteService.filterNotes("title", testList));
    }

    @Test
    public void testSearchSecond(){
        String[] titles = {"Title1", "Title2", "Title3", "Title4"};

        // Contents for the notes
        String[] contents = {"Content1", "Content2", "Content3", "Content4"};

        // Create and store the notes
        List<Note> testList = new ArrayList<>();
        for (int i = 0; i < titles.length; i++) {
            Note note = new Note(titles[i], contents[i], 0, "");
            testList.add(note);
        }
        List<Note> expectedList = new ArrayList<>();
        expectedList.addAll(testList);
        assertEquals(expectedList, noteService.filterNotes("content", testList));
    }

    @Test
    public void testSearchThird(){
        String[] titles = {"Title1", "Title2", "Title3", "Title4"};

        // Contents for the notes
        String[] contents = {"Content1", "Content2", "Content3", "Content4"};

        // Create and store the notes
        List<Note> testList = new ArrayList<>();
        for (int i = 0; i < titles.length; i++) {
            Note note = new Note(titles[i], contents[i], 0, "");
            testList.add(note);
        }
        List<Note> expectedList = new ArrayList<>();
        expectedList.addAll(testList);
        assertNotEquals(expectedList, noteService.filterNotes("nothingInCommonString", testList));
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
        assertEquals(changes, autoSaveService.getChanges(original, modified));
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
        assertEquals(changes, autoSaveService.getChanges(original, modified));
    }
}