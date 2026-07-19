package dataaccess;

import model.GameData;
import service.BadRequestException;

import java.util.List;

public class SQLGameDAO implements GameDAO {
    @Override
    public int createGame(String gameName) {
        return 0;
    }

    @Override
    public GameData getGame(int gameID) throws BadRequestException {
        return null;
    }

    @Override
    public List<GameData> listGames() {
        return List.of();
    }

    @Override
    public void updateGame(GameData game) throws BadRequestException {

    }

    @Override
    public void clear() {

    }
}
