package ui;

import client.Client;

public class PreUI {
    private final Client client;

    public PreUI(Client client) {
        this.client = client;
    }

    private boolean incorrectArgCount(int expected, int got) {
        return expected != got;
    }

    public void prompt() {
        System.out.print("Logged out >>> ");
    }

    public int run(String input) {
        var inputs = input.split(" ");

        switch (inputs[0]) {
            case "quit":
                return -1;
            case "login":
                if (incorrectArgCount(3, inputs.length)) {
                    System.out.println("Wrong number of arguments");
                    return 0;
                }
                try {
                    client.login(inputs[1], inputs[2]);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    return 0;
                }
                return 1;
            case "register":
                if (incorrectArgCount(4, inputs.length)) {
                    System.out.println("Wrong number of arguments");
                    return 0;
                }
                try {
                    client.register(inputs[1], inputs[2], inputs[3]);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    return 0;
                }
                return 1;
            case "help":
                help();
                return 0;
            default:
                System.out.println("Invalid command :(. Type 'help' for list of valid commands");
                return 0;
        }
    }

    public void help() {
        System.out.println("'register <username> <password> <email>' to create an account");
        System.out.println("'login <username> <password>' to play chess");
        System.out.println("'quit' to stop playing");
        System.out.println("'help' to print this help message");
    }
}