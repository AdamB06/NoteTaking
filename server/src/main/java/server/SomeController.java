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
    private CounterService counterService;
    public CollectionRepository db;

    /**
     * @param counterService is the counter that keeps track which visitor this is
     * @param db is the CollectionRepository, where all our Collections will be stored
     */
    public SomeController(CounterService counterService, CollectionRepository db){
        this.counterService = counterService;
        this.db = db;
    }

    /**
     * Index method for returning text
     * @return String to print
     */
    @GetMapping("/")
    @ResponseBody
    public String index() {
        return "LOCALHOST";
    }

    /**
     *
     * @param name The unique name of the user
     * @return Returns a welcoming message for the user
     */
    @GetMapping("/name/{name}")
    @ResponseBody
    public String name(@PathVariable ("name") String name) {
        if(!db.existsByName(name)){
            Collection col;
            col = new Collection();
            var listOfNotes = new LinkedList<Note>();
            col.setNotes(listOfNotes);
            db.save(col);
        }
        StringBuilder sb;
        sb = new StringBuilder("Welcome, ");
        sb.append(name);
        sb.append(", to the application!\n");
        int number = counterService.getAndIncrease();
        if(number % 10 == 1){
            sb.append("You are the " + number + "st visitor!");
        }
        else if(number % 10 == 2){
            sb.append("You are the " + number + "nd visitor!");
        }
        else if(number % 10 == 3){
            sb.append("You are the " + number + "rd visitor!");
        }
        else{
            sb.append("You are the " + number + "th visitor!");
        }
        return sb.toString();
    }
}