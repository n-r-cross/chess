package client;

import model.GameData;
import server.Server;
import server.result.ListResult;
import server.result.LoginResult;
import server.result.RegisterResult;

import java.util.List;

public class Client {

    private static ServerFacade serverFacade;

    private String authToken = "";

    private List<GameData> games;

    public Client() {
        serverFacade = new ServerFacade("localhost", 8080);
    }

    public String eval(String request) {
        return request;
    }

    public void login(String username, String password) throws Exception {
        System.out.println("Trying login");
        LoginResult lr = serverFacade.login(username, password);
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
        System.out.println("Trying register");
        RegisterResult rr = serverFacade.register(username, password, email);
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

    public void list() throws Exception {
        System.out.println("Trying list");
        ListResult lr = serverFacade.listGames(authToken);
        games = lr.games();
        for (int i = 0; i < games.size(); i++) {
            System.out.print(i + 1);
            System.out.print(") ");
            System.out.print(games.get(i).gameName());
            System.out.print(": WHITE - ");
            System.out.print(games.get(i).whiteUsername());
            System.out.print(" | BLACK - ");
            System.out.println(games.get(i).blackUsername());
        }
    }
}
