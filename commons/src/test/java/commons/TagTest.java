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

        tag = new Tag("Test Tag");
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
        Tag tag1 = new Tag("Test Tag");
        Tag tag2 = new Tag("Test Tag");

        // Assert that two tags with the same id and name are considered equal
        assertEquals(tag1, tag2, "Tags with the same  name should be equal.");
        assertEquals(tag1.hashCode(), tag2.hashCode(), "Tags with the same  name should have the same hashcode.");
    }

    @Test
    public void testRemoveTagFromNote() {
        note1.addTag(tag);
        note1.removeTag(tag);


        assertFalse(note1.getTags().contains(tag));
    }

    @Test
    public void testTagWithDifferentNotes() {
        Tag tag1 = new Tag("Unique Tag");
        Note note3 = new Note();
        Note note4 = new Note();

        note3.addTag(tag1);
        note4.addTag(tag1);


        assertTrue(note3.getTags().contains(tag1));
        assertTrue(note4.getTags().contains(tag1));


        assertTrue(tag1.getNotes().contains(note3));
        assertTrue(tag1.getNotes().contains(note4));
    }

    @Test
    public void testTagUniqueness() {

        Tag tag1 = new Tag("Common Tag");
        Tag tag2 = new Tag("Common Tag");


        assertNotSame(tag1, tag2);
    }


    @Test
    public void testTagAddRemoveInNote() {
        note1.addTag(tag);
        assertTrue(note1.getTags().contains(tag));

        note1.removeTag(tag);
        assertFalse(note1.getTags().contains(tag));
    }
}


