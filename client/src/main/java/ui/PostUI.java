package ui;

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
                    System.out.println("Wrong number of arguments");
                    return 1;
                }
                try {
                    client.create(inputs[1]);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                return 1;
            default:
                System.out.println("Invalid command :(. Type 'help' for list of valid commands");
                return 1;
        }
    }

    public void help() {
        System.out.println("\'logout\' to log out of your account");
        System.out.println("\'help\' to print this help message");
    }
}
