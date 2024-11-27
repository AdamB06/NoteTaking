package commons;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class NoteTest {

    private static final Note note = new Note("TestNote", "This is a Test");

    @Test
    public void ConstructorTest(){
        assertEquals("TestNote", note.getTitle());
        assertNotEquals("Another test", note.getContent());
        assertNotNull(note);

    }

    @Test
    public void equalsHashCodeTest(){
        Note note1 = new Note("TestNote", "This is a Test");
        Note note2 = new Note("TestNote", "This is another Test");

        assertEquals(note1, note);
        assertEquals(note, note);
        assertEquals(note.hashcode(), note1.hashcode());
        assertNotEquals(note.hashcode(), note2.hashcode());
        assertNotEquals(note, note2);

    }

    @Test
    public void ToStringTest(){

        var NoteTest = new Note("ToStringTest","This is the content").toString();
        assertTrue(NoteTest.contains("\n"));
        assertTrue(NoteTest.contains("Note"));
        assertTrue(NoteTest.contains(Note.class.getSimpleName()));



    }

}