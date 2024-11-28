package server.api;

import static org.junit.jupiter.api.Assertions.assertEquals;

import commons.Collection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import server.SomeController;
import server.database.CollectionRepository;

public class SomeControllerTest{
    private CollectionRepository db;
    private SomeController sut;

    @BeforeEach
    public void setUp(){
        db = Mockito.mock(CollectionRepository.class);
        sut = new SomeController(db);
    }

    @Test
    public void indexReturnsHelloWorld(){
        var expected = "Hello world!";
        var actual = sut.index();
        assertEquals(expected, actual);
    }

    @Test
    public void testNameEndpoint_NewCollection(){
        Collection existingCollection = new Collection();
        db.save(existingCollection);

        String response = sut.name("testName");
        assertEquals("Welcome, testName, to the application!", response);
    }

    @Test
    public void testNameEndpoint_ExistingCollection(){
        Collection existingCollection = new Collection();
        existingCollection.setName("testName");
        db.save(existingCollection);

        String response = sut.name("testName");
        assertEquals("Welcome, testName, to the application!", response);
    }
}