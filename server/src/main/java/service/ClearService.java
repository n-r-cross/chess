package service;

import dataaccess.DataAccessException;

public class ClearService extends Service {
    /**
     * Clear all static Service data
     */
    public void clear() throws DataAccessException {
        userData.clear();
        authData.clear();
        gameData.clear();
    }
}
