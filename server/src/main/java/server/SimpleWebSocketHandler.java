package server;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.TextMessage;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class SimpleWebSocketHandler extends TextWebSocketHandler {
    private static final Set<WebSocketSession> sessions = Collections
            .synchronizedSet(new HashSet<>());
    /**
     * Handle a text message received from a websocket session.
     * @param session the websocket session
     * @param message the message received
     * @throws Exception if the message cannot be handled
     */
    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        System.out.println("Received from session " + session.getId() + ": "
                + message.getPayload());
        broadcastMessage(message);
    }

    /**
     * Broadcast a message to all connected sessions.
     * @param message the message to broadcast
     */
    private void broadcastMessage(TextMessage message) {
        synchronized (sessions) {
            for (WebSocketSession session : sessions) {
                if (session.isOpen()) {
                    try {
                        System.out.println("Broadcasting to session: " + session.getId());
                        session.sendMessage(message);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * Handle a new websocket connection.
     * @param session the websocket session
     * @throws Exception if the connection cannot be established
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session);
        System.out.println("New session connected: " + session.getId());
        System.out.println("Total connected sessions: " + sessions.size());
    }

    /**
     * Handle a websocket connection closing.
     * @param session the websocket session
     * @param status the status of the connection
     * @throws Exception if the connection cannot be closed
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session,
                                      CloseStatus status) throws Exception {
        sessions.remove(session); // Remove session
        System.out.println("Session disconnected: " + session.getId());
        System.out.println("Total connected sessions: " + sessions.size());
    }


}