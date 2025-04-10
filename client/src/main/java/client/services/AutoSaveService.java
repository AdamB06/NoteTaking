package client.services;

import commons.Note;
import client.utils.ServerUtils;
import jakarta.inject.Inject;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class AutoSaveService {
    private int keyCount = 0;
    private Timer timer = null;
    private TimerTask saveTask = null;
    private String originalContent;
    private final ServerUtils serverUtils;
    private final NoteService noteService;
    private static final int KEY_THRESHOLD = 10;
    private static final long SAVE_DELAY = 2000; // 2 seconds

    /**
     *
     * @param serverUtils variable of the serverUtils
     * @param noteService variable of the noteService
     */
    @Inject
    public AutoSaveService(ServerUtils serverUtils, NoteService noteService) {
        this.serverUtils = serverUtils;
        this.noteService = noteService;
    }

    /**
     *
     * @param currentNote variable of currentNote
     * @param currentContent variable of the current content
     * @return returns a boolean value if saving took place
     */
    public boolean onKeyPressed(Note currentNote, String currentContent) {
        keyCount++;
        if (saveTask != null) {
            saveTask.cancel();
        }
        if (keyCount >= KEY_THRESHOLD) {
            keyCount = 0;
            save(currentNote, currentContent);
            return true;
        } else {
            if (timer == null) {
                timer = new Timer(true);
            }
            saveTask = new TimerTask() {
                @Override
                public void run() {
                    keyCount = 0;
                    save(currentNote, currentContent);
                }
            };
            timer.schedule(saveTask, SAVE_DELAY);
            return false;
        }
    }

    /**
     *
     * @param note variable of note
     * @param content variable of content
     */
    private void save(Note note, String content) {
        if (note != null) {
            Map<String, Object> changes = getChanges(originalContent, content);
            originalContent = content;
            System.out.println("Original content (After update): " + originalContent +"\n\n");
            String status = serverUtils.saveChanges(note.getId(), changes);
            if (!"Successful".equals(status)) {
                retrySave(note, changes);
            } else {
                noteService.refreshNotes();
            }
        }
    }

    /**
     *
     * @param original String of the original material
     * @param edited String of the edited material
     * @return returns the changes that occured
     */
    public Map<String, Object> getChanges(String original, String edited) {
        System.out.println("Get changes called");
        System.out.println("Original: " + original);
        System.out.println("Edited: " + edited);
        int startIndex = 0;
        while (startIndex < original.length() && startIndex < edited.length()
                && original.charAt(startIndex) == edited.charAt(startIndex)) {
            startIndex++;
        }
        int endIndexOriginal = original.length() - 1;
        int endIndexEdited = edited.length() - 1;

        while (endIndexOriginal >= startIndex && endIndexEdited >= startIndex
                && original.charAt(endIndexOriginal) == edited.charAt(endIndexEdited)) {
            endIndexOriginal--;
            endIndexEdited--;
        }
        String newText = edited.substring(startIndex, endIndexEdited + 1);

        Map<String, Object> changes = new HashMap<>();
        changes.put("operation", "Replace");
        changes.put("startIndex", startIndex);
        changes.put("endIndex", endIndexOriginal + 1);
        changes.put("newText", newText);
        return changes;
    }

    /**
     *
     * @param note variable of note
     * @param changes variable of the changes
     */
    public void retrySave(Note note, Map<String, Object> changes) {
        int retries = 3;
        while (retries > 0) {
            String status = serverUtils.saveChanges(note.getId(), changes);
            if ("Successful".equals(status)) {
                System.out.println("Retry successful.");
                return;
            }
            retries--;
        }
        System.err.println("All retries failed. Save aborted.");
    }

    /**
     *
     * @param content a string of the content
     */
    public void setOriginalContent(String content) {
        this.originalContent = content;
    }
}
