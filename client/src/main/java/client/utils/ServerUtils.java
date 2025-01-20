/*
 * Copyright 2021 Delft University of Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package client.utils;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Collections;
import java.util.List;


import commons.Collection;
import commons.Note;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.Response;
import org.glassfish.jersey.client.ClientConfig;

import jakarta.ws.rs.ProcessingException;
import jakarta.ws.rs.client.ClientBuilder;


public class ServerUtils {

    private final String serverUrl;

    /**
     * Constructor of the ServerUtils class
     * @param serverUrl URL of the server
     */
    public ServerUtils(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    /**
     * Checks if the server is available
     *
     * @return Whether the server is available
     */
    public boolean isServerAvailable(){
        try {
            ClientBuilder.newClient(new ClientConfig()) //
                    .target(serverUrl) //
                    .request(APPLICATION_JSON) //
                    .get();
        } catch (ProcessingException e) {
            if (e.getCause() instanceof ConnectException) {
                return false;
            }
        }
        return true;
    }

    /**
     * Sends a note to the database.
     *
     * @param note note to be sent to the database
     * @return returns the note sent to the database
     */
    public Note sendNote(Note note){
        Entity<Note> entity = Entity.entity(note, APPLICATION_JSON);
        try (Client client = ClientBuilder.newClient()) {
            Response response = client.target(note.getCollectionURL() + "Note")
                    .request(APPLICATION_JSON)
                    .post(entity);
            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                Note returnedNote = response.readEntity(Note.class);
                response.close();
                Entity<Note> collectionEntity = Entity.entity(returnedNote, APPLICATION_JSON);
                Response collectionResponse = client.target(note.getCollectionURL() + "Collection/NoteAdd/" + note.getCollectionID())
                        .request(APPLICATION_JSON)
                        .put(collectionEntity);
                if (collectionResponse.getStatus() == Response.Status.OK.getStatusCode()) {
                    collectionResponse.close();
                    return returnedNote;
                }
                else {
                    System.out.println("Error: " + collectionResponse.getStatus());
                    collectionResponse.close();
                    return null;
                }
            } else {
                System.out.println("Error: " + response.getStatus());
                response.close();
                return null;
            }
        }
    }

    /**
     * Sends a collection to the database.
     * @param collection to be sent to the database
     * @return returns the collection sent to the database
     */
    public Collection sendCollection(Collection collection) {
        Entity<Collection> entity = Entity.entity(collection, APPLICATION_JSON);
        try(Client client = ClientBuilder.newClient()) {
            Response response = client.target(serverUrl + "Collection") //TODO change serverUrl to collection.getURL
                    .request(APPLICATION_JSON)
                    .post(entity);
            System.out.println("Response Status: " + response.getStatus());
            if(response.getStatus() == Response.Status.OK.getStatusCode()) {
                Collection returnedCollection = response.readEntity(Collection.class);
                response.close();
                return returnedCollection;

            }
            else{
                System.out.println("Error: " + response.getStatus());
                response.close();
                return null;
            }
        }
    }

    /**
     * @param collectionURL The URL of the collection the note is in
     * @param title title of the note
     * @return returns if the title is a duplicate
     */
    public boolean isTitleDuplicate(String collectionURL, String title) {
        try (Client client = ClientBuilder.newClient()) {
            Response response = client.target(collectionURL + "Note/checkDuplicateTitle/" +title)
                    .request(APPLICATION_JSON)
                    .get();
            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                return response.readEntity(Boolean.class);
            } else {
                System.out.println("Error: " + response.getStatus());
                return false;
            }
        }
    }

    /**
     * @param title title of the collection
     * @return returns if the title is a duplicate
     */
    public boolean isTitleCollectionDuplicate(String title) {
        try (Client client = ClientBuilder.newClient()) {
            Response response = client.target(serverUrl + "Note/checkDuplicateCollectionTitle/"
                            + title)
                    .request(APPLICATION_JSON)
                    .get();
            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                return response.readEntity(Boolean.class);
            } else {
                System.out.println("Error: " + response.getStatus());
                return false;
            }
        }
    }

    /**
     * Sends the ID of a note to be updated to the database
     * @param id ID of the note to be updated
     * @param newTitle New title for the note
     * @return The updated note
     */
    public String updateNoteTitle(long id, String newTitle) {
        try (Client client = ClientBuilder.newClient()) {
            Response response = client.target(serverUrl + "Note/" + id)
                    .request(APPLICATION_JSON)
                    .put(Entity.entity(newTitle, APPLICATION_JSON));
            if (response.getStatus() == 200) {
                return newTitle;
            } else {
                return "Error: " + response.getStatus();
            }
        }
    }

    /**
     * Sends the ID of a note to be deleted to the database
     * @param note note to be deleted from the database
     * @return returns the status of the deletion
     */
    public String deleteNote(Note note) {
        try (Client client = ClientBuilder.newClient()) {
            String ret = "Failed";
            Response response = client.target(note.getCollectionURL() + "Note/" + note.getId())//TODO make it possibly only unsubscribe
                    .request(APPLICATION_JSON)
                    .delete();
            if (response.getStatus() == 200) {
                ret = "Successful";
            }
            response.close();
            return ret;
        }
    }

    /**
     * Sends the ID of a note to be deleted to the database
     * @param collection collection to be deleted from the database
     * @return returns the status of the deletion
     */
    public String deleteCollection(Collection collection) {
        try(Client client = ClientBuilder.newClient()){
            String ret = "Failed";
            Response response = client.target(serverUrl + "Collection/" + collection.getId())//TODO change serverUrl to collection.getURL and make it only unsubscribe
                    .request(APPLICATION_JSON)
                    .delete();
            if(response.getStatus() == 200){
                ret = "Successful";
            }
            response.close();
            return ret;
        }
    }

    /**
     * Takes a map of the changes and the id of the note and sends it to the server
     * @param id id of the edited note
     * @param collectionURL The URL of the collection the note is in
     * @param changes map of the changes and their location
     * @return Success or fail
     */
    public String saveChanges(long id, String collectionURL, Map<String, Object> changes) {
        try (Client client = ClientBuilder.newClient()) {
            String ret = "Failed";
            Response response = client.target(collectionURL + "Note/" + id)
                    .request()
                    .header("X-HTTP-Method-Override", "PATCH")
                    .post(Entity.entity(changes, APPLICATION_JSON));
            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                ret = "Successful";
            }
            response.close();
            return ret;
        }
    }

    /**
     * @param collections List of collections to get the notes from
     * @return returns a list of notes
     */
    public List<Note> getNotes(List<Collection> collections) {
        List<Note> notes = new ArrayList<>();
        try (Client client = ClientBuilder.newClient()) {
            for (Collection collection : collections) {
                Response response = client.target(serverUrl + "Collection/" + collection.getId()) //TODO change serverUrl to collection.getURL
                        .request(APPLICATION_JSON)
                        .get();
                if (response.getStatus() == 200) {
                    notes.addAll(response.readEntity(Collection.class).getNotes());
                } else {
                    System.out.println("Error fetching notes: " + response.getStatus());
                    return Collections.emptyList();
                }
            }
            return notes;
        }
    }

    /**
     * @return the list of collections
     */
    public List<Collection> getCollections() { //TODO You should never need to use all collections from a server, only the ones that are designated in the config file, so this method is uselessf
        try(Client client = ClientBuilder.newClient()) {
            Response response = client.target(serverUrl + "Collection")
                    .request(APPLICATION_JSON)
                    .get();

            if(response.getStatus() == 200) {
                return response.readEntity(new GenericType<List<Collection>>() {});
            }else{
                System.out.println("Error fetching collections: " + response.getStatus());
                return Collections.emptyList();
            }
        }
    }

    /**
     * @param collectionURL The URL of the collection the note is in
     * @param noteId This is the id of the note
     * @return the note by id
     */
    public Note getNoteById(String collectionURL, long noteId){
        try (Client client = ClientBuilder.newClient()) {
            Response response = client.target(collectionURL + "Note/" + noteId)
                    .request(APPLICATION_JSON).get();
            if (response.getStatus() == 200) {
                return response.readEntity(Note.class);
            } else {
                logError("getNoteById", response);
                return null;
            }
        }
    }

    private void logError(String method, Response response) {
        System.err.println("Error in " + method + ": " + response.getStatus()
                + " " + response.getStatusInfo());
    }
}