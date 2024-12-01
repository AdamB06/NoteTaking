package server.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.any;
import commons.Collection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import server.CounterService;
import server.SomeController;
import server.database.CollectionRepository;

public class SomeControllerTest{
    private CollectionRepository db;
    private CounterService cs;
    private SomeController sut;

    @BeforeEach
    public void setUp(){
        db = Mockito.mock(CollectionRepository.class);
        cs = Mockito.mock(CounterService.class);
        sut = new SomeController(cs, db);
    }

    @Test
    public void testConstructor(){
        // Act: Instantiate SomeController with the mock repository
        SomeController controller = new SomeController(cs, db);

        // Assert: Verify that the controller is correctly initialized
        assertNotNull(controller, "The controller should not be null");
        assertEquals(db, controller.db, "The repository instance should be correctly assigned");
    }
    @Test
    public void indexReturnsHelloWorld(){
        var expected = "LOCALHOST";
        var actual = sut.index();
        assertEquals(expected, actual);
    }

    @Test
    public void testNameEndPoint_NewCollection(){
        //Mock behavoir: in the case when the name is NOT present in the database
        Mockito.when(db.existsByName(anyString())).thenReturn(false);

        //Calling the method name
        String response = sut.name("testName");

        //Verifying that the response returns the expected outcome
        assertEquals("Welcome, testName, to the application!\nYou are the " + cs.getAndIncrease() + "th visitor!", response);

        //Verifying that the repository's "save" method was used and called once
        Mockito.verify(db).save(any(Collection.class));
    }

    @Test
    public void testNameEndPoint_ExistingCollection(){
        //Mock behavoir: in the case when the name is present in the database
        Mockito.when(db.existsByName(anyString())).thenReturn(true);

        //Calling the method name
        String response = sut.name("testName");

        //Verifying that the response returns the expected outcome
        assertEquals("Welcome, testName, to the application!\nYou are the " + cs.getAndIncrease() + "th visitor!", response);

        //Veryfying that the repository's "save" method was not used
        Mockito.verify(db, Mockito.never()).save(any(Collection.class));
    }
}