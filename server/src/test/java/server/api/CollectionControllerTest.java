package server.api;

import commons.Collection;
import commons.Note;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.CollectionController;
import server.database.CollectionRepository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class CollectionControllerTest {

    CollectionRepository testRepository;
    CollectionController testController;

    @BeforeEach
    public void init(){
        testRepository = new TestCollectionRepository();
        testController = new CollectionController(new CollectionService(testRepository), testRepository);
    }

    @Test
    public void testCreateCollection(){
        Note n1 = new Note("title", "content");
        Note n2 = new Note("title2", "content2");
        Collection c = new Collection("name", List.of(n1, n2));

        testController.createCollection(c);

        assertEquals(c, testRepository.getById(c.getId()));
    }

    @Test
    public void testRemoveCollection(){
        Note n1 = new Note("title", "content");
        Note n2 = new Note("title2", "content2");
        Collection c = new Collection("name", List.of(n1, n2));

        testController.createCollection(c);
        assertEquals(c, testRepository.getById(c.getId()));

        testController.deleteCollection(c.getId());
        assertNotEquals(c, testRepository.getOne(c.getId()));
    }

    @Test
    public void testEditCollection(){
        Note n1 = new Note("title", "content");
        Note n2 = new Note("title2", "content2");
        Collection c = new Collection("name", List.of(n1, n2));

        testController.createCollection(c);
        assertEquals(c, testRepository.getById(c.getId()));

        testController.editCollectionName("new name", c.getId());
        assertEquals("new name", testRepository.getById(c.getId()).getName());
    }
}
