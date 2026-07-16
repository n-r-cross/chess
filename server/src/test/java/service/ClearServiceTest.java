package service;

import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ClearServiceTest {
    ClearService clearService = new ClearService();


    @Test
    void clearUserData() {
        Service.userData.insertUser(new UserData("ga", "gaAa", "gaAaAa"));
        clearService.clear();
        Assertions.assertEquals(new MemoryUserDAO(), Service.userData);
    }

    @Test
    void clearAuthData() {
        Service.authData.createAuth(new AuthData("ga", "gaAaAa"));
        clearService.clear();
        Assertions.assertEquals(new MemoryAuthDAO(), Service.authData);
    }

    @Test
    void clearGameData() {
        Service.gameData.createGame("disc_wars");
        clearService.clear();
        Assertions.assertEquals(new MemoryGameDAO(), Service.gameData);
    }

    @Test
    void noClearUserData() {
        Service.userData.insertUser(new UserData("ga", "gaAa", "gaAaAa"));
        Assertions.assertNotEquals(new MemoryUserDAO(), Service.userData);
    }

    @Test
    void noClearAuthData() {
        Service.authData.createAuth(new AuthData("ga", "gaAaAa"));
        Assertions.assertNotEquals(new MemoryAuthDAO(), Service.authData);
    }

    @Test
    void noClearGameData() {
        Service.gameData.createGame("disc_wars");
        Assertions.assertNotEquals(new MemoryAuthDAO(), Service.authData);
    }
}