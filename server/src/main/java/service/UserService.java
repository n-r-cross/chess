package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import model.AuthData;
import model.UserData;
import server.*;

public class UserService extends Service {
    private final UserDAO users = Service.userData;
    private final AuthDAO auths = Service.authData;

    public void validate(String username, String password) throws DataAccessException {
        UserData u = users.getUser(username);
        if (!password.equals(u.password())) {
            throw new DataAccessException("unauthorized");
        }
        System.out.println("User validated!");
    }

    public RegisterResult register(RegisterRequest r) throws DataAccessException {
        // save userData
        UserData ud = new UserData(r.username(), r.password(), r.email());
        UserData existing = users.getUser(r.username());
        if (existing != null) {
            throw new DataAccessException("User already exists");
        }
        users.insertUser(ud);
        // save authData
        String token = newAuth(r.username());
        return new RegisterResult(token);
    }

    public LoginResult login(LoginRequest loginRequest) throws DataAccessException {
        // Create authToken
        String authToken = newAuth(loginRequest.username());
        return new LoginResult(loginRequest.username(), authToken);
    }

    public void logout(LogoutRequest logoutRequest) throws DataAccessException {
        // Delete authToken
        System.out.println("Logging out...");
        System.out.println(logoutRequest);
        auths.deleteAuth(logoutRequest.authToken());
    }
}
