package dataaccess;

import chess.ChessGame;
import model.GameData;

import java.util.ArrayList;
import java.util.List;

public class MemoryGameDAO implements GameDAO {
    private final List<GameData> list = new ArrayList<>();

    @Override
    public int createGame(String gameName) throws DataAccessException {
        System.out.println("Creating game in DAO");
        int gameID = list.size() + 1;
        list.add(new GameData(gameID, null, null, gameName, new ChessGame()));
        return gameID;
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        System.out.println("Getting game in DAO");
        if ((gameID < 1) || (gameID > (list.size()))) {
            throw new DataAccessException("unauthorized");
        }
        return list.get(gameID - 1);
    }

    @Override
    public List<GameData> listGames() {
        System.out.println("Listing games in DAO");
        return list;
    }

    @Override
    public void updateGame() throws DataAccessException {
        System.out.println("Updating game in DAO");
    }
}
