package service;

import chess.ChessGame;
import model.GameData;
import server.request.CreateRequest;
import server.request.JoinRequest;
import server.result.CreateResult;
import server.result.ListResult;

public class GameService extends Service {

    /**
     * Check that the given authToken exists
     *
     * @param authToken string to check
     * @return true if authToken exists
     * @throws Exception if authToken doesn't exist
     */
    public boolean validate(String authToken) throws Exception {
        authData.getAuth(authToken);
        return true;
    }

    /**
     * List all games stored
     *
     * @return a list of GameData objects
     */
    public ListResult list() throws Exception {
        return new ListResult(gameData.listGames());
    }

    /**
     * Creates a new game with given gameName
     *
     * @param r request
     * @return CreateResult with gameID
     * @throws Exception if request is incomplete
     */
    public CreateResult create(CreateRequest r) throws Exception {
        if (!r.complete()) {
            throw new BadRequestException("bad request");
        }
        int gameID = gameData.createGame(r.gameName());
        return new CreateResult(gameID);
    }

    /**
     * Add user to game
     *
     * @param r         JoinRequest
     * @param authToken token to get username from
     * @throws Exception if request is incomplete,
     *                   if color is already taken,
     *                   or if gameID is invalid
     */
    public void join(JoinRequest r, String authToken) throws Exception {
        if (!r.complete()) {
            throw new BadRequestException("bad request");
        }
        GameData g = gameData.getGame(r.gameID());
        if (r.playerColor() == ChessGame.TeamColor.BLACK) {
            if (g.blackUsername() != null) {
                throw new ForbiddenException("already taken");
            }
        } else {
            if (g.whiteUsername() != null) {
                throw new ForbiddenException("already taken");
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
