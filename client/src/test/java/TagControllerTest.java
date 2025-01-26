import client.TagController;
import client.services.NoteService;
import client.utils.ServerUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TagControllerTest {

    private TagController tagController;
    private NoteService noteService;

    @BeforeEach
    public void setUp() {
        this.noteService = new NoteService(new ServerUtils(""));
        tagController = new TagController(noteService);

    }


    @Test
    public void testProcessNoteLinksWithInvalidNote() {
        String content = "This references [[NonExistentNote]]";
        String processedContent = tagController.processNoteLinks(content);
        assertTrue(processedContent.contains("<span style='color: red;'>[[NonExistentNote]] (not found)</span>"),
                "The content should indicate that the note was not found.");
    }

    @Test
    public void testProcessNoteLinksWithNullContent() {
        String processedContent = tagController.processNoteLinks(null);
        assertNull(processedContent, "The method should return null for null content.");
    }

    @Test
    public void testProcessNoteLinksWithEmptyContent() {
        String processedContent = tagController.processNoteLinks("");
        assertEquals("", processedContent, "The method should return an empty string for empty content.");
    }


}
