package service;

import dataaccess.DataAccessException;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import server.request.LoginRequest;
import server.request.LogoutRequest;
import server.request.RegisterRequest;
import server.result.LoginResult;
import server.result.RegisterResult;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    private static final ClearService CLEAR_SERVICE = new ClearService();

    void reset() {
        CLEAR_SERVICE.clear();
    }

    @Test
    void validateValid() {
        reset();
        UserService u = new UserService();
        try {
            Service.userData.insertUser(new UserData("ga", "ga", "ga"));
        } catch (Exception e) {
            fail();
        }
        boolean result = false;
        try {
            result = u.validate("ga", "ga");
        } catch (DataAccessException e) {
            fail();
        }
        assertTrue(result);
    }

    @Test
    void validateInvalid() {
        reset();
        UserService u = new UserService();
        try {
            Service.userData.insertUser(new UserData("ga", "ga", "ga"));
        } catch (Exception e) {
            fail();
        }
        try {
            u.validate("ga", "gaAa");
            fail();
        } catch (DataAccessException e) {
            Assertions.assertEquals("unauthorized", e.getMessage());
        }
        try {
            u.validate("gaAa", "gaAa");
            fail();
        } catch (DataAccessException e) {
            Assertions.assertEquals("unauthorized", e.getMessage());
        }
    }

    @Test
    void registerValidRequest() {
        reset();
        UserService u = new UserService();
        RegisterRequest request = new RegisterRequest("ga", "ga", "ga");
        RegisterResult result = null;
        try {
            result = u.register(request);
        } catch (Exception e) {
            fail();
        }
        Assertions.assertNotEquals(null, result);
        Assertions.assertEquals(request.username(), result.username());
        Assertions.assertNotEquals(null, result.authToken());
    }

    @Test
    void registerInvalidRequest() {
        reset();
        UserService u = new UserService();
        RegisterRequest request = new RegisterRequest("ga", "ga", "ga");
        try {
            u.register(request);
        } catch (Exception e) {
            fail();
        }
        try {
            u.register(request);
            fail();
        } catch (Exception e) {
            Assertions.assertEquals("already taken", e.getMessage());
        }
        request = new RegisterRequest("ga", null, "ga");
        try {
            u.register(request);
            fail();
        } catch (Exception e) {
            Assertions.assertEquals("bad request", e.getMessage());
        }
        request = new RegisterRequest(null, "ga", "ga");
        try {
            u.register(request);
            fail();
        } catch (Exception e) {
            Assertions.assertEquals("bad request", e.getMessage());
        }
        request = new RegisterRequest("ga", "ga", null);
        try {
            u.register(request);
            fail();
        } catch (Exception e) {
            Assertions.assertEquals("bad request", e.getMessage());
        }
        request = new RegisterRequest(null, "ga", null);
        try {
            u.register(request);
            fail();
        } catch (Exception e) {
            Assertions.assertEquals("bad request", e.getMessage());
        }
        request = new RegisterRequest(null, null, "ga");
        try {
            u.register(request);
            fail();
        } catch (Exception e) {
            Assertions.assertEquals("bad request", e.getMessage());
        }
        request = new RegisterRequest("ga", null, null);
        try {
            u.register(request);
            fail();
        } catch (Exception e) {
            Assertions.assertEquals("bad request", e.getMessage());
        }
        request = new RegisterRequest(null, null, null);
        try {
            u.register(request);
            fail();
        } catch (Exception e) {
            Assertions.assertEquals("bad request", e.getMessage());
        }
    }

    @Test
    void loginValidRequest() {
        reset();
        UserService u = new UserService();
        RegisterRequest registerRequest = new RegisterRequest("ga", "ga", "ga");
        try {
            u.register(registerRequest);
        } catch (Exception e) {
            fail();
        }
        LoginRequest loginRequest = new LoginRequest("ga", "ga");
        LoginResult result = null;
        try {
            result = u.login(loginRequest);
        } catch (Exception e) {
            fail();
        }
        Assertions.assertNotEquals(null, result);
        Assertions.assertEquals(loginRequest.username(), result.username());
        Assertions.assertNotEquals(null, result.authToken());
    }

    @Test
    void loginInvalidRequest() {
        reset();
        UserService u = new UserService();
        try {
            u.register(new RegisterRequest("ga", "ga", "ga"));
        } catch (Exception e) {
            fail();
        }
        try {
            u.login(new LoginRequest("ga", "ga"));
        } catch (Exception e) {
            fail();
        }
        try {
            u.login(new LoginRequest("ga", null));
            fail();
        } catch (Exception e) {
            Assertions.assertEquals("bad request", e.getMessage());
        }
        try {
            u.login(new LoginRequest(null, "ga"));
            fail();
        } catch (Exception e) {
            Assertions.assertEquals("bad request", e.getMessage());
        }
        try {
            u.login(new LoginRequest(null, null));
            fail();
        } catch (Exception e) {
            Assertions.assertEquals("bad request", e.getMessage());
        }
    }

    String setupLogoutTest(UserService u) {
        reset();
        RegisterRequest registerRequest = new RegisterRequest("ga", "ga", "ga");
        String authToken = null;
        try {
            authToken = u.register(registerRequest).authToken();
        } catch (Exception e) {
            fail();
        }
        return authToken;
    }

    @Test
    void logoutValidRequest() {
        UserService u = new UserService();
        String authToken = setupLogoutTest(u);
        LogoutRequest logoutRequest = new LogoutRequest(authToken);
        try {
            u.logout(logoutRequest);
        } catch (Exception e) {
            fail();
        }
        try {
            Service.authData.getAuth(authToken);
            fail();
        } catch (Exception e) {
            Assertions.assertEquals("unauthorized", e.getMessage());
        }
    }

    @Test
    void logoutInvalidRequest() {
        UserService u = new UserService();
        String authToken = setupLogoutTest(u);

        LogoutRequest logoutRequest = new LogoutRequest(authToken);
        try {
            u.logout(logoutRequest);
        } catch (Exception e) {
            fail();
        }
        try {
            u.logout(logoutRequest);
            fail();
        } catch (Exception e) {
            Assertions.assertEquals("unauthorized", e.getMessage());
        }
    }
}