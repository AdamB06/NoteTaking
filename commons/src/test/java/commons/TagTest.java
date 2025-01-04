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

        tag = new Tag( "Test Tag");
        note1 = new Note();
        note2 = new Note();
    }

    @Test
    public void testTagCreation() {
        // Check if the tag is created with the right name and id
        assertEquals("Test Tag", tag.getName(), "Tag should have the name 'Test Tag'.");
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
        Tag tag1 = new Tag( "Test Tag");
        Tag tag2 = new Tag( "Test Tag");

        // Assert that two tags with the same id and name are considered equal
        assertEquals(tag1, tag2, "Tags with the same  name should be equal.");
        assertEquals(tag1.hashcode(), tag2.hashcode(), "Tags with the same  name should have the same hashcode.");
    }
}
