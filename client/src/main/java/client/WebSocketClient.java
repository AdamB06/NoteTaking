package client;
import javax.websocket.*;
import javafx.application.Platform;
import java.net.URI;
import org.eclipse.jetty.client.HttpClient;

@ClientEndpoint
public class WebSocketClient {
    private Session session;
    private HttpClient httpClient;

    /**
     * Constructor for WebSocketClient
     */
    public WebSocketClient() {
        httpClient = new HttpClient();
        try {
            httpClient.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * On open method (Event handler for when the websocket is opened)
     * @param session the session
     */
    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        System.out.println("Connected to server");
        sendMessage("Hello from client!");
    }

    /**
     * On message method (Event handler for when a message is received)
     * @param message the message
     */
    @OnMessage
    public void onMessage(String message) {
        System.out.println("Message from server: " + message);
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
     */
    public void sendMessage(String message) {
        session.getAsyncRemote().sendText(message);
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
