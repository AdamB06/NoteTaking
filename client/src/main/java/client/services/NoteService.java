package client.services;

import client.utils.ServerUtils;
import commons.Note;
import jakarta.inject.Inject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class NoteService {
    private final ServerUtils serverUtils;
    private List<Note> notes = new ArrayList<>();

    /**
     *
     * @param serverUtils makes use of serverutils class in order for some methods to work
     */
    @Inject
    public NoteService(ServerUtils serverUtils) {
        this.serverUtils = serverUtils;
    }

    /**
     * Retrieves notes from the server. If the internal list is empty, fetches from the server.
     *
     * @return List of notes.
     */
    public List<Note> getNotes() {
        if (notes.isEmpty()) {
            notes = serverUtils.getNotes();
        }
        return notes;
    }

    /**
     * Creates a new note with a unique title and sends it to the server.
     *
     * @return The created Note object.
     */
    public Note createNote() {
        int counter = 1;
        String uniqueTitle = "New Note Title " + counter;
        while (serverUtils.isTitleDuplicate(uniqueTitle)) {
            counter++;
            uniqueTitle = "New Note Title " + counter;
        }
        Note note = new Note(uniqueTitle, "New Note Content");
        Note createdNote = serverUtils.sendNote(note);

        if (createdNote != null) {
            notes.add(createdNote);
        }
        return createdNote;
    }

    /**
     * Deletes a note from the server and removes it from the internal list upon success.
     *
     * @param note The note to be deleted.
     * @return Status of the deletion ("Successful" or "Failed").
     */
    public String deleteNote(Note note) {
        String status = serverUtils.deleteNote(note);
        if ("Successful".equals(status)) {
            notes.remove(note);
        }
        return status;
    }

    /**
     * Updates the title of a note on the server.
     *
     * @param note   The ID of the note to be updated.
     * @param newTitle The new title for the note.
     * @return The updated title if successful, otherwise an error message.
     */
    public String updateNoteTitle(Note note, String newTitle) {
        return serverUtils.updateNoteTitle(note.getId(), newTitle);
    }

    /**
     * Saves changes to a note on the server.
     *
     * @param noteId  The ID of the note to be updated.
     * @param changes A map detailing the changes to be made.
     * @return Status of the save operation ("Successful" or "Failed").
     */
    public String saveChanges(long noteId, Map<String, Object> changes) {
        System.out.println("NoteService save changes was called");
        return serverUtils.saveChanges(noteId, changes);
    }

    /**
     * Retrieves a note by its ID from the server.
     *
     * @param noteId The ID of the note to retrieve.
     * @return The Note object if found, otherwise null.
     */
    public Note getNoteById(long noteId) {
        return serverUtils.getNoteById(noteId);
    }

    /**
     * Filters a list of notes based on the search query.
     *
     * @param searchBoxQuery The search query string.
     * @param noteList        The list of notes to filter.
     * @return A list of notes that match the search criteria.
     */
    public List<Note> filterNotes(String searchBoxQuery, List<Note> noteList) {
        List<Note> returnNotes = new ArrayList<>();

        String fixedSearchQuery = searchBoxQuery.toLowerCase().trim();

        for (Note note : noteList) {
            if (note.getTitle().toLowerCase().contains(fixedSearchQuery) ||
                    note.getContent().toLowerCase().contains(fixedSearchQuery)) {
                returnNotes.add(note);
            }
        }
        return returnNotes;
    }

    /**
     * Checks if a note exists in the internal list.
     * @param note variable of note
     * @return returns if the note exists
     */
    public boolean noteExists(Note note) {
        for(Note n : notes) {
            if(n.equals(note)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Refreshes the internal list of notes by fetching from the server.
     */
    public void refreshNotes() {
        notes = serverUtils.getNotes();
    }

    /**
     *
     * @param title representation of the note title
     * @return returns the note if equal otherwise null
     */
    public Note getNoteByTitle(String title) {
        if (title == null || title.isEmpty()) {
            return null;
        }

        for (Note note : notes) {
            if (note.getTitle() != null && note.getTitle().equals(title)) {
                return note;
            }
        }
        return null;
    }

    /**
     * Finds the index of a note in a list of notes.
     * @param note variable of note
     * @param notes list of notes
     * @return returns the index of the note
     */
    public int findNoteIndex(Note note, List<Note> notes) {
        for(int i = 0; i < notes.size(); i++) {
            if(notes.get(i).getId() == note.getId()) {
                return i;
            }
        }
        return -1;
    }

    /**
     * @param searchBoxQuery the search query
     * @return returns the list of notes
     */
    public List<Note> checkFilter(String searchBoxQuery) {
        if (searchBoxQuery == null || searchBoxQuery.isEmpty()) {
            return notes;
        }
        return filterNotes(searchBoxQuery, notes);
    }

    public boolean matchSearch(Note note, String query){
        if(note.getTitle().contains(query) || note.getContent().contains(query)){
            return true;
        }
        return false;
    }

    /**
     * Adds a note to the internal list.
     * @param note the note to be added
     */
    public void addNoteToList(Note note) {
        notes.add(note);
    }
}