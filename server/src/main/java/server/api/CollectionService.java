package server.api;

import commons.Collection;
import commons.Note;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import server.database.CollectionRepository;

import java.util.List;

@Service
public class CollectionService {
    private final CollectionRepository collectionRepository;

    /**
     * Constructor for CollectionService
     * @param collectionRepository The repository to interact with the database
     */
    @Autowired
    public CollectionService(CollectionRepository collectionRepository) {
        this.collectionRepository = collectionRepository;
    }

    /**
     * Saves a new collection or updates an existing one in the database
     * @param collection The new or updated collection
     * @return The saved collection including the generated id
     */
    public Collection saveCollection(Collection collection) {
        return collectionRepository.save(collection);
    }

    /**
     * Retrieves all collection from the database
     * @return A list of all collections
     */
    public List<Collection> getAllCollections() {
        return collectionRepository.findAll();
    }

    /**
     * Retrieves a collection by its id from the database
     * @param id The id of the collection to be retrieved
     * @return The retrieved collection
     */
    public Collection getCollectionById(long id) {
        return collectionRepository.getById(id);
    }

    /**
     * Deletes a collection by its id from the database
     * @param id The id of the collection to be deleted
     */
    public void deleteCollectionById(long id) {
        collectionRepository.deleteById(id);
    }

    /**
     * Adds a note to the list of the collection
     * @param id the id of the collection
     * @param note the note to be added
     */
    public void addNoteToCollection(long id, Note note) {
        Collection collection = getCollectionById(id);
        collection.getNotes().add(note);
        collectionRepository.save(collection);
    }

    /**
     * Deletes a note from the list of the collection
     * @param id the id of the collection
     * @param noteId the id of the note to be deleted
     */
    public void deleteNoteFromCollection(long id, long noteId) {
        Collection collection = getCollectionById(id);
        collection.getNotes().stream()
                .filter(note -> note.getId() == noteId)
                .findFirst()
                .ifPresent(note -> collection.getNotes().remove(note));
        collectionRepository.save(collection);
    }

    /**
     * Checks if a name is already used by a different collection in the database
     * @param name The name to be checked
     * @return Boolean of if a duplicate has been found
     */
    public boolean checkDuplicateName(String name) {
        List<Collection> collections = getAllCollections();
        for (Collection collection : collections) {
            if (collection.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }
}
