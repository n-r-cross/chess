package dataaccess;

import model.AuthData;

public interface AuthDAO {

    /**
     * Stores AuthData
     *
     * @param a AuthData to add
     */
    void createAuth(AuthData a) throws Exception;

    /**
     * Get AuthData with given token
     *
     * @param token token to get
     * @return AuthData with given token
     * @throws DataAccessException if token doesn't exist
     */
    AuthData getAuth(String token) throws Exception;

    /**
     * Delete the AuthData with given token
     *
     * @param token token to delete
     * @throws DataAccessException if token doesn't exist
     */
    void deleteAuth(String token) throws DataAccessException;

    /**
     * Clear all auth data
     */
    void clear() throws Exception;
}
