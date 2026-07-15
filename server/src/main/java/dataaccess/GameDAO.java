package dataaccess;

import model.GameData;

import java.util.List;

public interface GameDAO {
    int createGame(String gameName) throws DataAccessException;

    GameData getGame(int gameID) throws DataAccessException;

    List<GameData> listGames();

    void updateGame() throws DataAccessException;

    void clear();
}
