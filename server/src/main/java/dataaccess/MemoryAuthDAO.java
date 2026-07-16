package dataaccess;

import model.AuthData;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MemoryAuthDAO implements AuthDAO {
    List<AuthData> list = new ArrayList<>();

    @Override
    public void createAuth(AuthData a) {
        list.add(a);
    }

    @Override
    public AuthData getAuth(String token) throws DataAccessException {
        for (AuthData i : list) {
            if (Objects.equals(i.authToken(), token)) {
                return i;
            }
        }
        throw new DataAccessException("unauthorized");
    }

    @Override
    public void deleteAuth(String token) throws DataAccessException {
        AuthData a = getAuth(token);
        list.remove(a);
    }

    @Override
    public void clear() {
        list.clear();
    }
}
