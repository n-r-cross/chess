package websocket.messages;

import com.google.gson.Gson;

public class NotificationServerMessage extends ServerMessage {
    private String message;

    public NotificationServerMessage(String message) {
        super(ServerMessageType.NOTIFICATION);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
