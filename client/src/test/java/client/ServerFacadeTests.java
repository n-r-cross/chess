package client;

import org.junit.jupiter.api.*;
import server.Server;
import server.result.LoginResult;


public class ServerFacadeTests {

    private static Server server;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @BeforeEach
    void clear() {
        server.clear();
    }

    @Test
    public void loggedInSuccess() {
        Assertions.assertTrue(server.register("ga", "ga", "ga"));
        Assertions.assertTrue(server.loggedIn());
    }

    @Test
    public void loggedInFail() {
        Assertions.assertFalse(server.loggedIn());
    }

    @Test
    public void loginSuccess() {
        Assertions.assertDoesNotThrow(server.register("ga", "ga", "ga"));
        Assertions.assertDoesNotThrow(server.logout());
        Assertions.assertFalse(server.loggedIn());
        Assertions.assertTrue(server.login("ga", "ga"));
        // Verify that login worked
        LoginResult lr = server.login("ga", "ga");
        Assertions.assertTrue(lr.authToken().length() > 10);
        Assertions.assertTrue(server.loggedIn());
    }

    @Test
    public void loginFail() {
        LoginResult lr = server.login("ga", "ga");
        Assertions.assertFalse(server.loggedIn());
    }

    @Test
    public void registerSuccess() {
        Assertions.assertDoesNotThrow(server.register("ga", "ga", "ga"));
        Assertions.assertTrue(server.loggedIn());
    }

    @Test
    public void registerSuccess() {
        Assertions.assertDoesNotThrow(server.register(null, "ga", "ga"));
        Assertions.assertTrue(server.loggedIn());
    }

}
