package dataaccess;

import model.UserData;
import service.ForbiddenException;

public interface UserDAO {
    /**
     * Add UserData
     *
     * @param u UserData to add
     */
    void insertUser(UserData u) throws Exception;

    /**
     * Get UserData with given username
     *
     * @param username username to get
     * @return UserData with given username
     */
    UserData getUser(String username) throws DataAccessException;

    /**
     * Clear all user data
     */
    void clear() throws Exception;
}
