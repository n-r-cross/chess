package client;

import dataaccess.DataAccessException;
import org.junit.jupiter.api.*;
import server.Server;
import server.request.LogoutRequest;
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
            Assertions.assertEquals(null, serverFacade.register(null, "ga", "ga"));
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
            LoginResult lr = serverFacade.login("ga", "ga");
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
    }

    @Test
    public void createGameSuccess() {

    }


}
