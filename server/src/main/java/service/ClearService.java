package service;

public class ClearService extends Service {
    /**
     * Clear all static Service data
     */
    public void clear() {
        userData.clear();
        authData.clear();
        gameData.clear();
    }
}
