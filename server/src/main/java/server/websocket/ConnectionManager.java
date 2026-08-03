package server.websocket;

import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.Notification;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    public final ConcurrentHashMap<Integer, ConcurrentHashMap<Session, Session>> map = new ConcurrentHashMap<>();

    public void add(int gameID, Session session) {
        ConcurrentHashMap<Session, Session> participants = new ConcurrentHashMap<>(map.get(gameID));
        participants.put(session, session);
        map.put(gameID, participants);
    }

    public void remove(Session session) {
        for (Map.Entry<Integer, ConcurrentHashMap<Session, Session>> entry : map.entrySet()) {
            entry.getValue().remove(session);
            entry.setValue(entry.getValue());
        }
    }

    public void broadcast(int gameID, Notification notification) {
        String message = notification.toString();

        for (Map.Entry<Session, Session> entry : map.get(gameID).entrySet()) {
            entry.setValue(entry.getValue());
            if (entry.getValue().isOpen()) {
                try {
                    entry.getValue().getRemote().sendString(message);
                } catch (IOException e) {
                    System.out.println("Notification was not sent!");
                }
            }
        }
    }
}
