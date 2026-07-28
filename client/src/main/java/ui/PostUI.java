package ui;

import client.Client;

public class PostUI {

    private final Client client;

    public PostUI(Client client) {
        this.client = client;
    }

    public void prompt() {
        System.out.print("Logged in >>> ");
    }

    public int run(String input) {
        System.out.println("\'" + input + "\'");
        return 0;
    }
}
