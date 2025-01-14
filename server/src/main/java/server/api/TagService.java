package server.api;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import server.database.TagRepository;

@Service
public class TagService {

    private final TagRepository tagRepository;


    /**
     * constructor for TagService
     * @param tagRepository tagRepository parameter for interacting with the database
     */
    @Autowired
    public TagService(TagRepository tagRepository){
        this.tagRepository = tagRepository;
    }



}
