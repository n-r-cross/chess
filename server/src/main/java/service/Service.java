package service;

import dataaccess.*;

public class Service {
    protected static UserDAO userData = new MemoryUserDAO();
    protected static AuthDAO authData = new MemoryAuthDAO();
}
