package commons;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class NoteTest {

    private static final Note note = new Note("TestNote", "This is a Test");

    @Test
    public void ConstructorTest() {
        assertEquals("TestNote", note.getTitle());
        assertNotEquals("Another test", note.getContent());
        assertNotNull(note);

    }

    @Test
    public void equalsHashCodeTest() {
        Note note1 = new Note("TestNote", "This is a Test");
        Note note2 = new Note("TestNote", "This is another Test");

        assertEquals(note1, note);
        assertEquals(note, note);
        assertEquals(note.hashcode(), note1.hashcode());
        assertNotEquals(note.hashcode(), note2.hashcode());
        assertNotEquals(note, note2);

    }

    @Test
    public void ToStringTest() {

        var NoteTest = new Note("ToStringTest", "This is the content").toString();
        assertTrue(NoteTest.contains("\n"));
        assertTrue(NoteTest.contains("Note"));
        assertTrue(NoteTest.contains(Note.class.getSimpleName()));


    }

    @Test
    public void noteTitleNotEmpty(){
        Note note = new Note("   ", "This is a Test");
        Note note2 = new Note("", "This is a Test");
        Note note3 = new Note("title", "This is another Test");

        String expected = "New Note";

        String actual = note.getTitle();
        String actual2 = note2.getTitle();

        assertEquals(expected, actual);
        assertEquals(expected, actual2);
        assertEquals("title", note3.getTitle());
    }
@Test
    public void testAddTag() {
        Tag tag = new Tag( "Java");
        Note note = new Note();

        note.addTag(tag);


        assertTrue(note.getTags().contains(tag));

        // Verify that the tag contains the note (bi-directional relationship)
        assertTrue(tag.getNotes().contains(note));
    }

    @Test
    public void testRemoveTag() {
        Tag tag = new Tag( "Java");
        Note note = new Note();

        note.addTag(tag);
        note.removeTag(tag);

        assertFalse(note.getTags().contains(tag));


        assertFalse(tag.getNotes().contains(note));
    }

}