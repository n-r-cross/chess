package server.websocket;

import chess.ChessGame;
import chess.ChessMove;
import com.google.gson.Gson;
import io.javalin.websocket.*;
import model.GameData;
import org.jetbrains.annotations.NotNull;
import service.PlayService;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.*;

import java.util.Objects;

public class WebSocketHandler implements WsConnectHandler, WsMessageHandler, WsCloseHandler {
    private static final ConnectionManager CONNECTIONS = new ConnectionManager();
    private final PlayService playService = new PlayService();
    private static final Gson GSON = new Gson();

    @Override
    public void handleConnect(@NotNull WsConnectContext context) throws Exception {
        context.enableAutomaticPings();
        System.out.println("Connected to game");
    }

    private String getJoinNotification(String username, GameData gameData) {
        ChessGame.TeamColor color = null;
        if (Objects.equals(gameData.whiteUsername(), username)) {
            color = ChessGame.TeamColor.WHITE;
        }
        if (Objects.equals(gameData.blackUsername(), username)) {
            color = ChessGame.TeamColor.BLACK;
        }
        String msg = username + " has joined the game as ";
        msg += Objects.requireNonNullElse(color, "OBSERVER!");
        return msg;
    }

    private String getLeftNotification(String username, int gameID) throws Exception {
        ChessGame.TeamColor color = null;
        if (Objects.equals(playService.getGame(gameID).whiteUsername(), username)) {
            color = ChessGame.TeamColor.WHITE;
        }
        if (Objects.equals(playService.getGame(gameID).blackUsername(), username)) {
            color = ChessGame.TeamColor.BLACK;
        }
        String msg = username + " (";
        msg += Objects.requireNonNullElse(color, "OBSERVER");
        msg += ") has left the game";
        return msg;
    }

    private String getResignNotification(String username, int gameID) throws Exception {
        ChessGame.TeamColor color = null;
        if (Objects.equals(playService.getGame(gameID).whiteUsername(), username)) {
            color = ChessGame.TeamColor.WHITE;
        }
        if (Objects.equals(playService.getGame(gameID).blackUsername(), username)) {
            color = ChessGame.TeamColor.BLACK;
        }
        if (color == null) {
            return null;
        }
        String msg = username + " (";
        msg += color;
        msg += ") has resigned";
        return msg;
    }

    private String getMoveNotification(String username, int gameID, ChessMove move) throws Exception {
        ChessGame.TeamColor color = null;
        if (Objects.equals(playService.getGame(gameID).whiteUsername(), username)) {
            color = ChessGame.TeamColor.WHITE;
        }
        if (Objects.equals(playService.getGame(gameID).blackUsername(), username)) {
            color = ChessGame.TeamColor.BLACK;
        }
        String msg = username + " (";
        msg += Objects.requireNonNullElse(color, "OBSERVER");
        msg += ") has made a move from ";
        msg += move.getStartPosition();
        msg += " to ";
        msg += move.getEndPosition();
        return msg;
    }

    private String getStatusNotification(ChessGame.TeamColor oppColor, GameData data) {
        ChessGame.TeamColor color = oppColor == ChessGame.TeamColor.WHITE ? ChessGame.TeamColor.BLACK : ChessGame.TeamColor.WHITE;
        String username = color == ChessGame.TeamColor.WHITE ? data.whiteUsername() : data.blackUsername();
        String msg = username + "(";
        msg += color;
        msg += ") is in ";
        if (data.game().isInCheckmate(color)) {
            msg += "CHECKMATE!";
            return msg;
        }
        if (data.game().isInStalemate(color)) {
            msg += "STALEMATE!";
            return msg;
        }
        if (data.game().isInCheck(color)) {
            msg += "CHECK!";
            return msg;
        }
        return null;
    }

    private boolean gameEnded(ChessGame.TeamColor oppColor, GameData data) {
        ChessGame.TeamColor color = oppColor == ChessGame.TeamColor.WHITE ? ChessGame.TeamColor.BLACK : ChessGame.TeamColor.WHITE;
        return ((data.game().isInCheckmate(color)) || (data.game().isInStalemate(color)));
    }

    private void handleConnectMessage(@NotNull WsMessageContext context) throws Exception {
        UserGameCommand command = GSON.fromJson(context.message(), UserGameCommand.class);
        String username = playService.getAuth(command.getAuthToken()).username();
        GameData gameData = playService.getGame(command.getGameID());
        ServerMessage serverMessage = new LoadGameMessage(gameData.game());
        context.send(GSON.toJson(serverMessage));
        // Send notification to concerned parties
        String msg = getJoinNotification(username, gameData);
        CONNECTIONS.broadcast(command.getGameID(), new NotificationServerMessage(msg));
        // Add current session to concerned parties
        CONNECTIONS.add(command.getGameID(), context.session);
    }

    private void handleMakeMoveMessage(@NotNull WsMessageContext context) throws Exception {
        UserGameCommand command = GSON.fromJson(context.message(), UserGameCommand.class);
        MakeMoveCommand moveCommand = GSON.fromJson(context.message(), MakeMoveCommand.class);
        String username = playService.getAuth(command.getAuthToken()).username();
        GameData gameData = playService.getGame(command.getGameID());
        // Check if game is active
        if (gameData.complete()) {
            // Game is over
            ErrorServerMessage errorServerMessage = new ErrorServerMessage("Game already over");
            context.send(GSON.toJson(errorServerMessage));
            return;
        }
        // Check if your turn (prohibit observers and opponent from making move)
        ChessGame.TeamColor color = null;
        if (username.equals(gameData.blackUsername())) {
            color = ChessGame.TeamColor.BLACK;
        }
        if (username.equals(gameData.whiteUsername())) {
            color = ChessGame.TeamColor.WHITE;
        }
        if (gameData.game().getTeamTurn() != color) {
            ErrorServerMessage errorServerMessage = new ErrorServerMessage("Not your turn");
            context.send(GSON.toJson(errorServerMessage));
            return;
        }
        // Try to make move
        try {
            gameData.game().makeMove(moveCommand.getMove());
        } catch (Exception e) {
            ErrorServerMessage errorServerMessage = new ErrorServerMessage("Invalid move");
            context.send(GSON.toJson(errorServerMessage));
            return;
        }
        playService.updateGame(gameData);
        // Send load game to all parties
        LoadGameMessage loadGameMessage = new LoadGameMessage(gameData.game());
        CONNECTIONS.broadcast(gameData.gameID(), loadGameMessage);
        // Remove current session for move made broadcast
        CONNECTIONS.remove(context.session);
        // Broadcast move made
        String moveMessage = getMoveNotification(username, gameData.gameID(), moveCommand.getMove());
        if (gameEnded(color, gameData)) {
            GameData completed = new GameData(gameData.gameID(), gameData.whiteUsername(),
                    gameData.blackUsername(), gameData.gameName(), gameData.game(), true);
            playService.updateGame(completed);
        }
        // Check status of game
        String statusMessage = getStatusNotification(color, gameData);
        NotificationServerMessage notificationServerMessage = new NotificationServerMessage(moveMessage);
        CONNECTIONS.broadcast(gameData.gameID(), notificationServerMessage);
        // Add current session back in for future broadcasts
        CONNECTIONS.add(command.getGameID(), context.session);
        if (statusMessage != null) {
            notificationServerMessage = new NotificationServerMessage(statusMessage);
            CONNECTIONS.broadcast(gameData.gameID(), notificationServerMessage);
        }

    }

    private void handleLeaveMessage(@NotNull WsMessageContext context) throws Exception {
        UserGameCommand command = GSON.fromJson(context.message(), UserGameCommand.class);
        String username = playService.getAuth(command.getAuthToken()).username();
        GameData gameData = playService.getGame(command.getGameID());
        // Update game to remove user
        String whiteUsername = gameData.whiteUsername();
        String blackUsername = gameData.blackUsername();
        if (username.equals(gameData.blackUsername())) {
            blackUsername = null;
        }
        if (username.equals(gameData.whiteUsername())) {
            whiteUsername = null;
        }
        GameData new_game = new GameData(gameData.gameID(), whiteUsername, blackUsername,
                gameData.gameName(), gameData.game(), gameData.complete());
        // Generate left notification before removing username
        String msg = getLeftNotification(username, command.getGameID());
        // Update game
        playService.updateGame(new_game);
        // Remove session from concerned parties
        CONNECTIONS.remove(context.session);
        // Send notification to concerned parties
        CONNECTIONS.broadcast(command.getGameID(), new NotificationServerMessage(msg));
    }

    private void handleResignMessage(@NotNull WsMessageContext context) throws Exception {
        UserGameCommand command = GSON.fromJson(context.message(), UserGameCommand.class);
        String username = playService.getAuth(command.getAuthToken()).username();
        GameData gameData = playService.getGame(command.getGameID());
        // Check if game is active
        if (gameData.complete()) {
            // Game is over
            ErrorServerMessage errorServerMessage = new ErrorServerMessage("Game already over");
            context.send(GSON.toJson(errorServerMessage));
            return;
        }
        // Check if player
        ChessGame.TeamColor color = null;
        if (username.equals(gameData.blackUsername())) {
            color = ChessGame.TeamColor.BLACK;
        }
        if (username.equals(gameData.whiteUsername())) {
            color = ChessGame.TeamColor.WHITE;
        }
        if (color == null) {
            ErrorServerMessage errorServerMessage = new ErrorServerMessage("Not your turn");
            context.send(GSON.toJson(errorServerMessage));
            return;
        }
        GameData new_game = new GameData(gameData.gameID(), gameData.whiteUsername(), gameData.blackUsername(),
                gameData.gameName(), gameData.game(), true);
        // Generate left notification before removing username
        String msg = getResignNotification(username, command.getGameID());
        // Update game
        playService.updateGame(new_game);
        // Send notification to concerned parties
        CONNECTIONS.broadcast(command.getGameID(), new NotificationServerMessage(msg));
    }

    @Override
    public void handleMessage(@NotNull WsMessageContext context) throws Exception {
        UserGameCommand command = GSON.fromJson(context.message(), UserGameCommand.class);
        // Try to get username from auth token
        try {
            playService.getAuth(command.getAuthToken());
        } catch (Exception e) {
            // Invalid auth token
            ErrorServerMessage errorServerMessage = new ErrorServerMessage("Invalid auth token");
            context.send(GSON.toJson(errorServerMessage));
            return;
        }
        // Try to get game from game id
        try {
            playService.getGame(command.getGameID());
        } catch (Exception e) {
            // Invalid game id
            ErrorServerMessage errorServerMessage = new ErrorServerMessage("Invalid game ID");
            context.send(GSON.toJson(errorServerMessage));
            return;
        }
        switch (command.getCommandType()) {
            case CONNECT -> handleConnectMessage(context);
            case MAKE_MOVE -> handleMakeMoveMessage(context);
            case LEAVE -> handleLeaveMessage(context);
            case RESIGN -> handleResignMessage(context);
        }


    }

    @Override
    public void handleClose(@NotNull WsCloseContext context) throws Exception {
        System.out.println("Websocket closed");
    }

}
