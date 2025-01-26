import client.services.NoteService;
import client.utils.ServerUtils;
import commons.Note;
import commons.Tag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class NoteServiceTest {

    private NoteService noteService;

    @BeforeEach
    public void setUp() {
        noteService = new NoteService(new ServerUtils(""));
    }

    @Test
    public void getNoteByTitleTest() {
        Note note1 = new Note("Note1", "Note1 content #1", 1);
        Note note2 = new Note("Note2", "Note2 content #2", 2);
        List<Note> notes = new ArrayList<>();
        notes.add(note1);
        notes.add(note2);
        noteService.setNotes(notes);
        assertEquals(note1, noteService.getNoteByTitle("Note1"));
    }

    @Test
    public void getNotesByTitleEmptyTest() {
        Note note1 = new Note("Note1", "Note1 content #1", 1);
        Note note2 = new Note("Note2", "Note2 content #2", 2);
        List<Note> notes = new ArrayList<>();
        notes.add(note1);
        notes.add(note2);
        noteService.setNotes(notes);
        assertNull(noteService.getNoteByTitle(""));
    }

    @Test
    public void getNotesByTitleNullTest() {
        Note note1 = new Note("Note1", "Note1 content #1", 1);
        Note note2 = new Note("Note2", "Note2 content #2", 2);
        List<Note> notes = new ArrayList<>();
        notes.add(note1);
        notes.add(note2);
        noteService.setNotes(notes);
        assertNull(noteService.getNoteByTitle(null));
    }

    @Test
    public void findNoteIndexTest() {
        Note note1 = new Note("Note1", "Note1 content #1", 1);
        Note note2 = new Note("Note2", "Note2 content #2", 2);
        List<Note> notes = new ArrayList<>();
        notes.add(note1);
        notes.add(note2);
        noteService.setNotes(notes);
        assertEquals(1, noteService.findNoteIndex(note2, notes));
    }

    @Test
    public void findNoteIndexNotThereTest() {
        Note note1 = new Note("Note1", "Note1 content #1",1);
        Note note2 = new Note("Note2", "Note2 content #2", 2);
        Note note3 = new Note("Note3", "Note3 content #3",3);
        List<Note> notes = new ArrayList<>();
        notes.add(note1);
        notes.add(note2);
        noteService.setNotes(notes);
        assertEquals(-1, noteService.findNoteIndex(note3, notes));
    }

//    @Test
//    public void setTagsTest() {
//        Note note1 = new Note("Note1", "Note1 content #1", 1);
//        Note note2 = new Note("Note2", "Note2 content #2",2);
//        List<Note> notes = new ArrayList<>();
//        notes.add(note1);
//        notes.add(note2);
//        notes = noteService.setTags(notes);
//        Set<Tag> tags = new HashSet<>();
//        tags.add(new Tag("1"));
//        assertEquals(tags, notes.get(0).getTags());
//        tags.clear();
//        tags.add(new Tag("2"));
//        assertEquals(tags, notes.get(1).getTags());
//    }

//    @Test
//    public void getTagsTest() {
//        Note note1 = new Note("Note1", "Note1 content #1", 1);
//        Note note2 = new Note("Note2", "Note2 content #2", 2);
//        Note note3 = new Note("Note3", "Note3 content #3", 3);
//        List<Note> notes = new ArrayList<>();
//        notes.add(note1);
//        notes.add(note2);
//        noteService.setNotes(notes);
//        Set<Tag> tags = new HashSet<>();
//        tags.add(new Tag("1"));
//        assertEquals(tags, noteService.getTags(note1.getContent()));
//        tags.clear();
//        tags.add(new Tag("2"));
//        assertEquals(tags, noteService.getTags(note2.getContent()));
//        tags.clear();
//        tags.add(new Tag("3"));
//        assertEquals(tags, noteService.getTags(note3.getContent()));
//    }
}
