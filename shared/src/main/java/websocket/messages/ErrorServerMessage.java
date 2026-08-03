package websocket.messages;

import com.google.gson.Gson;

public class ErrorServerMessage extends ServerMessage {
    private String errorMessage;

    public ErrorServerMessage(String errorMessage) {
        super(ServerMessageType.ERROR);
        this.errorMessage = errorMessage;
    }

    public String getMessage() {
        return errorMessage;
    }

    public void setMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
