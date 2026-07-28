package ui;

import client.Client;

import java.util.Scanner;

public class PreUI {
    private final Client client;

    public PreUI(Client client) {
        this.client = client;
    }

    public void prompt() {
        System.out.print("Logged out >>> ");
    }

    public int run(String input) {
        System.out.println("Not logged in");
        System.out.println("\'" + input + "\'");
        var inputs = input.split(" ");

        switch (inputs[0]) {
            case "quit":
                return -1;
            case "login":
                System.out.println("Running login");
                try {
                    client.login(inputs[1], inputs[2]);
                } catch (Exception e) {
                    System.out.println("Command failed. (did you include username and password?)");
                    return 0;
                }
                return 1;
            case "register":
                System.out.println("Running register");
                return 1;
            case "help":
                System.out.println("Running help");
                help();
                return 0;
            default:
                System.out.println("Invalid command :(. Type 'help' for list of valid commands");
                return 0;
        }
    }

    public void help() {
        System.out.println("\'register\' to create an account");
        System.out.println("\'login\' to play chess");
        System.out.println("\'quit\' to stop playing");
        System.out.println("\'help\' to print this help message");
    }
}