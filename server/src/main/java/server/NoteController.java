package server;


import commons.Note;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import server.api.NoteService;
import server.database.NoteRepository;
import org.springframework.http.ResponseEntity;
import java.util.List;

@RestController
@RequestMapping("/Note")
public class NoteController {
    private final NoteRepository noteRepository;
    private final NoteService noteService;

    /**
     * @param noteRepository The note repository
     * @param noteService The note service interface
     */
    @Autowired
    public NoteController(NoteRepository noteRepository, NoteService noteService) {
        this.noteRepository = noteRepository;
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
        if(checkDuplicateTitle(note.getTitle())) {
            return ResponseEntity.badRequest().build();
        }
        else{
            Note savedNote = noteService.saveNote(note);
            return ResponseEntity.ok(savedNote);
        }
    }

    /**
     * Checks if the provided note title is a duplicate from the list of notes.
     * @param title The title of the note
     * @return True if the title is a duplicate, false otherwise.
     */
    public boolean checkDuplicateTitle(String title) {
        List<Note> notes = noteService.getAllNotes();
        for (Note note : notes) {
            if (note.getTitle().equals(title)) {
                return true;
            }
        }
        return false;
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

    /**
     * This deletes the note with the given ID.
     * @param id ID of the note that needs to be deleted.
     */
    @DeleteMapping("/{id}")
    public void deleteNote(@PathVariable long id) {
        noteRepository.deleteById(id);
    }
}