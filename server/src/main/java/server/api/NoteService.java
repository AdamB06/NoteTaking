package server.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import server.database.NoteRepository;

import commons.Note;

import java.util.List;
import java.util.Optional;

@Service
public class NoteService {

    private final NoteRepository noteRepository;

    /**
     * Constructor for NoteService. This injects the NoteRepository to interact with the data layer.
     *
     * @param noteRepository The repository layer for interacting with the database.
     */
    @Autowired
    public NoteService(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    /**
     * Saves a new note or updates an existing one in the database.
     *
     * @param note The note object to be saved. The note data is persisted in the database.
     * @return The saved or updated note, including its generated or existing ID.
     */
    public Note saveNote(Note note) {
        return noteRepository.save(note);
    }

    /**
     * Retrieves all notes from the database.
     *
     * @return A list of all notes stored in the database.
     */
    public List<Note> getAllNotes() {
        return noteRepository.findAll();
    }

    /**
     * Retrieves a note by its ID from the database.
     * @param id The ID of the note to be retrieved.
     * @return The note with the specified ID.
     */
    public Note getNoteById(long id) {
        return noteRepository.getReferenceById(id);
    }
}
