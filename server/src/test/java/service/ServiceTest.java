package service;

import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryUserDAO;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ServiceTest {

    @Test
    void clearUserData() {
        Service s = new Service();
        try {
            Service.userData.insertUser(new UserData("ga", "gaAa", "gaAaAa"));
            Service.clear();
        } catch (Exception e) {
            Assertions.assertEquals("", e.getMessage());
        }
        Assertions.assertEquals(new MemoryUserDAO(), Service.userData);
    }

    @Test
    void clearAuthData() {
        Service s = new Service();
        try {
            Service.authData.createAuth(new AuthData("ga", "gaAaAa"));
            Service.clear();
        } catch (Exception e) {
            Assertions.assertEquals("", e.getMessage());
        }
        Assertions.assertEquals(new MemoryAuthDAO(), Service.authData);
    }

    @Test
    void noClearUserData() {
        Service s = new Service();
        try {
            Service.userData.insertUser(new UserData("ga", "gaAa", "gaAaAa"));
        } catch (Exception e) {
            Assertions.assertEquals("", e.getMessage());
        }
        Assertions.assertNotEquals(new MemoryUserDAO(), Service.userData);
    }

    @Test
    void noClearAuthData() {
        Service s = new Service();
        try {
            Service.authData.createAuth(new AuthData("ga", "gaAaAa"));
        } catch (Exception e) {
            Assertions.assertEquals("", e.getMessage());
        }
        Assertions.assertNotEquals(new MemoryAuthDAO(), Service.authData);
    }
}