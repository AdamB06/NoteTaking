package client;
import javax.websocket.*;

import client.scenes.HomePageCtrl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import commons.Note;
import javafx.application.Platform;
import java.net.URI;
import org.eclipse.jetty.client.HttpClient;

@ClientEndpoint
public class WebSocketClient {
    private Session session;
    private HttpClient httpClient;
    private String messageType;
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
        sendMessage("Hello from client!", "default");
    }

    /**
     * On message method (Event handler for when a message is received)
     * @param message the message
     */
    @OnMessage
    public void onMessage(String message) {
        switch(messageType) {
            case "create":
                System.out.println("Note received: " + message);
                Note note = convertToNote(message);
                hpc.incomingNote(note);
                break;
            case "delete":
                System.out.println("Note ID received: " + message);
                break;
            case "editTitle":
                System.out.println("Title received: " + message);
                break;
            case "editContent":
                System.out.println("Content received: " + message);
                break;
            default:
                System.out.println("(Other type) Message received: " + message);
        }
        System.out.println("Message from server: " + message);
    }

    public Note convertToNote(String message) {
        try {
            return om.readValue(message, Note.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
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
     * @param message the message to send
     * @param messageType the type of message
     */
    public void sendMessage(String message, String messageType) {
        this.messageType = messageType;
        if (session != null && session.isOpen()) {
            Platform.runLater(() -> {
                System.out.println("Sending message: " + message);
                session.getAsyncRemote().sendText(message);
            });
        } else {
            System.err.println("Session is null or not open");
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
