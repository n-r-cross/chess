package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import model.AuthData;
import model.UserData;
import server.request.LoginRequest;
import server.request.LogoutRequest;
import server.request.RegisterRequest;
import server.result.LoginResult;
import server.result.RegisterResult;

import java.util.UUID;

public class UserService extends Service {
    private final UserDAO users = Service.userData;
    private final AuthDAO auths = Service.authData;

    /**
     * Generate an authToken
     *
     * @return authToken as string
     */
    private String generateToken() {
        return UUID.randomUUID().toString();
    }

    /**
     * Add new authToken
     *
     * @param username username corresponding to token
     * @return authToken as string
     */
    protected String newAuth(String username) {
        String token = generateToken();
        authData.createAuth(new AuthData(token, username));
        return token;
    }

    /**
     * Check if username and password are in users
     *
     * @param username username to check
     * @param password password to check
     * @return true if both match
     * @throws DataAccessException if no corresponding
     *                             username or no username/password combo
     */
    public boolean validate(String username, String password) throws DataAccessException {
        UserData u = users.getUser(username);
        if (u == null) {
            throw new DataAccessException("unauthorized");
        }
        if (!password.equals(u.password())) {
            throw new DataAccessException("unauthorized");
        }
        return true;
    }

    /**
     * Create new User and login
     *
     * @param r request
     * @return result with username and token
     * @throws Exception if request is incomplete or
     *                   username already exists
     */
    public RegisterResult register(RegisterRequest r) throws Exception {
        if (!r.complete()) {
            throw new BadRequestException("bad request");
        }
        // save userData
        UserData ud = new UserData(r.username(), r.password(), r.email());
        UserData existing = users.getUser(r.username());
        if (existing != null) {
            throw new ForbiddenException("already taken");
        }
        users.insertUser(ud);
        // save authData
        String token = newAuth(r.username());
        return new RegisterResult(r.username(), token);
    }

    /**
     * Creates an authToken for username
     *
     * @param r request
     * @return username and authToken in result
     * @throws Exception if request is incomplete
     */
    public LoginResult login(LoginRequest r) throws Exception {
        if (!r.complete()) {
            throw new BadRequestException("bad request");
        }
        // Create authToken
        String authToken = newAuth(r.username());
        return new LoginResult(r.username(), authToken);
    }

    /**
     * Deletes authToken
     *
     * @param r request with authToken to delete
     * @throws Exception if request is incomplete
     */
    public void logout(LogoutRequest r) throws Exception {
        if (!r.complete()) {
            throw new BadRequestException("bad request");
        }
        // Delete authToken
        auths.deleteAuth(r.authToken());
    }
}
