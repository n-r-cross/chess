package dataaccess;

import model.UserData;

public interface UserDAO {
    /**
     * Add UserData
     *
     * @param u UserData to add
     */
    void insertUser(UserData u);

    /**
     * Get UserData with given username
     *
     * @param username username to get
     * @return UserData with given username
     */
    UserData getUser(String username);

    /**
     * Clear all user data
     */
    void clear();
}
