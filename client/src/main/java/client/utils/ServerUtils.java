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

import java.io.IOException;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;


import com.fasterxml.jackson.databind.ObjectMapper;
import commons.Note;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.Response;
import org.glassfish.jersey.client.ClientConfig;

import jakarta.ws.rs.ProcessingException;
import jakarta.ws.rs.client.ClientBuilder;


public class ServerUtils {

    private static final String SERVER = "http://localhost:8080/";

    /**
     * Checks if the server is available
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
     *
     * @param note note to be added in the database
     */
    public void sendNote(Note note) {
        Entity<Note> entity = Entity.entity(note, APPLICATION_JSON);
        try (Client client = ClientBuilder.newClient()) {
            Response response = client.target(SERVER + "Note/")
                    .request(APPLICATION_JSON)
                    .post(entity);
            System.out.println(response.getStatus());
            response.close();
        }
    }

    public void deleteNote(Note note) {
        try (Client client = ClientBuilder.newClient()) {
            Response response = client.target(SERVER + "Note/" + note.getId())
                    .request(APPLICATION_JSON)
                    .delete();
            System.out.println(response.getStatus());
            response.close();
        }
    }
}