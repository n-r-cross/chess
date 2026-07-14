package service;

import dataaccess.DataAccessException;
import dataaccess.MemoryUserDAO;
import dataaccess.UserDAO;
import model.UserData;
import server.RegisterRequest;
import server.RegisterResponse;

import java.util.UUID;


public class UserService {
    private UserDAO data = new MemoryUserDAO();

    public RegisterResponse register(RegisterRequest r) throws DataAccessException {
        // save userData
        UserData ud = new UserData(r.username(), r.password(), r.email());
        UserData existing = data.getUser(r.username());
        if (existing != null) {
            throw new DataAccessException("User already exists");
        }
        data.insertUser(ud);
        // save authData
        String token = generateToken();
        return new RegisterResponse(token);
    }

    private static String generateToken() {
        return UUID.randomUUID().toString();
    }
}
