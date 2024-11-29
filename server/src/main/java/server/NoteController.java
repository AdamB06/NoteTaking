package server;


import commons.Note;
import org.springframework.web.bind.annotation.*;
import server.database.NoteRepository;

@RestController
@RequestMapping("/Note")
public class NoteController {
    private NoteRepository noteRepository;

    public NoteController(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    /**
     * This makes a new note and adds it to the repository.
     */
    @PostMapping("/")
    @ResponseBody
    public void newNote(@RequestBody Note note) {
        noteRepository.save(note);
        System.out.println(note);
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
