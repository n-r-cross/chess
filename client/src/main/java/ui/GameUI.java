package ui;

import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;
import client.Client;

import java.util.Objects;
import java.util.Scanner;

import static ui.EscapeSequences.*;

public class GameUI {

    private final Client client;

    public GameUI(Client client) {
        this.client = client;
    }

    public void prompt() {
        System.out.print(RESET_TEXT_COLOR + "Game >>> ");
    }

    private int letterToCol(String letter) throws Exception {
        if (letter.length() > 1) {
            throw new Exception("Error: Need single letter!");
        }
        return switch (letter) {
            case "a" -> 1;
            case "b" -> 2;
            case "c" -> 3;
            case "d" -> 4;
            case "e" -> 5;
            case "f" -> 6;
            case "g" -> 7;
            case "h" -> 8;
            default -> throw new Exception("Error: Not an acceptable letter!");
        };
    }

    public int run(String input) {
        var inputs = input.split(" ");
        switch (inputs[0]) {
            case "leave":
                try {
                    client.leave();
                } catch (Exception e) {
                    System.out.println(SET_TEXT_COLOR_RED + "Error: couldn't leave game");
                }
                return 1;
            case "move":
                if ((inputs.length != 5) && (inputs.length != 6)) {
                    System.out.println(SET_TEXT_COLOR_RED + "Error: Wrong number of arguments");
                    return 1;
                }
                ChessPosition start = null;
                ChessPosition end = null;
                try {
                    start = new ChessPosition(Integer.parseInt(inputs[2]), letterToCol(inputs[1]));
                    end = new ChessPosition(Integer.parseInt(inputs[4]), letterToCol(inputs[3]));
                } catch (Exception e) {
                    System.out.println(SET_TEXT_COLOR_RED + e.getMessage());
                }
                ChessPiece.PieceType promotion = null;
                if (inputs.length == 6) {
                    try {
                        promotion = ChessPiece.PieceType.valueOf(inputs[5]);
                    } catch (Exception e) {
                        System.out.println(SET_TEXT_COLOR_RED + "Error: Not a piece type!");
                    }
                }
                ChessMove move;
                try {
                    move = new ChessMove(start, end, promotion);
                    client.makeMove(move);
                } catch (Exception e) {
                    System.out.println(SET_TEXT_COLOR_RED + "Error: Failed to make move!");
                }
                return 2;
            case "redraw":
                client.redraw(null);
                return 2;
            case "highlight":
                if (inputs.length != 3) {
                    System.out.println(SET_TEXT_COLOR_RED + "Error: Wrong number of arguments");
                    return 1;
                }
                ChessPosition highlight = null;
                try {
                    highlight = new ChessPosition(Integer.parseInt(inputs[2]), letterToCol(inputs[1]));
                } catch (Exception e) {
                    System.out.println(SET_TEXT_COLOR_RED + e.getMessage());
                }
                client.redraw(highlight);
                return 2;
            case "resign":
                Scanner scanner = new Scanner(System.in);
                System.out.print(SET_TEXT_COLOR_YELLOW + "Confirm resignation? (yes/no): ");
                String result = scanner.nextLine();
                if (!Objects.equals(result, "yes")) {
                    System.out.println();
                    System.out.println(RESET_TEXT_COLOR + "Resign aborted");
                    return 2;
                }
                try {
                    client.resign();
                } catch (Exception e) {
                    System.out.println(SET_TEXT_COLOR_RED + "Error: couldn't resign");
                }
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
        System.out.println("'move <LETTER start column> <NUMBER start row> <LETTER end column> <NUMBER end row> [OPTIONAL promotion piece]' to make a move");
        System.out.println("'redraw' to redraw the board");
        System.out.println("'highlight' to highlight legal moves");
        System.out.println("'resign' to forfeit game");
        System.out.println("'help' to print this help message");
    }
}
