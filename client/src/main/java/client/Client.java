package client;

import server.Server;
import server.result.LoginResult;
import server.result.RegisterResult;
import ui.PreUI;

public class Client {
    // HttpClient for making requests
    // private static final HttpClient httpClient = HttpClient.newHttpClient();

    private static Server server;
    private static ServerFacade serverFacade;

    public Client() {
        serverFacade = new ServerFacade("localhost", 8080);
    }

    public void login() {
        //new ClientMain().get("localhost", 8080, "/name");
        LoginResult lr = null;
        try {
            System.out.println("Trying login");
            lr = serverFacade.login("ga", "ga");
        } catch (Exception e) {
            System.out.println(lr);
        }
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
