package server;

import commons.Note;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import server.api.NoteService;
import server.database.NoteRepository;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/Note")
public class NoteController {
    private final NoteRepository noteRepository;
    private final NoteService noteService;

    /**
     * @param noteRepository The note repository
     * @param noteService    The note service interface
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
        if (checkDuplicateTitle(note.getTitle(), -1)) {
            throw new IllegalArgumentException("Note title already exists");
        } else {
            Note savedNote = noteService.saveNote(note);
            return ResponseEntity.ok(savedNote);
        }
    }

    /**
     * Endpoint to edit the content of a note.
     *
     * @param title New title for the note
     * @param id    ID of the note to be edited
     * @return The edited note
     */
    @PutMapping("/{id}")
    public ResponseEntity<Note> editNoteTitle(@RequestBody String title,
                                              @PathVariable("id") long id) {
        Note note = noteService.getNoteById(id);
        if (checkDuplicateTitle(title, id)) {
            throw new IllegalArgumentException("Note title already exists");
        } else {
            String oldTitle = note.getTitle();
            note.setTitle(title);
            Note savedNote = noteService.saveNote(note);
            List<Note> allNotes = noteService.getAllNotes();
            for (Note n : allNotes) {
                if (n.getContent().contains("[[" + oldTitle + "]]")) {
                    String updatedContent = n.getContent().replace("[[" + oldTitle + "]]",
                            "[[" + title + "]]");
                    n.setContent(updatedContent);
                    noteService.saveNote(n);
                }
            }

            return ResponseEntity.ok(savedNote);
        }
    }


    /**
     * @param title The title of the note
     * @return True if the title is a duplicate, false otherwise.
     */
    @GetMapping("/checkDuplicateTitle/{title}")
    public ResponseEntity<Boolean> checkDuplicateTitleEndpoint(@PathVariable String title) {
        boolean isDuplicate = checkDuplicateTitle(title, -1);
        return ResponseEntity.ok(isDuplicate);
    }

    /**
     * Checks if the provided note title is a duplicate from the list of notes.
     *
     * @param title The title of the note
     * @param id    The ID of the note to be edited
     * @return True if the title is a duplicate, false otherwise.
     */
    public boolean checkDuplicateTitle(String title, long id) {
        List<Note> notes = noteService.getAllNotes();
        for (Note note : notes) {
            if (note.getTitle().equals(title) && note.getId() != id) {
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
     *
     * @param id ID of the note that needs to be deleted.
     */
    @DeleteMapping("/{id}")
    public void deleteNote(@PathVariable long id) {
        noteRepository.deleteById(id);
    }

    /**
     * Endpoint for patch request to edit content of note
     *
     * @param id             of the to be edited note
     * @param changes        to be added into the contents
     * @param overrideMethod to use post as patch mapping
     * @return returns a response of if the method successfully executed
     */
    @PostMapping("/{id}")
    public ResponseEntity<Void> patchNote(@PathVariable("id") long id,
                                          @RequestBody Map<String, Object> changes,
                                          @RequestHeader(value = "X-HTTP-Method-Override",
                                                  required = false) String overrideMethod) {
        if ("PATCH".equals(overrideMethod)) {
            String operation = (String) changes.get("operation");
            int startIndex = (Integer) changes.get("startIndex");
            int endIndex = (Integer) changes.get("endIndex");
            String newText = (String) changes.get("newText");

            Note note = noteService.getNoteById(id);
            String originalContent = note.getContent();

            String updatedContent = applyPatch(originalContent,
                    operation, startIndex, endIndex, newText);

            note.setContent(updatedContent);
            noteService.saveNote(note);

            noteService.saveNote(note);
            System.out.println("Updated Content");
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).build();
        }
    }

    /**
     * Takes the patch and applies it on the content in the database
     *
     * @param originalContent the saved content
     * @param operation       what operation to do
     * @param startIndex      from where to place the new text
     * @param endIndex        to where to place the new text
     * @param newText         the text to be added
     * @return the resulting string
     */
    public String applyPatch(String originalContent, String operation,
                             int startIndex, int endIndex, String newText) {
        if ("Replace".equals(operation)) {
            if (startIndex < 0) {
                startIndex = 0;
            }
            if (endIndex < 0) {
                endIndex = 0;
            }
            if (startIndex > originalContent.length()) {
                startIndex = originalContent.length();
            }
            if (endIndex > originalContent.length()) {
                endIndex = originalContent.length();
            }
            if (startIndex > endIndex) {
                // handle the case where startIndex is bigger than endIndex
                // for example, you might swap them or skip the patch
                int temp = startIndex;
                startIndex = endIndex;
                endIndex = temp;
            }
            return originalContent.substring(0, startIndex) + newText +
                    originalContent.substring(endIndex);
        }
        throw new UnsupportedOperationException("Unsupported operation: " + operation);
    }

    /**
     * @param id identification of the note
     * @return the id of the note
     */
    @GetMapping("/{id}")
    public ResponseEntity<Note> getNoteById(@PathVariable long id) {
        Note note = noteService.getNoteById(id);
        if (note == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(note);
    }

    /**
     * @param tagName name of the tag
     * @return returns the list of notes with that specific tagname
     */
    @GetMapping("/tags/{tagName}")
    public ResponseEntity<List<Note>> getNotesByTagName(@PathVariable String tagName) {
        List<Note> notes = noteService.findNotesByTagName(tagName);
        return ResponseEntity.ok(notes);
    }


}