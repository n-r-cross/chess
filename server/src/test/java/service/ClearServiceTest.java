package service;

import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryUserDAO;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ClearServiceTest {

    @Test
    void clearUserData() {
        ClearService s = new ClearService();
        try {
            Service.userData.insertUser(new UserData("ga", "gaAa", "gaAaAa"));
            s.clear();
        } catch (Exception e) {
            Assertions.assertEquals("", e.getMessage());
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
            Assertions.assertEquals("", e.getMessage());
        }
        Assertions.assertEquals(new MemoryAuthDAO(), Service.authData);
    }

    @Test
    void noClearUserData() {
        try {
            Service.userData.insertUser(new UserData("ga", "gaAa", "gaAaAa"));
        } catch (Exception e) {
            Assertions.assertEquals("", e.getMessage());
        }
        Assertions.assertNotEquals(new MemoryUserDAO(), Service.userData);
    }

    @Test
    void noClearAuthData() {
        try {
            Service.authData.createAuth(new AuthData("ga", "gaAaAa"));
        } catch (Exception e) {
            Assertions.assertEquals("", e.getMessage());
        }
        Assertions.assertNotEquals(new MemoryAuthDAO(), Service.authData);
    }
}