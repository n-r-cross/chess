package server.request;

public record LogoutRequest(String authToken) {
    public boolean complete() {
        return (authToken != null);
    }
}
