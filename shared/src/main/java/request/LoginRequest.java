package request;

public record LoginRequest(String username, String password) {
    public boolean complete() {
        return (username != null) && (password != null);
    }
}
