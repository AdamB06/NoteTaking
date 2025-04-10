package commons;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

public class CollectionTest {

    private static final Note note1 = new Note("User", "This is the first Test note");
    private static final Note note2 = new Note("User1", "This is the second Test note");
    private static final List<Note> notes = new ArrayList<>();


    static {
        notes.add(note1);
        notes.add(note2);
    }

    private static final Collection collection = new Collection("TestUser", notes);

    @Test
    public void ConstructorTest() {
        assertEquals("User", collection.getNotes().get(0).getTitle());
        assertEquals(2, collection.getNotes().size());
        assertNotNull(collection);
    }

    @Test
    public void constructorWithEmptyListTest() {
        List<Note> emptyNotes = new ArrayList<>();
        Collection emptyCollection = new Collection("User", emptyNotes);
        assertEquals(0, emptyCollection.getNotes().size());
    }


    @Test
    public void equalsHashCodeTest() {
        Collection collection1 = new Collection("TestUser", notes);
        Collection collection2 = new Collection("AnotherUser", notes);

        assertEquals(collection1, collection1);
        assertNotEquals(collection1, collection2);
        assertEquals(collection.hashcode(), collection1.hashcode());
        assertNotEquals(collection.hashcode(), collection2.hashcode());
    }

    @Test
    public void toStringTest() {
        Collection collection = new Collection("TestCollection", new ArrayList<>());
        String collectionToString = collection.toString();
        assertTrue(collectionToString.contains("TestCollection"));
        assertTrue(collectionToString.contains("notes"));
        assertTrue(collectionToString.contains("[]"));  // If the notes list is empty
    }

}
