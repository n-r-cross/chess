package ui;

import chess.ChessGame;
import client.Client;

public class PostUI {

    private final Client client;

    public PostUI(Client client) {
        this.client = client;
    }

    private boolean checkArgCount(int expected, int got) {
        return expected == got;
    }

    public void prompt() {
        System.out.print("Logged in >>> ");
    }

    public int run(String input) {
        System.out.println("\'" + input + "\'");
        var inputs = input.split(" ");

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
            case "create":
                if (!checkArgCount(2, inputs.length)) {
                    System.out.println("Error: Wrong number of arguments");
                    return 1;
                }
                try {
                    client.create(inputs[1]);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                return 1;
            case "list":
                try {
                    client.list();
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                return 1;
            case "join":
                if (!checkArgCount(3, inputs.length)) {
                    System.out.println("Error: Wrong number of arguments");
                    return 1;
                }
                int game_number = -1;
                try {
                    game_number = Integer.parseInt(inputs[1]) - 1;
                } catch (Exception e) {
                    System.out.println("Error: Argument isn't a digit");
                    return 1;
                }
                ChessGame.TeamColor color;
                try {
                    color = ChessGame.TeamColor.valueOf(inputs[2]);
                } catch (Exception e) {
                    System.out.println("Error: Color must be WHITE or BLACK");
                    return 1;
                }
                try {
                    client.join(game_number, color);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                // WHEN ENABLING GAME UI, CHANGE TO 2
                return 1;
            default:
                System.out.println("Error: Invalid command. Type 'help' for list of valid commands");
                return 1;
        }
    }

    public void help() {
        System.out.println("'logout' to log out of your account");
        System.out.println("'create <name>' to create a game with name");
        System.out.println("'list' to list all games on server");
        System.out.println("'help' to print this help message");
    }
}
