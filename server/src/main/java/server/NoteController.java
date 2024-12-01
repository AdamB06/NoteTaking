package server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import commons.Note;
import server.api.NoteService;

import java.util.List;

@RestController
@RequestMapping("/notes")
public class NoteController {

    private final NoteService noteService;

    /**
     * Constructor for NoteController.
     * This injects the NoteService to interact with the service layer.
     *
     * @param noteService The service layer for handling logic related to notes.
     */
    @Autowired
    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    /**
     * Endpoint to create a new note.
     * This method accepts a JSON Note object, saves it in the database,
     * and returns the saved Note with its generated ID.
     *
     * @param note The note object to be created.
     *             The data is passed in the body of the request as JSON.
     * @return A ResponseEntity containing the saved note with a 200 OK status.
     */
    @PostMapping
    public ResponseEntity<Note> createNote(@RequestBody Note note) {
        Note savedNote = noteService.saveNote(note);
        return ResponseEntity.ok(savedNote);
    }

    /**
     * Endpoint to retrieve all notes from the database.
     * This method retrieves all notes stored in the database and returns them in a JSON array.
     *
     * @return A ResponseEntity containing a list of all notes, along with a 200 OK status.
     */
    @GetMapping
    public ResponseEntity<List<Note>> getAllNotes() {
        List<Note> notes = noteService.getAllNotes();
        return ResponseEntity.ok(notes);
    }
}
