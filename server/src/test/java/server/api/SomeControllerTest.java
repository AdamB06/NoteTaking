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
        SomeController controller = new SomeController(cs, db);
        assertNotNull(controller, "The controller should not be null");
        assertEquals(db, controller.getDb(), "The repository instance should be correctly assigned");
    }
    @Test
    public void indexReturnsHelloWorld(){
        var expected = "LOCALHOST";
        var actual = sut.index();
        assertEquals(expected, actual);
    }

    @Test
    public void testNameEndPoint_NewCollection(){
        Mockito.when(db.existsByName(anyString())).thenReturn(false);

        String response = sut.name("testName");

        int number = cs.getAndIncrease();
        if(number % 10 == 1){
            assertEquals("Welcome, testName, to the application!\nYou are the " + number + "st visitor!", response);
        }
        else if(number % 10 == 2){
            assertEquals("Welcome, testName, to the application!\nYou are the " + number + "nd visitor!", response);
        }
        else if(number % 10 == 3){
            assertEquals("Welcome, testName, to the application!\nYou are the " + number + "th visitor!", response);
        }
        else{
            assertEquals("Welcome, testName, to the application!\nYou are the " + number + "th visitor!", response);
        }

        Mockito.verify(db).save(any(Collection.class));
    }

    @Test
    public void testNameEndPoint_ExistingCollection(){
        Mockito.when(db.existsByName(anyString())).thenReturn(true);

        String response = sut.name("testName");

        int number = cs.getAndIncrease();
        if(number % 10 == 1){
            assertEquals("Welcome, testName, to the application!\nYou are the " + number + "st visitor!", response);
        }
        else if(number % 10 == 2){
            assertEquals("Welcome, testName, to the application!\nYou are the " + number + "nd visitor!", response);
        }
        else if(number % 10 == 3){
            assertEquals("Welcome, testName, to the application!\nYou are the " + number + "th visitor!", response);
        }
        else{
            assertEquals("Welcome, testName, to the application!\nYou are the " + number + "th visitor!", response);
        }
        Mockito.verify(db, Mockito.never()).save(any(Collection.class));
    }
}