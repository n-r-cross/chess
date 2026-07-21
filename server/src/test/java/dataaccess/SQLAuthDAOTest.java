package dataaccess;

import model.AuthData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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
    @DisplayName("Create auth success")
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
    void getAuth() {
    }

    @Test
    void deleteAuth() {
    }

    @Test
    void clear() {

    }
}