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
import java.util.Map;
import java.util.Collections;
import java.util.List;


import commons.Note;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.Response;
import org.glassfish.jersey.client.ClientConfig;

import jakarta.ws.rs.ProcessingException;
import jakarta.ws.rs.client.ClientBuilder;


public class ServerUtils {

    private static final String SERVER = "http://localhost:8080/";

    /**
     * Checks if the server is available
     *
     * @return Whether the server is available
     */
    public boolean isServerAvailable() {
        try {
            ClientBuilder.newClient(new ClientConfig()) //
                    .target(SERVER) //
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
    public Note sendNote(Note note) {
        Entity<Note> entity = Entity.entity(note, APPLICATION_JSON);
        try (Client client = ClientBuilder.newClient()) {
            Response response = client.target(SERVER + "Note")
                    .request(APPLICATION_JSON)
                    .post(entity);
            System.out.println("Response Status: " + response.getStatus());
            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                Note returnedNote = response.readEntity(Note.class);
                response.close();
                return returnedNote;
            } else {
                System.out.println("Error: " + response.getStatus());
                response.close();
                return null;
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
            Response response = client.target(SERVER + "Note/" + id)
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
            Response response = client.target(SERVER + "Note/" + note.getId())
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
     * Takes a map of the changes and the id of the note and sends it to the server
     * @param id id of the edited note
     * @param changes map of the changes and their location
     * @return Success or fail
     */
    public String saveChanges(long id, Map<String, Object> changes) {
        try (Client client = ClientBuilder.newClient()) {
            String ret = "Failed";
            Response response = client.target(SERVER + "Note/" + id)
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
    public List<Note> getNotes() {
        try (Client client = ClientBuilder.newClient()) {
            Response response = client.target(SERVER + "Note")
                    .request(APPLICATION_JSON)
                    .get();
            if (response.getStatus() == 200) {
                return response.readEntity(new GenericType<List<Note>>() {});
            } else {
                System.out.println("Error fetching notes: " + response.getStatus());
                return Collections.emptyList();
            }
        }
    }
}