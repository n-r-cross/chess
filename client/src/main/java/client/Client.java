package client;

import chess.*;
import model.GameData;
import result.ListResult;
import result.LoginResult;
import result.RegisterResult;
import websocket.messages.ErrorServerMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationServerMessage;
import websocket.messages.ServerMessage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static chess.ChessGame.TeamColor.WHITE;
import static ui.EscapeSequences.*;

public class Client implements NotificationHandler {

    private static ServerFacade serverFacade;
    private static WebSocketFacade webSocketFacade;

    private String authToken = "";
    private int connected = -1;
    private ChessGame.TeamColor perspective = WHITE;
    private ChessGame currentGame;

    private List<GameData> games;

    public Client() throws Exception {
        serverFacade = new ServerFacade("localhost", 8080);
        webSocketFacade = new WebSocketFacade("localhost", 8080, this);
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
            System.out.print(RESET_TEXT_COLOR);
            System.out.print(i + 1);
            System.out.print(") ");
            System.out.print(games.get(i).gameName());
            System.out.print(": WHITE - ");
            System.out.print(games.get(i).whiteUsername());
            System.out.print(" | BLACK - ");
            System.out.print(games.get(i).blackUsername());
            if (games.get(i).complete()) {
                System.out.print(" (FINISHED)");
            }
            System.out.println();
        }
    }

    public void join(int gameNumber, ChessGame.TeamColor color) throws Exception {
        if ((gameNumber < 0) || (gameNumber >= games.size())) {
            throw new Exception("Error: game number doesn't correspond to a game");
        }
        int gameID = games.get(gameNumber).gameID();
        serverFacade.joinGame(color, gameID, authToken);
        perspective = color;
        // printGame(games.get(gameNumber).game(), perspective);
    }

    public void observe(int gameNumber) throws Exception {
        if ((gameNumber < 0) || (gameNumber >= games.size())) {
            throw new Exception("Error: game number doesn't correspond to a game");
        }
        perspective = WHITE;
        printGame(games.get(gameNumber).game(), perspective, null);
    }

    public void connect(int gameNumber) throws Exception {
        int gameID = games.get(gameNumber).gameID();
        connected = gameID;
        webSocketFacade.connect(authToken, gameID);
    }

    public void leave() throws Exception {
        webSocketFacade.leave(authToken, connected);
        connected = -1;
    }

    public void resign() throws Exception {
        webSocketFacade.resign(authToken, connected);
        connected = -1;
    }

    public void makeMove(ChessMove move) throws Exception {
        Collection<ChessMove> list = currentGame.validMoves(move.getStartPosition());
        boolean validated = false;
        for (ChessMove option : list) {
            if (option.equals(move)) {
                validated = true;
                break;
            }
        }
        if (!validated) {
            throw new Exception("Invalid move from " + move.getStartPosition() + " to " + move.getEndPosition());
        }
        webSocketFacade.makeMove(authToken, connected, move);
    }

    public void redraw(ChessPosition position) {
        printGame(currentGame, perspective, position);
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

    private String getBackgroundFormat(int row, int col, ChessGame game, ChessPosition highlight) {
        if (highlight != null) {
            Collection<ChessMove> moves = game.validMoves(highlight);
            Collection<ChessPosition> highlighted = new ArrayList<>();
            for (ChessMove move : moves) {
                highlighted.add(move.getEndPosition());
            }
            String format = "";
            if (((row + col) % 2) == 1) {
                if (highlighted.contains(new ChessPosition(row, col, true))) {
                    format += SET_BG_COLOR_GREEN;
                } else {
                    format += SET_BG_COLOR_LIGHT_GREY;
                }
            } else {
                if (highlighted.contains(new ChessPosition(row, col, true))) {
                    format += SET_BG_COLOR_DARK_GREEN;
                } else {
                    format += SET_BG_COLOR_DARK_GREY;
                }
            }
            if (new ChessPosition(row, col, true).equals(highlight)) {
                format += SET_BG_COLOR_YELLOW;
            }
            return format;
        }
        String format = "";
        if (((row + col) % 2) == 1) {
            format += SET_BG_COLOR_LIGHT_GREY;
        } else {
            format += SET_BG_COLOR_DARK_GREY;
        }
        return format;
    }

    private void printGame(ChessGame game, ChessGame.TeamColor color, ChessPosition highlight) {
        ChessBoard board = game.getBoard();
        String triple_thin = THIN + THIN + THIN;
        // Try to erase screen and reset text color so we print as default
        System.out.print(ERASE_SCREEN);
        System.out.println(RESET_TEXT_COLOR);
        boolean reversed = color != WHITE;
        printHorizontalIndex(reversed);
        int row_start = 7;
        int row_finish = -1;
        int row_change = -1;
        int col_start = 0;
        int col_finish = 8;
        int col_change = 1;
        if (reversed) {
            row_start = 0;
            row_finish = 8;
            row_change = 1;
            col_start = 7;
            col_finish = -1;
            col_change = -1;
        }
        for (int i = row_start; i != row_finish; i += row_change) {
            System.out.print(SET_BG_COLOR_BLACK + triple_thin + (1 + i) + triple_thin);
            for (int j = col_start; j != col_finish; j += col_change) {
                String format = getBackgroundFormat(i, j, game, highlight);
                System.out.print(format + THIN + THIN + getCharForPiece(board.getPiece(i, j)) + THIN + THIN);
            }
            System.out.print(SET_BG_COLOR_BLACK + triple_thin + (1 + i) + triple_thin);
            System.out.println(RESET_BG_COLOR);
        }
        printHorizontalIndex(reversed);
    }

    @Override
    public void notify(ServerMessage notification) {
        System.out.println();
        switch (notification.getServerMessageType()) {
            case LOAD_GAME -> {
                LoadGameMessage message = (LoadGameMessage) notification;
                currentGame = message.getGame();
                printGame(message.getGame(), perspective, null);
            }
            case ERROR -> {
                ErrorServerMessage message = (ErrorServerMessage) notification;
                System.out.println(SET_TEXT_COLOR_RED + message.getMessage());
            }
            case NOTIFICATION -> {
                NotificationServerMessage message = (NotificationServerMessage) notification;
                System.out.println(SET_TEXT_COLOR_BLUE + message.getMessage());
            }
        }
    }
}
