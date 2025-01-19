package server.api;

import commons.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import server.database.NoteRepository;

import commons.Note;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
     *
     * @param note note attribute
     * @return returns saved note
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

    /**
     *
     * @param tagName name of the tag
     * @return returns the list of notes with that tag name
     */
    public List<Note> findNotesByTagName(String tagName) {
        return  noteRepository.findNotesByTagName(tagName);
    }

    /**
     *
     * @return returns the list of all the tag names
     */
    public List<String> getAllTagNames() {
        // Fetch all tags from the database, assuming each note can have multiple tags
        List<Note> notes = noteRepository.findAll();
        Set<String> tagNames = new HashSet<>();


        for (Note note : notes) {
            for (Tag tag : note.getTags()) {
                tagNames.add(tag.getName());
            }
        }

        return new ArrayList<>(tagNames);
    }

}
