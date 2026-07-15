package service;

import dataaccess.DataAccessException;

public class ClearService extends Service {
    public void clear() throws DataAccessException {
        userData.clear();
        authData.clear();
    }
}
