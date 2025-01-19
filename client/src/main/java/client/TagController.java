package client;

import client.services.NoteService;
import commons.Note;
import commons.Tag;

import javafx.scene.control.ListView;
import javafx.scene.web.WebView;


import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class TagController {
    private final NoteService noteService;

    public TagController(NoteService noteService) {
        this.noteService = noteService;
    }


    /**
     * Process tags in the note text and save them to the current note.
     *
     * @param content       content of the note
     * @param note          current note
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
                String tagName = matcher.group(1);
                System.out.println("Extracted tag: " + tagName);

                if (!(note.getTags().contains(tagName))) {
                    Tag newTag = new Tag(tagName);
                    note.getTags().add(newTag);
                    universalList.add(newTag);
                    System.out.println(note.getTags());
                    System.out.println(universalList);

                }
                String link = "<a href='/tags/" + tagName + "'>#" + tagName + "</a>";
                content = content.replace("#" + tagName, link);


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
     * @param content       the entire content of the note
     * @param character     final input of the user
     * @param note          current note that we are looking at
     * @param universalList list of universal tags  across all notes for the combobox
     */
    public void checkForCorrectUserInput(String content, String character, Note note, Set<Tag> universalList) {
        if (content == null || content.trim().isEmpty()) {
            return;
        }

        String[] words = content.split("\\s+");
        if (words.length == 0) {
            return;
        }

        String lastWord = words[words.length - 1];
        if (lastWord.startsWith("#")) {
            if (character.equals(" ") || character.equals("\n")) {
                System.out.println("Space detected.");

                if (lastWord.length() > 1 && lastWord.substring(1).matches("\\w+")) {
                    System.out.println("Valid tag detected: " + lastWord);
                    initializeTags(content, note, universalList);
                }
            }
        }
    }


    /**
     * @param selectedTags  tags that have been selected from the checkcombobox by the user
     * @param notesListView listview of notes
     * @param noteList list of notes that has to be filtered
     */
    public void filterNotesByTag(Set<Tag> selectedTags, ListView<Note> notesListView, List<Note> noteList) {
        List<Note> filteredNotes = new ArrayList<>();
        for (Note note : noteList) {
            System.out.println("Checking note: " + note.getTitle());
            System.out.println("Tags: " + note.getTags());

            boolean hasAllTags = true;
            for (Tag tag : selectedTags) {
                if (!note.getTags().contains(tag)) {
                    hasAllTags = false;
                    break;
                }
            }

            if (hasAllTags) {
                System.out.println("This note contains all selected tags.");
                filteredNotes.add(note); // Add note if it contains all selected tags
            }
        }

        System.out.println("Filtered notes count: " + filteredNotes.size());

        updateNotesListView(filteredNotes, notesListView);
    }


    /**
     * @param filteredNotes notes that have been filtered on a tag
     * @param notesListView listview of notes
     */
    public void updateNotesListView(List<Note> filteredNotes, ListView<Note> notesListView) {
        notesListView.getItems().clear();
        System.out.println("CLEARED NOTES");
        notesListView.getItems().addAll(filteredNotes);
        System.out.println("NOW SHOWING: " + notesListView.getItems());
    }

    public void processNoteLinks(String content, Note note) {
        if (content == null || content.isEmpty()) return;
        Pattern pattern = Pattern.compile("\\[\\[(.*?)\\]\\]");
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            String referencedNoteTitle = matcher.group(1);
            Note referencedNote = noteService.getNoteByTitle(referencedNoteTitle);
            String replacement;
            if (referencedNote != null) {
                replacement = "<a href='/Note/" + referencedNote.getId() + "'>" + referencedNoteTitle + "</a>";
            } else {
                replacement = "<span style='color: red;'>[[ " + referencedNoteTitle + " ]] (not found)</span>";
            }
            content = content.replace("[[" + referencedNoteTitle + "]]", replacement);
        }

        note.setContent(content);


    }


    public void handleLinkClick(String link, ListView<Note> notesListView) {
        if (link.startsWith("/Note/")) {
            long noteId = Long.parseLong(link.replace("/Note/", ""));
            Note note = noteService.getNoteById(noteId);
            if (note != null) {
                loadNoteInView(note, new WebView());
            } else {
                System.out.println("Note not found for ID: " + noteId);
            }
        } else if (link.startsWith("/tags/")) {
            String tagName = link.replace("/tags/", "");
            Tag tag = new Tag(tagName);
            //filterNotesByTag(tag, notesListView); needs to be resolved
        }
    }


//ObservableList<Note> observableNotes = FXCollections.observableArrayList(filteredNotes);
//notesListView.setItems(observableNotes);

    /**
     * @param note    gives a note
     * @param webView is the webview
     */
    public void loadNoteInView(Note note, WebView webView) {
        if (note == null) {
            System.out.println("No note to be printed");
            return;
        }
        processNoteLinks(note.getContent(), note);
        webView.getEngine().loadContent(note.getContent());
        System.out.println("Loading note: " + note.getTitle());
    }

    public void updateNoteReferences(String oldTitle, String newTitle) {
        for (Note note : noteService.getNotes()) {
            String content = note.getContent();
            String updatedContent = content.replace("[[" + oldTitle + "]]", "[[" + newTitle + "]]");
            if (!content.equals(updatedContent)) {
                note.setContent(updatedContent);
                processNoteLinks(updatedContent, note); // Re-process links
            }
        }
    }
}


