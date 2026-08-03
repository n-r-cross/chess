package websocket.messages;

import com.google.gson.Gson;

public record Notification(NotificationType type, String message) {
    public enum NotificationType {
        PLAYER_JOINED,
        OBSERVER_JOINED,
        MADE_MOVE,
        IN_CHECK,
        IN_CHECKMATE,
        LEFT,
        RESIGNED,
    }

    public String toString() {
        return new Gson().toJson(this);
    }
}
