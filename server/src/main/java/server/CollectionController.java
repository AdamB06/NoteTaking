package server;

import commons.Collection;
import commons.Note;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.api.CollectionService;
import server.database.CollectionRepository;

import java.util.List;

@RestController
@RequestMapping("/my-collection/Collection")
public class CollectionController {
    private final CollectionService collectionService;
    private final CollectionRepository collectionRepository;

    /**
     * Constructor for CollectionController
     *
     * @param collectionService    The collectionService to be used
     * @param collectionRepository The collectionRepository to interact with the database
     */
    @Autowired
    public CollectionController(CollectionService collectionService,
                                CollectionRepository collectionRepository) {
        this.collectionService = collectionService;
        this.collectionRepository = collectionRepository;
    }

    /**
     * Endpoint to create a new collection
     *
     * @param collection The collection to be saved
     * @return A ResponseEntity containing the saved collection with the generated id
     */
    @PostMapping
    public ResponseEntity<Collection> createCollection(@RequestBody Collection collection) {
        if (collectionService.checkDuplicateName(collection.getName())) {
            throw new IllegalArgumentException("Collection name already exists");
        } else {
            collectionService.saveCollection(collection);
            return ResponseEntity.ok().body(collection);
        }
    }

    /**
     * Endpoint to delete a collection by id
     *
     * @param id id of the collection to be deleted
     * @return A ResponseEntity containing a message if the method was successfully executed
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCollection(@PathVariable long id) {
        collectionService.deleteCollectionById(id);
        return ResponseEntity.ok().build();
    }

    /**
     * Endpoint to edit the name of a collection
     *
     * @param name The new name
     * @param id   The id of the collection to be edited
     * @return A ResponseEntity containing the new edited collection
     */
    @PutMapping("/{id}")
    public ResponseEntity<Collection> editCollectionName(
            @RequestBody String name, @PathVariable long id) {
        Collection collection = collectionService.getCollectionById(id);
        if (collectionService.checkDuplicateName(collection.getName())) {
            throw new IllegalArgumentException("Collection name already exists");
        } else {
            collection.setName(name);
            collection = collectionService.saveCollection(collection);
            return ResponseEntity.ok(collection);
        }
    }

    /**
     * Endpoint to get all the saved collections
     *
     * @return A ResponseEntity containing a list of all saved collections
     */
    @GetMapping
    public ResponseEntity<List<Collection>> getAllCollections() {
        List<Collection> collections = collectionService.getAllCollections();
        return ResponseEntity.ok(collections);
    }

    /**
     * Endpoint to getting a collection by id
     *
     * @param id id of the collection to get
     * @return A ResponseEntity containing the collection
     */
    @GetMapping("/{id}")
    public ResponseEntity<Collection> getCollectionById(long id) {
        Collection collection = collectionService.getCollectionById(id);
        return ResponseEntity.ok(collection);
    }

    /**
     * Endpoint to add a note to a collection
     *
     * @param id The id of the collection
     * @param note The Note to be added
     * @return A ResponseEntity containing a message if the method was successfully executed
     */
    @PutMapping("/NoteAdd/{id}")
    public ResponseEntity<Void> addNote(@PathVariable long id, @RequestBody Note note) {
        collectionService.addNoteToCollection(id, note);
        return ResponseEntity.ok().build();
    }

    /**
     * Endpoint to delete a note from a collection
     *
     * @param collectionId The id of the collection
     * @param noteId The id of the Note to be deleted
     * @return A ResponseEntity containing a message if the method was successfully executed
     */
    @DeleteMapping("/NoteDelete/{collectionId}/{noteId}")
    public ResponseEntity<Void> deleteNote(@PathVariable long collectionId,
                                           @PathVariable long noteId) {
        collectionService.deleteNoteFromCollection(collectionId, noteId);
        return ResponseEntity.ok().build();
    }
}
