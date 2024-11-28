package server;

import commons.Collection;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import server.database.CollectionRepository;

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

    @GetMapping("/name/{name}")
    @ResponseBody
    public String name(@PathVariable ("name") String name, @RequestParam(name = "prefix", required = false) String prefix) {
        var col = new Collection();
        col.name = name;
        db.save(col);
        var sb = new StringBuilder("Welcome ");
        if(prefix != null){
            sb.append(prefix).append(" ");
        }
        sb.append(name);
        sb.append(" to the application!");
        return sb.toString();
    }
}