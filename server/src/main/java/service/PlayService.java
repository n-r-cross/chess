package service;

import model.AuthData;
import model.GameData;

public class PlayService extends Service {
    public GameData getGame(int gameID) throws Exception {
        return gameData.getGame(gameID);
    }

    public void updateGame(GameData data) throws Exception {
        gameData.updateGame(data);
    }

    public AuthData getAuth(String token) throws Exception {
        return authData.getAuth(token);
    }
}
