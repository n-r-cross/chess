package client;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import model.GameData;
import result.ListResult;
import result.LoginResult;
import result.RegisterResult;

import java.util.List;

import static ui.EscapeSequences.*;

public class Client {

    private static ServerFacade serverFacade;

    private String authToken = "";

    private List<GameData> games;

    public Client() {
        serverFacade = new ServerFacade("localhost", 8080);
    }

    public void login(String username, String password) throws Exception {
        LoginResult lr = serverFacade.login(username, password);
        if ((lr == null) || (lr.authToken() == null)) {
            System.out.println("Login failed");
            return;
        }
        authToken = lr.authToken();
    }

    public void register(String username, String password, String email) throws Exception {
        RegisterResult rr = serverFacade.register(username, password, email);
        authToken = rr.authToken();
    }

    public void logout() throws Exception {
        serverFacade.logout(authToken);
        authToken = "";
    }

    public void create(String gameName) throws Exception {
        serverFacade.createGame(gameName, authToken);
        System.out.println("Create succeeded! Try listing games to see it!");
    }

    public void list() throws Exception {
        ListResult lr = serverFacade.listGames(authToken);
        games = lr.games();
        for (int i = 0; i < games.size(); i++) {
            System.out.print(i + 1);
            System.out.print(") ");
            System.out.print(games.get(i).gameName());
            System.out.print(": WHITE - ");
            System.out.print(games.get(i).whiteUsername());
            System.out.print(" | BLACK - ");
            System.out.println(games.get(i).blackUsername());
        }
    }

    public void join(int gameNumber, ChessGame.TeamColor color) throws Exception {
        if ((gameNumber < 0) || (gameNumber >= games.size())) {
            throw new Exception("Error: game number doesn't correspond to a game");
        }
        int gameID = games.get(gameNumber).gameID();
        serverFacade.joinGame(color, gameID, authToken);
        printGame(gameNumber, color);
    }

    public void observe(int gameNumber) throws Exception {
        if ((gameNumber < 0) || (gameNumber >= games.size())) {
            throw new Exception("Error: game number doesn't correspond to a game");
        }
        printGame(gameNumber, ChessGame.TeamColor.WHITE);
    }

    private String getCharForPiece(ChessPiece piece) {
        if (piece == null) {
            return EMPTY;
        }
        if (piece.getTeamColor() == ChessGame.TeamColor.BLACK) {
            return switch (piece.getPieceType()) {
                case KING -> BLACK_KING;
                case QUEEN -> BLACK_QUEEN;
                case BISHOP -> BLACK_BISHOP;
                case KNIGHT -> BLACK_KNIGHT;
                case ROOK -> BLACK_ROOK;
                case PAWN -> BLACK_PAWN;
            };
        } else {
            return switch (piece.getPieceType()) {
                case KING -> WHITE_KING;
                case QUEEN -> WHITE_QUEEN;
                case BISHOP -> WHITE_BISHOP;
                case KNIGHT -> WHITE_KNIGHT;
                case ROOK -> WHITE_ROOK;
                case PAWN -> WHITE_PAWN;
            };
        }
    }

    private void printHorizontalIndex(boolean reversed) {
        String triple_thin = THIN + THIN + THIN;
        System.out.print(SET_BG_COLOR_BLACK + THIN + THIN + EMPTY + THIN + THIN);
        if (reversed) {
            System.out.print(SET_BG_COLOR_BLACK + triple_thin + "h" + triple_thin);
            System.out.print(SET_BG_COLOR_BLACK + triple_thin + "g" + triple_thin);
            System.out.print(SET_BG_COLOR_BLACK + triple_thin + "f" + triple_thin);
            System.out.print(SET_BG_COLOR_BLACK + triple_thin + "e" + triple_thin);
            System.out.print(SET_BG_COLOR_BLACK + triple_thin + "d" + triple_thin);
            System.out.print(SET_BG_COLOR_BLACK + triple_thin + "c" + triple_thin);
            System.out.print(SET_BG_COLOR_BLACK + triple_thin + "b" + triple_thin);
            System.out.print(SET_BG_COLOR_BLACK + triple_thin + "a" + triple_thin);
        } else {
            System.out.print(SET_BG_COLOR_BLACK + triple_thin + "a" + triple_thin);
            System.out.print(SET_BG_COLOR_BLACK + triple_thin + "b" + triple_thin);
            System.out.print(SET_BG_COLOR_BLACK + triple_thin + "c" + triple_thin);
            System.out.print(SET_BG_COLOR_BLACK + triple_thin + "d" + triple_thin);
            System.out.print(SET_BG_COLOR_BLACK + triple_thin + "e" + triple_thin);
            System.out.print(SET_BG_COLOR_BLACK + triple_thin + "f" + triple_thin);
            System.out.print(SET_BG_COLOR_BLACK + triple_thin + "g" + triple_thin);
            System.out.print(SET_BG_COLOR_BLACK + triple_thin + "h" + triple_thin);
        }
        System.out.print(SET_BG_COLOR_BLACK + THIN + THIN + EMPTY + THIN + THIN);
        System.out.println(RESET_BG_COLOR);
    }

    private void printGame(int gameNumber, ChessGame.TeamColor color) {
        ChessGame game = games.get(gameNumber).game();
        ChessBoard board = game.getBoard();
        String triple_thin = THIN + THIN + THIN;
        System.out.print(ERASE_SCREEN);
        boolean reversed = color != ChessGame.TeamColor.WHITE;
        printHorizontalIndex(reversed);
        int start = 0;
        int finish = 8;
        int change = 1;
        if (reversed) {
            start = 8;
            finish = 0;
            change = -1;
        }
        for (int i = start; i != finish; i += change) {
            System.out.print(SET_BG_COLOR_BLACK + triple_thin + (8 - i) + triple_thin);
            for (int j = 7; j >= 0; j--) {
                String format = "";
                if (((i + j) % 2) == 1) {
                    format += SET_BG_COLOR_LIGHT_GREY;
                } else {
                    format += SET_BG_COLOR_DARK_GREY;
                }
                System.out.print(format + THIN + THIN + getCharForPiece(board.getPiece(i, j)) + THIN + THIN);
            }
            System.out.print(SET_BG_COLOR_BLACK + triple_thin + (8 - i) + triple_thin);
            System.out.println(RESET_BG_COLOR);
        }
        printHorizontalIndex(reversed);
    }
}
