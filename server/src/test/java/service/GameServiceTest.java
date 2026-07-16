package service;

import chess.ChessGame;
import model.GameData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import server.request.*;
import server.result.CreateResult;
import server.result.RegisterResult;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameServiceTest {

    private static final ClearService CLEAR_SERVICE = new ClearService();

    void reset() {
        CLEAR_SERVICE.clear();
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
            fail();
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
            fail();
        } catch (Exception e) {
            Assertions.assertEquals("unauthorized", e.getMessage());
        }
    }

    @Test
    void listEmpty() {
        GameService gameService = new GameService();
        UserService userService = new UserService();
        reset();
        try {
            userService.register(new RegisterRequest("ga", "ga", "ga"));
        } catch (Exception e) {
            fail();
        }
        List<GameData> data = null;
        try {
            data = gameService.list().games();
        } catch (Exception e) {
            fail();
        }
        Assertions.assertEquals(new ArrayList<GameData>(), data);
        try {
            gameService.create(new CreateRequest("disc_wars"));
            data = gameService.list().games();
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
            data = gameService.list().games();
        } catch (Exception e) {
            fail();
        }
        Assertions.assertEquals(golden, data);
    }

    @Test
    void listOne() {
        GameService gameService = new GameService();
        UserService userService = new UserService();
        reset();
        try {
            userService.register(new RegisterRequest("ga", "ga", "ga"));
        } catch (Exception e) {
            fail();
        }
        List<GameData> data = null;
        try {
            gameService.create(new CreateRequest("disc_wars"));
            data = gameService.list().games();
        } catch (Exception e) {
            fail();
        }
        GameData game1 = new GameData(1, null, null, "disc_wars", new ChessGame());
        ArrayList<GameData> golden = new ArrayList<>();
        golden.add(game1);
        Assertions.assertEquals(golden, data);
    }

    @Test
    void listTwo() {
        GameService gameService = new GameService();
        UserService userService = new UserService();
        reset();
        try {
            userService.register(new RegisterRequest("ga", "ga", "ga"));
        } catch (Exception e) {
            fail();
        }
        List<GameData> data = null;
        GameData game1 = new GameData(1, null, null, "disc_wars", new ChessGame());
        GameData game2 = new GameData(2, null, null, "light_cycles", new ChessGame());
        ArrayList<GameData> golden = new ArrayList<>();
        golden.add(game1);
        golden.add(game2);
        try {
            gameService.create(new CreateRequest("disc_wars"));
            gameService.create(new CreateRequest("light_cycles"));
            data = gameService.list().games();
        } catch (Exception e) {
            fail();
        }
        Assertions.assertEquals(golden, data);
    }

    @Test
    void createValid() {
        GameService gameService = new GameService();
        reset();
        try {
            gameService.create(new CreateRequest("disc_wars"));
        } catch (Exception e) {
            fail();
        }
        GameData game1 = new GameData(1, null, null, "disc_wars", new ChessGame());
        try {
            Assertions.assertEquals(game1, Service.gameData.getGame(1));
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    void createInvalid() {
        GameService gameService = new GameService();
        reset();
        try {
            gameService.create(new CreateRequest(null));
            fail();
        } catch (Exception e) {
            Assertions.assertEquals("bad request", e.getMessage());
        }
    }

    @Test
    void joinValid() {
        GameService gameService = new GameService();
        UserService userService = new UserService();
        reset();
        RegisterResult registerResult = null;
        CreateResult createResult = null;
        try {
            registerResult = userService.register(new RegisterRequest("ga", "ga", "ga"));
            createResult = gameService.create(new CreateRequest("disc_wars"));
        } catch (Exception e) {
            fail();
        }
        try {
            gameService.join(new JoinRequest(ChessGame.TeamColor.WHITE, createResult.gameID()), registerResult.authToken());
            Assertions.assertEquals("ga", Service.gameData.getGame(createResult.gameID()).whiteUsername());
        } catch (Exception e) {
            fail();
        }
        try {
            gameService.join(new JoinRequest(ChessGame.TeamColor.BLACK, createResult.gameID()), registerResult.authToken());
            Assertions.assertEquals("ga", Service.gameData.getGame(createResult.gameID()).blackUsername());
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    void joinInvalid() {
        GameService gameService = new GameService();
        UserService userService = new UserService();
        reset();
        RegisterResult registerResult = null;
        CreateResult createResult = null;
        try {
            registerResult = userService.register(new RegisterRequest("ga", "ga", "ga"));
            createResult = gameService.create(new CreateRequest("disc_wars"));
        } catch (Exception e) {
            fail();
        }
        try {
            gameService.join(new JoinRequest(null, createResult.gameID()), registerResult.authToken());
            fail();
        } catch (Exception e) {
            Assertions.assertEquals("bad request", e.getMessage());
        }
        try {
            gameService.join(new JoinRequest(ChessGame.TeamColor.WHITE, -1), registerResult.authToken());
            fail();
        } catch (Exception e) {
            Assertions.assertEquals("bad request", e.getMessage());
        }
        try {
            gameService.join(new JoinRequest(ChessGame.TeamColor.WHITE, createResult.gameID()), "fake-auth-token");
            fail();
        } catch (Exception e) {
            Assertions.assertEquals("unauthorized", e.getMessage());
        }
        try {
            gameService.join(new JoinRequest(ChessGame.TeamColor.BLACK, createResult.gameID()), registerResult.authToken());
            gameService.join(new JoinRequest(ChessGame.TeamColor.WHITE, createResult.gameID()), registerResult.authToken());
        } catch (Exception e) {
            fail();
        }
        try {
            gameService.join(new JoinRequest(ChessGame.TeamColor.BLACK, createResult.gameID()), registerResult.authToken());
            fail();
        } catch (Exception e) {
            Assertions.assertEquals("already taken", e.getMessage());
        }
        try {
            gameService.join(new JoinRequest(ChessGame.TeamColor.WHITE, createResult.gameID()), registerResult.authToken());
            fail();
        } catch (Exception e) {
            Assertions.assertEquals("already taken", e.getMessage());
        }
    }
}