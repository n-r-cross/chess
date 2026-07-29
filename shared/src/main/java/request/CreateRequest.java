package request;

public record CreateRequest(String gameName) {
    public boolean complete() {
        return gameName != null;
    }
}
