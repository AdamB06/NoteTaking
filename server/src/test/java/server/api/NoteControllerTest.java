package server.api;

import commons.Note;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.NoteController;

import static org.junit.jupiter.api.Assertions.*;

public class NoteControllerTest {

    private NoteController notesController;
    private TestNoteRepository noteRepository;

    @BeforeEach
    public void setup() {
        noteRepository = new TestNoteRepository();
        notesController = new NoteController(noteRepository, new NoteService(noteRepository));
    }

    @Test
    public void databaseIsUsed() {
        Note n = new Note("title", "content");
        notesController.createNote(n);
        assertTrue(noteRepository.methodIsCalled("save"));
    }


    @Test
    public void testNewNote() {
        Note note = new Note("Hello", "This is a note");
        notesController.createNote(note);
        assertEquals(note, noteRepository.getById(note.getId()));
    }

    @Test
    public void testDeleteNote() {
        Note note = new Note("Hello", "This is a note");
        Note note2 = new Note("Hello2", "This is a note2");
        notesController.createNote(note);
        notesController.createNote(note2);
        notesController.deleteNote(note2.getId());
        assertNotEquals(note2, noteRepository.getOne(note.getId()));
        assertEquals(note, noteRepository.getOne(note.getId()));
    }
}
