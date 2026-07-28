package client;

import org.junit.jupiter.api.*;
import server.Server;
import server.result.LoginResult;

import static org.junit.jupiter.api.Assertions.fail;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade serverFacade;

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

    @Test
    @Order(1)
    public void registerFail() {
        Assertions.assertDoesNotThrow(() -> serverFacade.register(null, "ga", "ga"));
        Assertions.assertTrue(serverFacade.loggedIn());
    }

    @Test
    @Order(2)
    public void registerSuccess() {
        Assertions.assertDoesNotThrow(() -> serverFacade.register("ga", "ga", "ga"));
        Assertions.assertTrue(serverFacade.loggedIn());
    }

    @Test
    @Order(3)
    public void loggedInSuccess() {
        Assertions.assertTrue(serverFacade.loggedIn());
    }

    @Test
    @Order(4)
    public void logoutFail() {
        // TODO: log out test
    }

    @Test
    @Order(5)
    public void logoutSuccess() {
        // TODO: log out test
    }

    @Test
    @Order(6)
    public void loggedInFail() {
        Assertions.assertFalse(serverFacade.loggedIn());
    }

    @Test
    @Order(7)
    public void loginFail() {
        try {
            LoginResult lr = serverFacade.login("ga", "ga");
        } catch (Exception e) {
            fail();
        }
        Assertions.assertFalse(serverFacade.loggedIn());
    }

    @Test
    @Order(8)
    public void loginSuccess() {
        Assertions.assertDoesNotThrow(() -> serverFacade.register("ga", "ga", "ga"));
        //Assertions.assertDoesNotThrow(() -> serverFacade.logout());
        Assertions.assertFalse(serverFacade.loggedIn());
        try {
            serverFacade.login("ga", "ga");
        } catch (Exception e) {
            fail();
        }
        // Verify that login worked
        LoginResult lr = null;
        try {
            lr = serverFacade.login("ga", "ga");
        } catch (Exception e) {
            fail();
        }
        Assertions.assertTrue(lr.authToken().length() > 10);
        Assertions.assertTrue(serverFacade.loggedIn());
    }

    @Test
    @Order(9)
    public void createGameFail() {
    }

    @Test
    @Order(10)
    public void createGameSuccess() {

    }
    

}
