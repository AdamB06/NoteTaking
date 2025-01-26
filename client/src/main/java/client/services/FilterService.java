package client.services;

import commons.Note;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class FilterService {
    private List<Note> filteredNotes = new ArrayList<>();
    private Consumer<List<Note>> onFilterUpdate;

    public void setFilteredNotes(List<Note> notes) {
        this.filteredNotes = notes;
        if (onFilterUpdate != null) {
            onFilterUpdate.accept(filteredNotes);
        }
    }

    /**
     *
     * @return returns filteredNotes
     */
    public List<Note> getFilteredNotes() {
        return filteredNotes;
    }

    public void setOnFilterUpdate(Consumer<List<Note>> callback) {
        this.onFilterUpdate = callback;
    }
}
