package dataaccess;

import model.GameData;

public interface GameDAO {
    void createGame() throws DataAccessException;

    GameData getGame() throws DataAccessException;

    void listGames() throws DataAccessException;

    void updateGame() throws DataAccessException;
}
