package dataaccess;


import model.UserData;

import java.util.ArrayList;
import java.util.List;

public class MemoryUserDAO implements UserDAO {
    List<UserData> list = new ArrayList<>();

    @Override
    public void insertUser(UserData u) throws DataAccessException {
        list.add(u);
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        for (UserData i : list) {
            if (i.username().equals(username)) {
                System.out.println("Got user:");
                System.out.println(i);
                return i;
            }
        }
        return null;
    }

    @Override
    public void deleteUser(String username) throws DataAccessException {

    }
}
