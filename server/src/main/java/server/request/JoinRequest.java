package server.request;

import chess.ChessGame;

public record JoinRequest(ChessGame.TeamColor playerColor, int gameID) {
    public boolean complete() {
        return (playerColor != null);
    }
}
