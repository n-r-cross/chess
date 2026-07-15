package service;

import dataaccess.DataAccessException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import server.LogoutRequest;
import server.RegisterRequest;
import server.RegisterResult;

import static org.junit.jupiter.api.Assertions.*;

class GameServiceTest {

    void reset() {
        ClearService clearService = new ClearService();
        try {
            clearService.clear();
        } catch (DataAccessException e) {
            fail();
        }
    }

    @Test
    void validateValid() {
        GameService gameService = new GameService();
        UserService userService = new UserService();
        reset();
        RegisterResult result = null;
        try {
            result = userService.register(new RegisterRequest("ga", "ga", "ga"));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            fail();
        }
        boolean valid = false;
        try {
            valid = gameService.validate(result.authToken());
        } catch (Exception e) {
            fail();
        }
        Assertions.assertTrue(valid);
    }

    @Test
    void validateInvalid() {
        GameService gameService = new GameService();
        UserService userService = new UserService();
        reset();
        RegisterResult result = null;
        try {
            gameService.validate("pretend-auth-token");
        } catch (Exception e) {
            Assertions.assertEquals("unauthorized", e.getMessage());
        }
        try {
            result = userService.register(new RegisterRequest("ga", "ga", "ga"));
            userService.logout(new LogoutRequest(result.authToken()));
        } catch (Exception e) {
            fail();
        }
        try {
            gameService.validate(result.authToken());
        } catch (Exception e) {
            Assertions.assertEquals("unauthorized", e.getMessage());
        }
    }

    @Test
    void list() {
    }

    @Test
    void create() {
    }

    @Test
    void join() {
    }
}