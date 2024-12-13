package commons;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class TagTest {

    private Tag tag;
    private Note note1;
    private Note note2;

    @BeforeEach
    public void setUp() {

        tag = new Tag(1L, "Test Tag");
        note1 = new Note();
        note2 = new Note();
    }

    @Test
    public void testTagCreation() {
        // Check if the tag is created with the right name and id
        assertEquals("Test Tag", tag.getName(), "Tag should have the name 'Test Tag'.");
        assertNotNull(tag.getId(), "Tag ID should not be null.");
    }

    @Test
    public void testAddTagToNote() {

        note1.addTag(tag);

        // Assert that the tag is in the note's set of tags
        assertTrue(note1.getTags().contains(tag), "Note should contain the tag.");

        // Assert that the note is added to the tag's notes set
        assertTrue(tag.getNotes().contains(note1), "Tag should contain the note.");
    }

    @Test
    public void testRemoveTagFromNote() {

        note1.addTag(tag);
        note1.removeTag(tag);

        // Assert that the note no longer contains the tag
        assertFalse(note1.getTags().contains(tag), "Note should not contain the tag after removal.");

        // Assert that the tag no longer contains the note
        assertFalse(tag.getNotes().contains(note1), "Tag should not contain the note after removal.");
    }

    @Test
    public void testAddMultipleTagsToNote() {

        note1.addTag(tag);
        note1.addTag(tag);  // Adding the same tag again

        // Since Set does not allow duplicates, assert that the note only contains one tag
        assertEquals(1, note1.getTags().size(), "Note should contain only one instance of the tag.");
    }

    @Test
    public void testTagAndNotesRelationship() {

        note1.addTag(tag);
        note2.addTag(tag);

        // Assert that the tag is linked to both notes
        assertTrue(tag.getNotes().contains(note1), "Tag should contain note1.");
        assertTrue(tag.getNotes().contains(note2), "Tag should contain note2.");

        // Assert that both notes contain the tag
        assertTrue(note1.getTags().contains(tag), "Note1 should contain the tag.");
        assertTrue(note2.getTags().contains(tag), "Note2 should contain the tag.");
    }

    @Test
    public void testTagEqualsAndHashCode() {
        Tag tag1 = new Tag(1L, "Test Tag");
        Tag tag2 = new Tag(1L, "Test Tag");

        // Assert that two tags with the same id and name are considered equal
        assertEquals(tag1, tag2, "Tags with the same id and name should be equal.");
        assertEquals(tag1.hashcode(), tag2.hashcode(), "Tags with the same id and name should have the same hashcode.");
    }
}
