package service;

import model.GameData;

public class PlayService extends Service {
    public GameData get(int gameID) throws Exception {
        return gameData.getGame(gameID);
    }

    public void update(GameData data) throws Exception {
        gameData.updateGame(data);
    }
}
