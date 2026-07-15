package dataaccess;

import chess.ChessGame;
import model.GameData;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MemoryGameDAO implements GameDAO {
    private final List<GameData> list = new ArrayList<>();

    @Override
    public int createGame(String gameName) throws DataAccessException {
        int gameID = list.size() + 1;
        list.add(new GameData(gameID, null, null, gameName, new ChessGame()));
        return gameID;
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        if ((gameID < 1) || (gameID > (list.size()))) {
            throw new DataAccessException("unauthorized");
        }
        return list.get(gameID - 1);
    }

    @Override
    public List<GameData> listGames() {
        return list;
    }

    @Override
    public void updateGame() throws DataAccessException {
        System.out.println("Updating game in DAO");
    }

    @Override
    public void clear() {
        list.clear();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MemoryGameDAO that = (MemoryGameDAO) o;
        return Objects.equals(list, that.list);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(list);
    }
}
