package dataaccess;


import model.UserData;

import java.util.ArrayList;
import java.util.List;

public class MemoryUserDAO implements UserDAO {
    List<UserData> list = new ArrayList<>();

    @Override
    public void insertUser(UserData u) {
        list.add(u);
    }

    @Override
    public UserData getUser(String username) {
        for (UserData i : list) {
            if (i.username().equals(username)) {
                return i;
            }
        }
        return null;
    }

    @Override
    public void clear() {
        list.clear();
    }
}
