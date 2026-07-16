package service;

import chess.ChessGame;
import dataaccess.DataAccessException;
import model.GameData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import server.*;

import java.util.ArrayList;
import java.util.List;

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
    void listValid() {
        GameService gameService = new GameService();
        UserService userService = new UserService();
        reset();
        RegisterResult result = null;
        try {
            result = userService.register(new RegisterRequest("ga", "ga", "ga"));
        } catch (Exception e) {
            fail();
        }
        List<GameData> data = null;
        try {
            data = gameService.list(new ListRequest(result.authToken())).games();
        } catch (Exception e) {
            fail();
        }
        Assertions.assertEquals(new ArrayList<GameData>(), data);
        try {
            gameService.create(new CreateRequest("disc_wars"));
            data = gameService.list(new ListRequest(result.authToken())).games();
        } catch (Exception e) {
            fail();
        }
        GameData game1 = new GameData(1, null, null, "disc_wars", new ChessGame());
        ArrayList<GameData> golden = new ArrayList<GameData>();
        golden.add(game1);
        Assertions.assertEquals(golden, data);
        GameData game2 = new GameData(2, null, null, "light_cycles", new ChessGame());
        golden.add(game2);
        try {
            gameService.create(new CreateRequest("light_cycles"));
            data = gameService.list(new ListRequest(result.authToken())).games();
        } catch (Exception e) {
            fail();
        }
        Assertions.assertEquals(golden, data);
    }

    @Test
    void listInvalid() {
        GameService gameService = new GameService();
        UserService userService = new UserService();
        reset();
        RegisterResult result = null;
        try {
            result = userService.register(new RegisterRequest("ga", "ga", "ga"));
        } catch (Exception e) {
            fail();
        }
        List<GameData> data = null;
        try {
            data = gameService.list(new ListRequest("fake-auth-token")).games();
        } catch (Exception e) {
            Assertions.assertEquals("bad request", e.getMessage());
        }
        try {
            gameService.create(new CreateRequest("disc_wars"));
            data = gameService.list(new ListRequest(result.authToken())).games();
        } catch (Exception e) {
            fail();
        }
        GameData game1 = new GameData(1, null, null, "disc_wars", new ChessGame());
        ArrayList<GameData> golden = new ArrayList<>();
        golden.add(game1);
        Assertions.assertEquals(golden, data);
        GameData game2 = new GameData(2, null, null, "light_cycles", new ChessGame());
        golden.add(game2);
        try {
            gameService.create(new CreateRequest("light_cycles"));
            data = gameService.list(new ListRequest(result.authToken())).games();
        } catch (Exception e) {
            fail();
        }
        Assertions.assertEquals(golden, data);
    }

    @Test
    void createValid() {
        GameService gameService = new GameService();
        UserService userService = new UserService();
        reset();
        try {
            userService.register(new RegisterRequest("ga", "ga", "ga"));
        } catch (Exception e) {
            fail();
        }
        try {
            CreateResult createResult = gameService.create(new CreateRequest("disc_wars"));
        } catch (Exception e) {
            fail();
        }
        GameData game1 = new GameData(1, null, null, "disc_wars", new ChessGame());
        try {
            Assertions.assertEquals(game1, Service.gameData.getGame(1));
        } catch (DataAccessException e) {
            fail();
        }
    }

    @Test
    void createInvalid() {
        GameService gameService = new GameService();
        UserService userService = new UserService();
        reset();
        RegisterResult result = null;
        try {
            result = userService.register(new RegisterRequest("ga", "ga", "ga"));
        } catch (Exception e) {
            fail();
        }
        try {
            gameService.create(new CreateRequest(null));
        } catch (Exception e) {
            Assertions.assertEquals("bad request", e.getMessage());
        }
    }

    @Test
    void join() {
    }
}