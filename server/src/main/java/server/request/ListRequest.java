package server.request;

public record ListRequest(String authToken) {
    public boolean complete() {
        return authToken != null;
    }
}
