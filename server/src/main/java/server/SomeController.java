package server;

import commons.Collection;
import commons.Note;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import server.database.CollectionRepository;

import java.util.LinkedList;

@Controller
@RequestMapping("/")

public class SomeController {
    private CollectionRepository db;

    public SomeController( CollectionRepository db) {
        this.db = db;
    }

    /**
     * Index method for returning text
     * @return String to print
     */
    @GetMapping("/")
    @ResponseBody
    public String index() {
        return "Hello world!";
    }

    /**
     *
     * @param name The unique name of the user
     * @return Returns a welcoming message for the user
     */
    @GetMapping("/name/{name}")
    @ResponseBody
    public String name(@PathVariable ("name") String name) {
        if(!db.existsByName(name))
        {
            var col = new Collection();
            col.setName(name);
            var listOfNotes = new LinkedList<Note>();
            col.setNotes(listOfNotes);
            db.save(col);
        }
        var sb = new StringBuilder("Welcome, ");
        sb.append(name);
        sb.append(", to the application!");
        return sb.toString();
    }
}