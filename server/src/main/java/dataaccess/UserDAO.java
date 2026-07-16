package dataaccess;

import model.UserData;

public interface UserDAO {
    /**/
    void insertUser(UserData u);

    UserData getUser(String username);

    void clear();
}
