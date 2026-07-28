package ui;

import client.Client;

public class GameUI {

    private final Client client;

    public GameUI(Client client) {
        this.client = client;
    }

    public void prompt() {
        System.out.print("Game >>> ");
    }

    public int run(String input) {
        System.out.println("'" + input + "'");
        return 1;
    }
}
