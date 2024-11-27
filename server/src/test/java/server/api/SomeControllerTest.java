package server.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.SomeController;

public class SomeControllerTest{
    private SomeController sut;

    @BeforeEach
    public void setUp(){
        sut = new SomeController(null);
    }

    @Test
    public void indexReturnsHelloWorld(){
        var expected = "Hello world!";
        var actual = sut.index();
        assertEquals(expected, actual);
    }
}