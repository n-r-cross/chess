package server;

public record ListRequest(String authToken) {
    public boolean complete() {
        return authToken != null;
    }
}
