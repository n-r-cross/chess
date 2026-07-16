package dataaccess;

import model.GameData;
import service.BadRequestException;

import java.util.List;

public interface GameDAO {
    int createGame(String gameName);

    GameData getGame(int gameID) throws BadRequestException;

    List<GameData> listGames();

    void updateGame(GameData game) throws BadRequestException;

    void clear();
}
