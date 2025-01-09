package server;

import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.TextMessage;

public class SimpleWebSocketHandler extends TextWebSocketHandler {
    /**
     * Handle a text message received from a websocket session.
     * @param session the websocket session
     * @param message the message received
     * @throws Exception if the message cannot be handled
     */
    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        System.out.println("Received: " + message.getPayload());
        session.sendMessage(new TextMessage("Server Echo: " + message.getPayload()));
    }
}