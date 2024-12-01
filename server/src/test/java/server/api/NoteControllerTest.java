package server.api;

import commons.Note;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.NoteController;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class NoteControllerTest {

    private NoteController nc;
    private TestNoteRepository nr;

    @BeforeEach
    public void setup() {
        nr = new TestNoteRepository();
        nc = new NoteController(new NoteService(nr));
    }

    @Test
    public void databaseIsUsed() {
        Note n = new Note("title", "content");
        nc.createNote(n);
        assertTrue(nr.methodIsCalled("save"));
    }
}
