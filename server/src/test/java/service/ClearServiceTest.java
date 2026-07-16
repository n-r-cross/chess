package service;

import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.fail;

class ClearServiceTest {

    @Test
    void clearUserData() {
        ClearService s = new ClearService();
        try {
            Service.userData.insertUser(new UserData("ga", "gaAa", "gaAaAa"));
            s.clear();
        } catch (Exception e) {
            fail();
        }
        Assertions.assertEquals(new MemoryUserDAO(), Service.userData);
    }

    @Test
    void clearAuthData() {
        ClearService s = new ClearService();
        try {
            Service.authData.createAuth(new AuthData("ga", "gaAaAa"));
            s.clear();
        } catch (Exception e) {
            fail();
        }
        Assertions.assertEquals(new MemoryAuthDAO(), Service.authData);
    }

    @Test
    void clearGameData() {
        ClearService s = new ClearService();
        try {
            Service.gameData.createGame("disc_wars");
            s.clear();
        } catch (Exception e) {
            fail();
        }
        Assertions.assertEquals(new MemoryGameDAO(), Service.gameData);
    }

    @Test
    void noClearUserData() {
        try {
            Service.userData.insertUser(new UserData("ga", "gaAa", "gaAaAa"));
        } catch (Exception e) {
            fail();
        }
        Assertions.assertNotEquals(new MemoryUserDAO(), Service.userData);
    }

    @Test
    void noClearAuthData() {
        try {
            Service.authData.createAuth(new AuthData("ga", "gaAaAa"));
        } catch (Exception e) {
            fail();
        }
        Assertions.assertNotEquals(new MemoryAuthDAO(), Service.authData);
    }

    @Test
    void noClearGameData() {
        try {
            Service.gameData.createGame("disc_wars");
        } catch (Exception e) {
            fail();
        }
        Assertions.assertNotEquals(new MemoryAuthDAO(), Service.authData);
    }
}