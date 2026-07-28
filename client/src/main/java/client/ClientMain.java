package client;

import chess.*;
import ui.GameUI;
import ui.PostUI;
import ui.PreUI;

import java.util.Scanner;

public class ClientMain {
    private static PreUI preUI;
    private static PostUI postUI;
    private static GameUI gameUI;

    public static void main(String[] args) {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Client: " + piece);
        Client client = new Client();
        preUI = new PreUI(client);
        postUI = new PostUI(client);
        gameUI = new GameUI(client);

        Scanner scanner = new Scanner(System.in);
        var result = "";

        int state = 0;
        while (state >= 0) {
            switch (state) {
                case 0:
                    preUI.prompt();
                    result = scanner.nextLine();
                    state = preUI.run(result);
                    break;
                case 1:
                    postUI.prompt();
                    result = scanner.nextLine();
                    state = postUI.run(result);
                    break;
                case 2:
                    gameUI.prompt();
                    result = scanner.nextLine();
                    state = gameUI.run(result);
                    break;
            }
        }
    }
}
