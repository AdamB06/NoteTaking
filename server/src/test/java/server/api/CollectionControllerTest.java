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
        Note n1 = new Note("title", "content", 0, "");
        Note n2 = new Note("title2", "content2", 0, "");
        Collection c = new Collection("name", List.of(n1, n2), "id");

        testController.createCollection(c);

        assertEquals(c, testRepository.findAll().stream().filter(collection -> collection.getId().equals("id")).findFirst().get());
    }

    @Test
    public void testRemoveCollection(){
        Note n1 = new Note("title", "content", 0, "");
        Note n2 = new Note("title2", "content2", 0, "");
        Collection c = new Collection("name", List.of(n1, n2), "id");

        testController.createCollection(c);
        assertEquals(c, testRepository.findAll().stream().filter(collection -> collection.getId().equals("id")).findFirst().get());

        testController.deleteCollection(c.getId());
        assertNotEquals(c, testRepository.findAll().stream().filter(collection -> collection.getId().equals("id")).findFirst().get());
    }

    @Test
    public void testEditCollection(){
        Note n1 = new Note("title", "content", 0, "");
        Note n2 = new Note("title2", "content2", 0, "");
        Collection c = new Collection("name", List.of(n1, n2), "id");

        testController.createCollection(c);
        assertEquals(c, testRepository.findAll().stream().filter(collection -> collection.getId().equals("id")).findFirst().get());

        testController.editCollectionName("new name", c.getId());
        assertEquals("new name", testRepository.findAll().stream().filter(collection -> collection.getId().equals("id")).findFirst().get().getName());
    }
}
