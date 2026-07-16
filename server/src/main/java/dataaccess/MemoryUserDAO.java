package dataaccess;


import model.UserData;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MemoryUserDAO that = (MemoryUserDAO) o;
        return Objects.equals(list, that.list);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(list);
    }
}
