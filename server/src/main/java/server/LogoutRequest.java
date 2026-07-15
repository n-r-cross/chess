package server;

public record LogoutRequest(String authToken) {
    public boolean complete() {
        return (authToken != null);
    }
}
