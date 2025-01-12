package client.services;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Collection;

import java.util.ArrayList;
import java.util.List;

public class CollectionService{

    private final ServerUtils serverUtils;
    private List<Collection> collections = new ArrayList<>();


    @Inject
    public CollectionService(ServerUtils serverUtils) {
        this.serverUtils = serverUtils;
    }

    /**
     * Retrieves the collections from the server. If the internal list is empty,
     * fetches from the server
     * @return List of collections
     */
    public List<Collection> getCollections() {
        if(collections.isEmpty()) {
            collections = serverUtils.getCollections();
        }
        return collections;
    }

    public Collection createCollection() {
        int counter = 1;
        String uniqueTitle = "New Collection Title" + counter;
        //TODO
        while(serverUtils.isTitleCollectionDuplicate(uniqueTitle)){
            counter++;
            uniqueTitle = "New Collection Title" + counter;
        }
        Collection collection = new Collection(uniqueTitle, new ArrayList<>());
        Collection createdCollection = serverUtils.sendCollection(collection);

        if(createdCollection != null) {
            collections.add(createdCollection);
        }
        return createdCollection;
    }

    /**
     * Deletes a collection from the server and removes it from the interval
     *
     * @param collection The collection to be deleted
     * @return Status of the deletion("Successful" or "Failed")
     */
    public String deleteCollection(Collection collection) {
        String status = serverUtils.deleteCollection(collection);
        if("Successful".equals(status)) {
            collections.remove(collection);
        }
        return status;
    }

    /**
     * Refershes the internal list of collections by fetching from the server.
     */
    public void refreshCollections(){
        collections = serverUtils.getCollections();
    }
}


