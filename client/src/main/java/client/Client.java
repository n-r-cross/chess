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

    public void login(String username, String password) {
        //new ClientMain().get("localhost", 8080, "/name");
        LoginResult lr = null;
        try {
            System.out.println("Trying login");
            lr = serverFacade.login(username, password);
        } catch (Exception e) {
            System.out.println(lr);
        }
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

    public void register() {
        RegisterResult rr = null;
        try {
            System.out.println("Trying register");
            rr = serverFacade.register("ga", "ga", "ga");
        } catch (Exception e) {
            System.out.println(rr);
        }
    }
}
