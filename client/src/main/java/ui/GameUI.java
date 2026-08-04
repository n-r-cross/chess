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
            case "leave":
                try {
                    client.leave();
                } catch (Exception e) {
                    System.out.println(SET_TEXT_COLOR_RED + "Error: couldn't leave game");
                }
                return 1;
            case "move":
                System.out.println("Moving!");
                return 2;
            case "redraw":
                System.out.println("Redrawing!");
                return 2;
            case "highlight":
                System.out.println("Highlighting!");
                return 2;
            case "resign":
                System.out.println("Resigning!");

                return 1;
            case "help":
                help();
                return 2;
            default:
                System.out.println(SET_TEXT_COLOR_RED + "Error: Invalid command. Type 'help' for list of valid commands");
                return 2;
        }
    }

    public void help() {
        System.out.println("'leave' to remove yourself from game");
        System.out.println("'move' to make a move");
        System.out.println("'redraw' to redraw the board");
        System.out.println("'highlight' to highlight legal moves");
        System.out.println("'resign' to forfeit game");
        System.out.println("'help' to print this help message");
    }
}
