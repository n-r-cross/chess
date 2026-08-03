package websocket.messages;

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
}
