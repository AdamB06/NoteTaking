package client;

import client.services.NoteService;
import commons.Note;
import commons.Tag;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;


import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class TagController {
    private NoteService noteService;



    /**
     * Process tags in the note text and save them to the current note.
     *
     * @param content content of the note
     * @param note    current note
     * @param universalList list of all the tags that are in notes for the combobox;
     */
    public void initializeTags(String content, Note note, Set<Tag> universalList) {
        System.out.println("Initializing tags...");

        if (content == null || content.isEmpty()) {
            System.out.println("No content to process for tags.");
            return;
        }


        if (note == null) {
            System.out.println("No current note is selected.");
            return;
        }

        try {
            // Pattern to match hashtags like #tag1, #tag2, etc.
            Pattern pattern = Pattern.compile("#(\\w+)");
            Matcher matcher = pattern.matcher(content);

            while (matcher.find()) {
                String tagName = matcher.group(1); // this is so that we store the tag without the #
                System.out.println("Extracted tag: " + tagName);

                if (!(note.getTags().contains(tagName))) {
                    Tag newTag = new Tag(tagName);
                    note.getTags().add(newTag);
                    universalList.add(newTag);
                    System.out.println(note.getTags());
                    System.out.println(universalList);

                }


               /* for (Tag tag : note.getTags()) {
                    if (!note.getContent().contains(tag.getName())) {
                        note.removeTag(tag);
                        System.out.println("Tag removed: " + tag.getName());
                    }
                }



                */

            }
        } catch (Exception e) {
            System.err.println("Error initializing tags: " + e.getMessage());
        }
    }

    /**
     * @param content   the entire content of the note
     * @param character final input of the user
     * @param note      current note that we are looking at
     * @param universalList list of universal tags  across all notes for the combobox
     */
    public void checkForCorrectUserInput(String content, String character,
                                         Note note, Set<Tag> universalList) {


        if (character.equals(" ") || character.equals("\n")) {
            System.out.println("Space detected.");
            String[] words = content.split("\\s+");
            String lastWord = words[words.length - 1];
            if (lastWord.startsWith("#") && lastWord.length() > 1 &&
                    lastWord.substring(1).matches("\\w+")) {
                System.out.println("Valid tag detected: " + lastWord);
                initializeTags(content, note, universalList);
            }
        }
    }

    /**
     *
     * @param selectedTag tag that has been selected from the combobox by the user
     * @param notesListView listview of notes
     */
    public void filterNotesByTag(Tag selectedTag, ListView<Note> notesListView) {
        List<Note> filteredNotes = new ArrayList<>();
        for (Note note : noteService.getNotes()) {
            System.out.println("Checking note: " + note.getTitle());
            System.out.println("Tags: " + note.getTags());
            if (note.getTags().contains(selectedTag)) {
                filteredNotes.add(note);
            }
        }

        System.out.println("Filtered notes count: " + filteredNotes.size());

        updateNotesListView(filteredNotes, notesListView);
    }

    /**
     *
     * @param filteredNotes notes that have been filtered on a tag
     * @param notesListView listview of notes
     */
    public void updateNotesListView(List<Note> filteredNotes, ListView<Note> notesListView) {
        ObservableList<Note> observableNotes = FXCollections.observableArrayList(filteredNotes);
        notesListView.setItems(observableNotes);
    }
}