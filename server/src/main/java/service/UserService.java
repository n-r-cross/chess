package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import model.UserData;
import server.RegisterRequest;
import server.RegisterResponse;

public class UserService extends Service {
    private final UserDAO userData = Service.userData;
    private final AuthDAO authData = Service.authData;

    public RegisterResponse register(RegisterRequest r) throws DataAccessException {
        // save userData
        UserData ud = new UserData(r.username(), r.password(), r.email());
        UserData existing = userData.getUser(r.username());
        if (existing != null) {
            throw new DataAccessException("User already exists");
        }
        userData.insertUser(ud);
        // save authData
        String token = newAuth(r.username());
        return new RegisterResponse(token);
    }
}
