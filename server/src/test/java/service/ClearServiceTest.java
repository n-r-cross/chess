package service;

import dataaccess.*;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.fail;

class ClearServiceTest {
    ClearService clearService = new ClearService();

    @Test
    void clearUserData() {
        try {
            Service.userData.insertUser(new UserData("ga", "gaAa", "gaAaAa"));
        } catch (Exception e) {
            fail();
        }
        try {
            clearService.clear();
        } catch (Exception e) {
            fail();
        }
        Assertions.assertThrows(Exception.class, () -> Service.userData.getUser("ga"));
    }

    @Test
    void clearAuthData() throws Exception {
        Service.authData.createAuth(new AuthData("ga", "gaAaAa"));
        clearService.clear();
        Assertions.assertThrows(Exception.class, () -> Service.authData.getAuth("ga"));
    }

    @Test
    void clearGameData() {
        try {
            Service.gameData.createGame("disc_wars");
        } catch (Exception e) {
            fail();
        }
        try {
            clearService.clear();
        } catch (Exception e) {
            fail();
        }
        try {
            Assertions.assertEquals(new ArrayList<GameData>(), Service.gameData.listGames());
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    void noClearUserData() {
        try {
            Service.userData.insertUser(new UserData("ga", "gaAa", "gaAaAa"));
        } catch (Exception e) {
            fail();
        }
        Assertions.assertDoesNotThrow(() -> Service.userData.getUser("ga"));
    }

    @Test
    void noClearAuthData() {
        try {
            Service.authData.createAuth(new AuthData("ga", "gaAaAa"));
        } catch (Exception e) {
            fail();
        }
        Assertions.assertDoesNotThrow(() -> Service.authData.getAuth("ga"));
    }

    @Test
    void noClearGameData() {
        try {
            Service.gameData.createGame("disc_wars");
        } catch (Exception e) {
            fail();
        }
        try {
            Assertions.assertNotEquals(new ArrayList<GameData>(), Service.gameData.listGames());
        } catch (Exception e) {
            fail();
        }
    }

    @BeforeEach
    void setUp() {
        try {
            clearService.clear();
        } catch (Exception e) {
            fail();
        }
    }
}