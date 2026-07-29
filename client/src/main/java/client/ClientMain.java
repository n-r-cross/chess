package client;

import chess.*;
import ui.GameUI;
import ui.PostUI;
import ui.PreUI;

import java.util.Scanner;

public class ClientMain {

    public static void main(String[] args) {
        System.out.println("♕ 240 Chess Client");
        Client client = new Client();
        PreUI preUI = new PreUI(client);
        PostUI postUI = new PostUI(client);
        GameUI gameUI = new GameUI(client);

        preUI.help();

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
