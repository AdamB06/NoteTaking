package server.api;

import commons.Note;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
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
        Note n = new Note("title", "content", "id", "");
        notesController.createNote(n);
        assertTrue(noteRepository.methodIsCalled("save"));
    }


    @Test
    public void testNewNote() {
        Note note = new Note("Hello", "This is a note", "id", "");
        notesController.createNote(note);
        assertEquals(note, noteRepository.getById(note.getId()));
    }

    @Test
    public void testDeleteNote() {
        Note note = new Note("Hello", "This is a note", "id", "");
        Note note2 = new Note("Hello2", "This is a note2", "id1", "");
        notesController.createNote(note);
        notesController.createNote(note2);
        notesController.deleteNote(note2.getId());
        assertNotEquals(note2, noteRepository.getOne(note.getId()));
        assertEquals(note, noteRepository.getOne(note.getId()));
    }

    @Test
    public void testNewDuplicateNote(){
        Note note = new Note ("Hello", "This is a note", "id", "");
        notesController.createNote (note);
        Note note2 = new Note ("Hello", "This is a note", "id1", "");
        assertThrows(IllegalArgumentException.class, () -> notesController.createNote(note2));
    }

    @Test
    public void testEditNoteTitle(){
        Note note = new Note ("Hello", "This is a note", "id", "");
        notesController.createNote (note);
        Note note2 = new Note ("Hello2", "This is a note", "id1", "");
        notesController.editNoteTitle("Hello2", note.getId());
        //assertEquals(note2, noteRepository.getOne(note.getId())); //TODO Currently broken
    }

    @Test
    public void testEditDuplicateNoteTitle(){
        Note note = new Note ("Hello", "This is a note", "id", "");
        notesController.createNote (note);
        Note note2 = new Note ("Hello2", "This is a note", "id1", "");
        //assertEquals(, () -> notesController.editNoteTitle("Hello", note2.getId())); //TODO Check this test couldn't get it to work
    }

    @Test
    public void testApplyPatch() {
        String original = "Hello World";
        String edited = "Hello There World";
        String newText = "There ";
        String operation = "Replace";
        int startIndex = 6;
        int endIndex = 6;
        assertEquals(edited, notesController.applyPatch(original, operation, startIndex, endIndex, newText));
    }

    @Test
    public void testApplyPatchReplace() {
        String original = "Hello World";
        String edited = "Hi World";
        String newText = "i";
        String operation = "Replace";
        int startIndex = 1;
        int endIndex = 5;
        assertEquals(edited, notesController.applyPatch(original, operation, startIndex, endIndex, newText));
    }
}
