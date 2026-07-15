package server;

public record CreateResult(int gameNumber) {
    public boolean complete() {
        return gameNumber != 0;
    }
}
