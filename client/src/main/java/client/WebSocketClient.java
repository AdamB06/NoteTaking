package client;
import javax.websocket.*;

import client.scenes.HomePageCtrl;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import commons.Note;
import javafx.application.Platform;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.jetty.client.HttpClient;

@ClientEndpoint
public class WebSocketClient {
    private Session session;
    private HttpClient httpClient;
    private ObjectMapper om;
    private HomePageCtrl hpc;

    /**
     * Constructor for WebSocketClient
     */
    @Inject
    public WebSocketClient() {
        httpClient = new HttpClient();
        om = new ObjectMapper();
        try {
            httpClient.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Set the home page controller
     * @param hpc the home page controller
     */
    public void setHomePageCtrl(HomePageCtrl hpc) {
        this.hpc = hpc;
    }

    /**
     * On open method (Event handler for when the websocket is opened)
     * @param session the session
     */
    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        System.out.println("Connected to server");
    }

    /**
     * On message method (Event handler for when a message is received)
     * @param message the message
     */
    @OnMessage
    public void onMessage(String message) {
        Platform.runLater(() -> {
            try {
                // Parse the JSON payload
                Map<String, Object> payload = om.readValue(message, new TypeReference<>() {});
                String type = (String) payload.get("type");
                Note note = om.convertValue(payload.get("note"), Note.class);

                // Process based on message type
                switch (type) {
                    case "create":
                        hpc.incomingNote(note);
                        break;
                    case "delete":
                        System.out.println("Note deletion received: " + note);
                        hpc.incomingDeletion(note);
                        break;
                    case "updateTitle":
                        System.out.println("Note title update received: " + note);
                        hpc.incomingTitleUpdate(note);
                        break;
                    case "updateContent":
                        hpc.incomingContentUpdate(note);
                        break;
                    default:
                        System.out.println("(Other type) Message received: " + message);
                }
            } catch (Exception e) {
                System.err.println("Error processing message: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }

    /**
     * On close method (Event handler for when the websocket is closed)
     * @param session the session
     * @param reason the reason
     */
    @OnClose
    public void onClose(Session session, CloseReason reason) {
        System.out.println("Disconnected: " + reason.getReasonPhrase());
        Platform.exit();
    }

    /**
     * On error method (Event handler for when an error occurs)
     * @param session the session
     * @param throwable the throwable (error)
     */
    @OnError
    public void onError(Session session, Throwable throwable) {
        System.err.println("Error: " + throwable.getMessage());
    }

    /**
     * Send a message to the server
     * @param note the note to send
     * @param messageType the type of message
     */
    public void sendMessage(Note note, String messageType) {
        try {
            Map<String, Object> payload = new HashMap<>();
            payload.put("type", messageType);
            payload.put("note", note);

            String jsonPayload = om.writeValueAsString(payload);
            if (session != null && session.isOpen()) {
                session.getAsyncRemote().sendText(jsonPayload);
            } else {
                System.err.println("Session is not open or null.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Connect to the server
     */
    public void connect() {
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        String uri = "ws://localhost:8080/chat";
        try {
            container.connectToServer(this, URI.create(uri));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Close the connection
     */
    public void closeConnection() {
        if (session != null && session.isOpen()) {
            try {
                session.close(new CloseReason(CloseReason.CloseCodes.NORMAL_CLOSURE,
                        "Client closed"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // Shut down the HTTP client
        if (httpClient != null) {
            try {
                httpClient.stop();
                System.out.println("HttpClient stopped.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
