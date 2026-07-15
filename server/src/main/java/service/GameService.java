package service;

import server.*;

import java.util.ArrayList;

public class GameService extends Service {

    public boolean validate(String authToken) throws Exception {
        authData.getAuth(authToken);
        return true;
    }

    public ListResult list(ListRequest r) throws Exception {
        if (!r.complete()) {
            throw new BadRequestException("bad request");
        }
        // TODO: create actual ListResult with games
        gameData.listGames();
        return new ListResult(new ArrayList<String>());
    }

    public CreateResult create(CreateRequest r) throws Exception {
        if (!r.complete()) {
            throw new BadRequestException("bad request");
        }
        // TODO: create new Game
        gameData.createGame();
        return new CreateResult(0);
    }

    public void join(JoinRequest r) throws Exception {
        if (!r.complete()) {
            throw new BadRequestException("bad request");
        }
        // TODO: join game
        gameData.updateGame();
    }
}
