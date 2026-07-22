package service;

import dataaccess.*;

public class Service {
    protected static UserDAO userData = new SQLUserDAO();
    protected static AuthDAO authData = new SQLAuthDAO();
    protected static GameDAO gameData = new SQLGameDAO();
}
