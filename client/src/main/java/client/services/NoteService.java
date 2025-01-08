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

    @Inject
    public NoteService(ServerUtils serverUtils) {
        this.serverUtils = serverUtils;
    }

    public List<Note> getNotes() {
        if (notes.isEmpty()) {
            notes = serverUtils.getNotes();
        }
        return notes;
    }

    public Note createNote() {
        int counter = 1;
        String uniqueTitle = "New Note Title " + counter;
        while (serverUtils.isTitleDuplicate(uniqueTitle)) {
            uniqueTitle = "New Note Title " + counter;
            counter++;
        }
        Note note = new Note(uniqueTitle, "New Note Content");
        Note createdNote = serverUtils.sendNote(note);

        if (createdNote != null) {
            notes.add(createdNote);
        }
        return createdNote;
    }

    public String deleteNote(Note note) {
        String status = serverUtils.deleteNote(note);
        if ("Successful".equals(status)) {
            notes.remove(note);
        }
        return status;
    }

    public String updateNoteTitle(long noteId, String newTitle) {
        String updatedTitle = serverUtils.updateNoteTitle(noteId, newTitle);
        return updatedTitle;
    }

    public String saveChanges(long noteId, Map<String, Object> changes) {
        return serverUtils.saveChanges(noteId, changes);
    }

    public Note getNoteById(long noteId) {
        return serverUtils.getNoteById(noteId);
    }

    public List<Note> filterNotes(String query) {
        String fixedSearchQuery = query.toLowerCase().trim();
        List<Note> filteredNotes = new ArrayList<>();
        for (Note note : notes) {
            if (note.getTitle().toLowerCase().contains(fixedSearchQuery) ||
                    note.getContent().toLowerCase().contains(fixedSearchQuery)) {
                filteredNotes.add(note);
            }
        }
        return filteredNotes;
    }

    public void refreshNotes() {
        notes = serverUtils.getNotes();
    }
}
