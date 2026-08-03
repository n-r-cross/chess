package ui;

import client.Client;

import static ui.EscapeSequences.*;

public class GameUI {

    private final Client client;

    public GameUI(Client client) {
        this.client = client;
    }

    public void prompt() {
        System.out.print(RESET_TEXT_COLOR + "Game >>> ");
    }

    public int run(String input) {
        var inputs = input.split(" ");
        int game_number;
        switch (inputs[0]) {
            case "logout":
                try {
                    client.logout();
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    return 1;
                }
                return 0;
            case "help":
                help();
                return 1;
            default:
                System.out.println(SET_TEXT_COLOR_RED + "Error: Invalid command. Type 'help' for list of valid commands");
                return 2;
        }
    }

    public void help() {
        System.out.println("'logout' to log out of your account");
        System.out.println("'create <name>' to create a game with name");
        System.out.println("'join <ID> [WHITE|BLACK]' to join a game");
        System.out.println("'observe <ID>' to observe a game");
        System.out.println("'list' to list all games on server");
        System.out.println("'help' to print this help message");
    }
}
