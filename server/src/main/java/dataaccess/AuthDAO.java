package dataaccess;

import model.AuthData;

public interface AuthDAO {

    void createAuth(AuthData a);

    AuthData getAuth(String token) throws DataAccessException;

    void deleteAuth(String token) throws DataAccessException;

    void clear();
}
