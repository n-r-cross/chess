package dataaccess;

import model.GameData;
import service.BadRequestException;

import java.util.List;

public interface GameDAO {
    /**
     * Create a new game and store it
     *
     * @param gameName name of game
     * @return gameID
     */
    int createGame(String gameName);

    /**
     * Get a game with given gameID
     *
     * @param gameID gameID to get
     * @return GameData of game with gameID
     * @throws BadRequestException if gameID is invalid
     */
    GameData getGame(int gameID) throws BadRequestException;

    /**
     * Get a list of all games
     *
     * @return a list of GameData
     */
    List<GameData> listGames();

    /**
     * Set a game to a new game object with same gameID
     *
     * @param game GameData object to replace with
     * @throws BadRequestException if gameID is invalid
     */
    void updateGame(GameData game) throws BadRequestException;

    /**
     * Clear all game data
     */
    void clear();
}
