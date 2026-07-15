package dataaccess;

import model.GameData;

public class MemoryGameDAO implements GameDAO {
    @Override
    public void createGame() throws DataAccessException {
        System.out.println("Creating game in DAO");
    }

    @Override
    public GameData getGame() throws DataAccessException {
        System.out.println("Getting game in DAO");
        return null;
    }

    @Override
    public void listGames() throws DataAccessException {
        System.out.println("Listing games in DAO");
    }

    @Override
    public void updateGame() throws DataAccessException {
        System.out.println("Updating game in DAO");
    }
}
