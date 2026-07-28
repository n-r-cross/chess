package client;

import chess.ChessGame;
import dataaccess.DataAccessException;
import model.GameData;
import org.junit.jupiter.api.*;
import server.Server;
import server.result.ListResult;
import server.result.LoginResult;
import service.ClearService;

import static org.junit.jupiter.api.Assertions.fail;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade serverFacade;
    private static final ClearService clearService = new ClearService();

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        serverFacade = new ServerFacade("localhost", port);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @BeforeEach
    void clear() {
        try {
            clearService.clear();
        } catch (DataAccessException e) {
            fail();
        }
    }

    @Test
    public void registerFail() {
        try {
            Assertions.assertNull(serverFacade.register(null, "ga", "ga"));
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void registerSuccess() {
        Assertions.assertDoesNotThrow(() -> serverFacade.register("ga", "ga", "ga"));
    }


    @Test
    public void logoutFail() {
        Assertions.assertThrows(Exception.class, () -> serverFacade.logout("fake-auth-token"));
    }

    @Test
    public void logoutSuccess() {
        String token = "";
        try {
            token = serverFacade.register("ga", "ga", "ga").authToken();
        } catch (Exception e) {
            fail();
        }
        String tokenFinal = token;
        Assertions.assertDoesNotThrow(() -> serverFacade.logout(tokenFinal));
    }


    @Test
    public void loginFail() {
        try {
            serverFacade.login("ga", "ga");
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void loginSuccess() {
        Assertions.assertDoesNotThrow(() -> serverFacade.register("ga", "ga", "ga"));
        //Assertions.assertDoesNotThrow(() -> serverFacade.logout());
        // Verify that login worked
        LoginResult lr = null;
        try {
            lr = serverFacade.login("ga", "ga");
        } catch (Exception e) {
            fail();
        }
        Assertions.assertTrue(lr.authToken().length() > 10);
    }

    @Test
    public void createGameFail() {
        Assertions.assertThrows(Exception.class, () -> serverFacade.createGame(null, "fake-auth-token"));
        String token = "";
        try {
            token = serverFacade.register("ga", "ga", "ga").authToken();
        } catch (Exception e) {
            fail();
        }
        String finalToken = token;
        Assertions.assertThrows(Exception.class, () -> serverFacade.createGame(null, finalToken));

    }

    @Test
    public void createGameSuccess() {
        String token = "";
        try {
            token = serverFacade.register("ga", "ga", "ga").authToken();
        } catch (Exception e) {
            fail();
        }

        int id = -1;
        try {
            id = serverFacade.createGame("light_cycles", token).gameID();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            fail();
        }
        Assertions.assertNotEquals(-1, id);
    }

    @Test
    public void listGamesSuccess() {
        String token = "";
        try {
            token = serverFacade.register("ga", "ga", "ga").authToken();
        } catch (Exception e) {
            fail();
        }
        try {
            Assertions.assertEquals(ListResult.class, serverFacade.listGames(token).getClass());
            serverFacade.createGame("light_cycles", token);
            Assertions.assertEquals(ListResult.class, serverFacade.listGames(token).getClass());
            GameData data = serverFacade.listGames(token).games().getFirst();
            Assertions.assertEquals(new GameData(1, null, null, "light_cycles", new ChessGame()), data);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void listGamesFail() {
        Assertions.assertThrows(Exception.class, () -> serverFacade.listGames("fake-auth-token"));
    }

    @Test
    public void joinGameSuccess() {
    }

    @Test
    public void joinGameFail() {
    }

}
