package service;

import chess.ChessGame;
import model.GameData;
import server.*;

public class GameService extends Service {

    public boolean validate(String authToken) throws Exception {
        authData.getAuth(authToken);
        return true;
    }

    public ListResult list(ListRequest r) throws Exception {
        if (!r.complete()) {
            throw new BadRequestException("bad request");
        }
        return new ListResult(gameData.listGames());
    }

    public CreateResult create(CreateRequest r) throws Exception {
        if (!r.complete()) {
            throw new BadRequestException("bad request");
        }
        int gameID = gameData.createGame(r.gameName());
        return new CreateResult(gameID);
    }

    public void join(JoinRequest r, String authToken) throws Exception {
        if (!r.complete()) {
            throw new BadRequestException("bad request");
        }
        GameData g = gameData.getGame(r.gameID());
        if (r.playerColor() == ChessGame.TeamColor.BLACK) {
            if (g.blackUsername() != null) {
                throw new ForbiddenException("Forbidden");
            }
        } else {
            if (g.whiteUsername() != null) {
                throw new ForbiddenException("Forbidden");
            }
        }
        String username = Service.authData.getAuth(authToken).username();
        // Update game to have player join game
        if (r.playerColor() == ChessGame.TeamColor.BLACK) {
            gameData.updateGame(new GameData(g.gameID(), g.whiteUsername(), username, g.gameName(), g.game()));
        } else {
            gameData.updateGame(new GameData(g.gameID(), username, g.blackUsername(), g.gameName(), g.game()));
        }

    }
}
