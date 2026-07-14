package service;

import dataaccess.*;
import model.AuthData;

import java.util.UUID;

public class Service {
    protected static UserDAO userData = new MemoryUserDAO();
    protected static AuthDAO authData = new MemoryAuthDAO();

    private String generateToken() {
        return UUID.randomUUID().toString();
    }

    protected String newAuth(String username) throws DataAccessException {
        String token = generateToken();
        authData.createAuth(new AuthData(token, username));
        return token;
    }
}
