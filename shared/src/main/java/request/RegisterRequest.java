package request;

public record RegisterRequest(String username, String password, String email) {
    public boolean complete() {
        return (username != null) && (password != null) && (email != null);
    }
}
