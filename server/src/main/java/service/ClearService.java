package service;

public class ClearService extends Service {
    /**
     * Clear all static Service data
     */
    public void clear() throws Exception {
        try {
            userData.clear();
            authData.clear();
            gameData.clear();
        } catch (Exception e) {
            throw new Exception("Clear failed!");
        }
    }
}
