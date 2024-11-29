package server;


import commons.Note;
import org.springframework.web.bind.annotation.*;
import server.database.NoteRepository;

@RestController
@RequestMapping("/Note")
public class NotesController {
    private final NoteRepository noteRepository;

    public NotesController(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    /**
     * Adds the given note to the database
     *
     * @param note note to be added to the database
     */
    @PostMapping("/")
    @ResponseBody
    public void newNote(@RequestBody Note note) {
        noteRepository.save(note);
    }
}