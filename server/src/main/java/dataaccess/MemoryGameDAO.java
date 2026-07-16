package dataaccess;

import chess.ChessGame;
import model.GameData;
import service.BadRequestException;

import java.util.ArrayList;
import java.util.List;

public class MemoryGameDAO implements GameDAO {
    private final List<GameData> list = new ArrayList<>();

    @Override
    public int createGame(String gameName) {
        int gameID = list.size() + 1;
        list.add(new GameData(gameID, null, null, gameName, new ChessGame()));
        return gameID;
    }

    @Override
    public GameData getGame(int gameID) throws BadRequestException {
        if ((gameID < 1) || (gameID > (list.size()))) {
            throw new BadRequestException("bad request");
        }
        return list.get(gameID - 1);
    }

    @Override
    public List<GameData> listGames() {
        return list;
    }

    @Override
    public void updateGame(GameData game) throws BadRequestException {
        int gameID = game.gameID();
        if ((gameID < 1) || (gameID > (list.size()))) {
            throw new BadRequestException("bad request");
        }
        list.set(gameID - 1, game);
    }

    @Override
    public void clear() {
        list.clear();
    }
}
