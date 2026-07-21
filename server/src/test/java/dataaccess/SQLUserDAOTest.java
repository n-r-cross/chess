package dataaccess;

import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import service.ForbiddenException;

import static org.junit.jupiter.api.Assertions.fail;

class SQLUserDAOTest {
    @BeforeEach
    void beforeEach() {
        UserDAO userDAO = new SQLUserDAO();
        try {
            userDAO.clear();
        } catch (Exception e) {
            System.out.println("Clear failed!");
            fail();
        }
    }

    @Test
    @DisplayName("Valid User Insertion")
    void insertUserSuccess() {
        UserDAO userDAO = new SQLUserDAO();
        Assertions.assertDoesNotThrow(() -> {
            userDAO.insertUser(new UserData("ga", "ga", "ga"));
        });
    }

    @Test
    @DisplayName("Invalid User Insertion (username already exists)")
    void insertUserFail() {
        UserDAO userDAO = new SQLUserDAO();
        UserData ud = new UserData("ga", "ga", "ga");
        Assertions.assertDoesNotThrow(() -> {
            userDAO.insertUser(ud);
        });
        Assertions.assertThrows(ForbiddenException.class, () -> {
            userDAO.insertUser(ud);
        });
    }

    @Test
    @DisplayName("Valid Get User (user already exists)")
    void getUserSuccess() {
        UserDAO userDAO = new SQLUserDAO();
        UserData ud = new UserData("ga", "ga", "ga");
        Assertions.assertDoesNotThrow(() -> {
            userDAO.insertUser(new UserData("ga", "ga", "ga"));
        });
        UserData response = null;
        try {
            response = userDAO.getUser("ga");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            fail();
        }
        Assertions.assertEquals(ud, response);
    }

    @Test
    @DisplayName("Invalid Get User (user doesn't exist)")
    void getUserFail() {
        UserDAO userDAO = new SQLUserDAO();
        Assertions.assertThrows(DataAccessException.class, () -> {
            userDAO.getUser("ga");
        });
    }

    @Test
    void clear() {
        UserDAO userDAO = new SQLUserDAO();
        UserData ud = new UserData("ga", "ga", "ga");
        Assertions.assertDoesNotThrow(() -> {
            userDAO.insertUser(new UserData("ga", "ga", "ga"));
        });
        try {
            userDAO.clear();
        } catch (Exception e) {
            System.out.println("Clear failed!");
            fail();
        }
        Assertions.assertThrows(DataAccessException.class, () -> {
            userDAO.getUser("ga");
        });
    }
}