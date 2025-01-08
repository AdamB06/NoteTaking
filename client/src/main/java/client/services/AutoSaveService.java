package client.services;

import commons.Note;
import client.utils.ServerUtils;

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
    private static final long SAVE_DELAY = 5000; // 5 seconds

    public AutoSaveService(ServerUtils serverUtils, NoteService noteService) {
        this.serverUtils = serverUtils;
        this.noteService = noteService;
    }

    public void onKeyPressed(Note currentNote, String currentContent) {
        keyCount++;
        if (saveTask != null) {
            saveTask.cancel();
        }

        if (keyCount >= KEY_THRESHOLD) {
            keyCount = 0;
            save(currentNote, currentContent);
        } else {
            timer = new Timer(true);
            saveTask = new TimerTask() {
                @Override
                public void run() {
                    keyCount = 0;
                    save(currentNote, currentContent);
                }
            };
            timer.schedule(saveTask, SAVE_DELAY);
        }
    }

    private void save(Note note, String content) {
        if (note != null) {
            Map<String, Object> changes = getChanges(originalContent, content);
            originalContent = content;
            String status = serverUtils.saveChanges(note.getId(), changes);
            if (!"Successful".equals(status)) {
                retrySave(note, changes);
            } else {
                noteService.refreshNotes();
            }
        }
    }

    public Map<String, Object> getChanges(String original, String edited) {
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

    public void setOriginalContent(String content) {
        this.originalContent = content;
    }
}
