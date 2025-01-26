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


    public TagController(NoteService noteService) {
        this.noteService = noteService;
        filterService = new FilterService();
        universalTags = new HashSet<>();
    }








    /**
     * @param selectedTags  tags that have been selected from the allTags button or unselected from the selectedTags button by the user
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



    /**
     * @param note    gives a note
     * @param webView is the webView
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

    /**
     *
     * @return returns universalTags
     */
    public Set<Tag> getUniversalTags() {
        return universalTags;
    }


}


