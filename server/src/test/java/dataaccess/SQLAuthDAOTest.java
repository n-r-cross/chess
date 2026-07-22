package dataaccess;

import model.AuthData;
import org.junit.jupiter.api.*;
import service.UnauthorizedException;

import static org.junit.jupiter.api.Assertions.*;

class SQLAuthDAOTest {

    @BeforeEach
    void setUp() {
        AuthDAO authDAO = new SQLAuthDAO();
        try {
            authDAO.clear();
        } catch (Exception e) {
            System.out.println("Clear failed!");
            fail();
        }
    }

    @Test
    @DisplayName("Valid Auth creation")
    void createAuthSuccess() {
        AuthDAO authDAO = new SQLAuthDAO();
        Assertions.assertDoesNotThrow(() -> authDAO.createAuth(new AuthData("ga", "ga")));
        Assertions.assertDoesNotThrow(() -> authDAO.createAuth(new AuthData("gaAa", "ga")));
        Assertions.assertDoesNotThrow(() -> authDAO.createAuth(new AuthData("gaAaAa", "gaAa")));
    }

    @Test
    @DisplayName("Invalid Auth creation")
    void createAuthFail() {
        AuthDAO authDAO = new SQLAuthDAO();
        AuthData tokenNull = new AuthData(null, "ga");
        Assertions.assertThrows(Exception.class, () -> authDAO.createAuth(tokenNull));
        AuthData usernameNull = new AuthData("ga", null);
        Assertions.assertThrows(Exception.class, () -> authDAO.createAuth(usernameNull));
        AuthData bothNull = new AuthData(null, null);
        Assertions.assertThrows(Exception.class, () -> authDAO.createAuth(bothNull));
    }

    @Test
    @DisplayName("Valid Get Auth (auth already exists)")
    void getAuthSuccess() {
        AuthDAO authDAO = new SQLAuthDAO();
        AuthData ad = new AuthData("ga", "ga");
        Assertions.assertDoesNotThrow(() -> authDAO.createAuth(ad));
        AuthData response = null;
        try {
            response = authDAO.getAuth("ga");
        } catch (Exception e) {
            fail();
        }
        Assertions.assertEquals(ad.username(), response.username());
        Assertions.assertEquals(ad.authToken(), response.authToken());
    }

    @Test
    @DisplayName("Invalid Get Auth")
    void getAuthFail() {
        AuthDAO authDAO = new SQLAuthDAO();
        Assertions.assertThrows(UnauthorizedException.class, () -> authDAO.getAuth("ga"));
    }

    @Test
    @DisplayName("Valid Delete Auth (already exists)")
    void deleteAuthSuccess() {
        AuthDAO authDAO = new SQLAuthDAO();
        Assertions.assertDoesNotThrow(() -> authDAO.createAuth(new AuthData("gaAa", "ga")));
        Assertions.assertDoesNotThrow(() -> authDAO.getAuth("gaAa"));
        Assertions.assertDoesNotThrow(() -> authDAO.deleteAuth("gaAa"));
        Assertions.assertThrows(UnauthorizedException.class, () -> authDAO.getAuth("gaAa"));
    }

    @Test
    @DisplayName("Invalid Delete Auth")
    void deleteAuthFail() {
        AuthDAO authDAO = new SQLAuthDAO();
        Assertions.assertThrows(UnauthorizedException.class, () -> authDAO.deleteAuth("gaAa"));
        Assertions.assertDoesNotThrow(() -> authDAO.createAuth(new AuthData("gaAa", "ga")));
        Assertions.assertDoesNotThrow(() -> authDAO.getAuth("gaAa"));
        Assertions.assertDoesNotThrow(() -> authDAO.deleteAuth("gaAa"));
        Assertions.assertThrows(UnauthorizedException.class, () -> authDAO.deleteAuth("gaAa"));
    }

    @Test
    void clear() {
        AuthDAO authDAO = new SQLAuthDAO();
        Assertions.assertDoesNotThrow(() -> authDAO.createAuth(new AuthData("gaAa", "ga")));
        Assertions.assertDoesNotThrow(() -> authDAO.getAuth("gaAa"));
        try {
            authDAO.clear();
        } catch (Exception e) {
            fail();
        }
        Assertions.assertThrows(UnauthorizedException.class, () -> authDAO.getAuth("gaAa"));

    }

    @AfterEach
    void tearDown() {
        AuthDAO authDAO = new SQLAuthDAO();
        try {
            authDAO.clear();
        } catch (Exception e) {
            System.out.println("Clear failed!");
        }
    }
}