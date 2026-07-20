package service;

public class ClearService extends Service {
    /**
     * Clear all static Service data
     */
    public void clear() {
        try {
            userData.clear();
            authData.clear();
            gameData.clear();
        } catch (Exception e) {
            throw new RuntimeException("Clear failed!");
        }
    }
}
