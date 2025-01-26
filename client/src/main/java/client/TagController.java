package client;

import client.services.FilterService;
import client.services.NoteService;
import commons.Note;
import commons.Tag;

import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.web.WebView;


import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


public class TagController {
    private final NoteService noteService;
    private final FilterService filterService;
    private static  Set<Tag> universalTags = new HashSet<>();


    /**
     *
     * @param noteService noteservice Object for certain methods
     */
    public TagController(NoteService noteService) {
        this.noteService = noteService;
        filterService = new FilterService();
        universalTags = new HashSet<>();
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
    public void checkForCorrectUserInput(String content, String character,
                                         Note note, Set<Tag> universalList) {
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
     * @param selectedTags  tags that have been selected from the allTags button or unselected from the selectedTags button by the user
     * @param notesListView listview of notes
     * @param noteList list of notes that has to be filtered
     */
    public void filterNotesByTag(Set<Tag> selectedTags,
                                 ListView<Note> notesListView, List<Note> noteList) {
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
                filteredNotes.add(note);
            }
        }

        System.out.println("Filtered notes count: " + filteredNotes.size());
        filterService.setFilteredNotes(filteredNotes);
        updateNotesListView(filteredNotes, notesListView);
    }


    /**
     *
     * @param tag tag
     * @param allTags tags in allTags button
     * @param selectedTags tags of the selectedTags button
     * @param noteListView listview of notes
     * @return returns the menuItem that has been clicked
     */
    public MenuItem createMenuItemForAllTags(Tag tag, SplitMenuButton allTags, SplitMenuButton selectedTags, ListView<Note> noteListView) {
        MenuItem menuItem = new MenuItem(tag.getName());
        menuItem.setOnAction(event -> {

            allTags.getItems().remove(menuItem);
            selectedTags.getItems().add(createMenuItemForSelectedTags(tag, allTags, selectedTags, noteListView));

            // this is because selectedTagSet.getItems() does not work so we need a proper Set
            Set<Tag> selectedTagSet = selectedTags.getItems().stream()
                    .map(menuItem1 -> new Tag(menuItem1.getText()))
                    .collect(Collectors.toSet());


            filterNotesByTag(selectedTagSet, noteListView, noteService.getNotes());
        });
        return menuItem;
    }

    /**
     *
     * @param tag tag
     * @param allTags tags in allTags button
     * @param selectedTags tags of the selectedTags button
     * @param noteListView listview of notes
     * @return returns the menuItem that has been clicked
     */
    public MenuItem createMenuItemForSelectedTags(Tag tag, SplitMenuButton allTags, SplitMenuButton selectedTags, ListView<Note> noteListView) {
        MenuItem menuItem = new MenuItem(tag.getName());
        menuItem.setOnAction(event -> {
            selectedTags.getItems().remove(menuItem);
            allTags.getItems().add(createMenuItemForAllTags(tag, allTags, selectedTags, noteListView));


            Set<Tag> selectedTagSet = selectedTags.getItems().stream()
                    .map(menuItem1 -> new Tag(menuItem1.getText()))
                    .collect(Collectors.toSet());

            // Trigger filtering again because if multiple are selected and one is removed, it needs to filter again
            filterNotesByTag(selectedTagSet, noteListView, noteService.getNotes());

        });
        return menuItem;
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

    /**
     * Processes the note content by converting tag-like text to HTML links
     * and handling references to other notes.
     *
     * @param content String representation of content
     * @return returns the content with processed links and references
     */
    public String processNoteLinks(String content) {
        if (content == null || content.isEmpty()) return content;

        String[] words = content.split("(?<=\\s)|(?=\\s)");

        StringBuilder updatedContent = new StringBuilder();

        for (String word : words) {
            if (word.startsWith("#") && word.length() > 1) {
                String link = String.format("<a href='#' onclick='alert(\"/Tag/%s\"); return false;'>%s</a>",
                        word.substring(1), word);
                updatedContent.append(link);
            } else {
                updatedContent.append(word);
            }
        }
        content = updatedContent.toString();

        Pattern pattern = Pattern.compile("\\[\\[(.*?)\\]\\]");
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            String referencedNoteTitle = matcher.group(1);
            Note referencedNote = noteService.getNoteByTitle(referencedNoteTitle);

            String replacement;
            if (referencedNote != null) {
                replacement = String.format(
                        "<a href='#' onclick='alert(\"/Note/%d\"); return false;'>%s</a>",
                        referencedNote.getId(),
                        referencedNoteTitle
                );
            } else {
                replacement = String.format(
                        "<span style='color: red;'>[[%s]] (not found)</span>",
                        referencedNoteTitle
                );
            }
            content = content.replace("[[" + referencedNoteTitle + "]]", replacement);
        }
        return content;
    }






    /**
     * Handles the click event on a link, either navigating to a note or adding/removing a tag.
     *
     * @param link         representation of the link
     * @param notesListView Object called notesListView
     * @param allTags      Object called allTags
     * @param selectedTags Object called selectedTags
     */
    public void handleLinkClick(String link, ListView<Note> notesListView, SplitMenuButton allTags, SplitMenuButton selectedTags) {
        if (link.startsWith("/Note/")) {
            long noteId = Long.parseLong(link.replace("/Note/", ""));
            Note note = noteService.getNoteById(noteId);
            if (note != null) {
                int noteIndex = noteService.findNoteIndex(note, notesListView.getItems());
                notesListView.getSelectionModel().select(noteIndex);
            } else {
                System.out.println("Note not found for ID: " + noteId);
            }
        }
        else if (link.startsWith("/Tag/")) {
            String tagName = link.replace("/Tag/", "");
            Tag tag = new Tag(tagName);
            MenuItem menuItem = new MenuItem(tag.getName());
            Set<Tag> selectedTagSet = selectedTags.getItems().stream()
                    .map(menuItem1 -> new Tag(menuItem1.getText()))
                    .collect(Collectors.toSet());
            if(!selectedTagSet.contains(tag)){
                selectedTagSet.add(tag);
                allTags.getItems().remove(menuItem);
                System.out.println("all tags: " + allTags.getItems());
                selectedTags.getItems().add(menuItem);
                filterNotesByTag(selectedTagSet, notesListView, noteService.getNotes());
            }
        }
    }

    /**
     *
     * @param input content of the note
     * @return returns the tags (words that start with #)
     */
    public Set<Tag> getTags(String input){
        Set<Tag> tags = new HashSet<Tag>();
        Pattern pattern = Pattern.compile("#(\\w+)");
        Matcher matcher = pattern.matcher(input);
        while (matcher.find()) {
            tags.add(new Tag(matcher.group(1)));
            universalTags.add(new Tag(matcher.group(1)));
        }

        return tags;
    }

    public boolean isTagInAnyNote(String tagName) {
        // Fetch notes from noteService to ensure you get the actual list of notes
        return noteService.getNotes().stream()
                .flatMap(note -> note.getTags().stream())
                .anyMatch(tag -> tag.getName().equals(tagName));
    }


    /**
     * @param note    gives a note
     * @param webView is the webView
     */
    public void loadNoteInView(Note note, WebView webView) {
        if (note == null) {
            System.out.println("No note to be printed");
            return;
        }
        webView.getEngine().loadContent(processNoteLinks(note.getContent()));
        System.out.println("Loading note: " + note.getTitle());
    }



    /**
     *
     * @return returns universalTags
     */
    public Set<Tag> getUniversalTags() {
        return universalTags;
    }


}