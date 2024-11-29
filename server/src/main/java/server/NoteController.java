package server;


import commons.Note;
import org.springframework.web.bind.annotation.*;
import server.database.NoteRepository;

@RestController
@RequestMapping("/Note")
public class NoteController {
    private final NoteRepository noteRepository;

    public NoteController(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    /**
     * Adds the given note to the database
     * @param note note to be added to the database
     */
    @PostMapping("/")
    @ResponseBody
    public void newNote(@RequestBody Note note) {
        noteRepository.save(note);
    }

    /**
     * This deletes the note with the given ID.
     * @param ID ID of the note that needs to be deleted.
     */
    @DeleteMapping("/{ID}")
    public void deleteNote(@PathVariable long ID) {
        noteRepository.deleteById(ID);
    }
}
