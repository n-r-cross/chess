package client;

import server.Server;
import server.result.LoginResult;
import server.result.RegisterResult;

public class Client {

    private static Server server;
    private static ServerFacade serverFacade;

    private String authToken = "";

    public Client() {
        serverFacade = new ServerFacade("localhost", 8080);
    }

    public String eval(String request) {
        return request;
    }

    public void login(String username, String password) throws Exception {
        LoginResult lr = null;
        System.out.println("Trying login");
        lr = serverFacade.login(username, password);
        if ((lr == null) || (lr.authToken() == null)) {
            System.out.println("Login failed :(");
            return;
        }
        authToken = lr.authToken();
        // TODO: remove authToken debug print
        System.out.println(authToken);
        // PreUI preUI = new PreUI();
        // preUI.run();
    }

    public void register(String username, String password, String email) throws Exception {
        RegisterResult rr = null;
        System.out.println("Trying register");
        rr = serverFacade.register(username, password, email);
        authToken = rr.authToken();
        // TODO: remove authToken debug print
        System.out.println(authToken);
    }

    public void logout() throws Exception {
        System.out.println("Trying logout");
        serverFacade.logout(authToken);
        authToken = "";
    }

    public void create(String gameName) throws Exception {
        System.out.println("Trying create");
        serverFacade.createGame(gameName, authToken);
        System.out.println("Create succeeded! Try listing games to see it!");
    }
}
