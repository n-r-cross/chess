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
    private static final ConnectionManager connections = new ConnectionManager();
    private final PlayService playService = new PlayService();
    private static final Gson GSON = new Gson();

    @Override
    public void handleConnect(@NotNull WsConnectContext context) throws Exception {
        context.enableAutomaticPings();
        System.out.println("Connected to game");
    }

    private String getJoinNotification(String username, int gameID) throws Exception {
        ChessGame.TeamColor color = null;
        if (Objects.equals(playService.getGame(gameID).whiteUsername(), username)) {
            color = ChessGame.TeamColor.WHITE;
        }
        if (Objects.equals(playService.getGame(gameID).blackUsername(), username)) {
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

    private String getStatusNotification(GameData data) throws Exception {
        return null;
    }

    @Override
    public void handleMessage(@NotNull WsMessageContext context) throws Exception {
        UserGameCommand command = GSON.fromJson(context.message(), UserGameCommand.class);
        switch (command.getCommandType()) {
            case CONNECT -> {
                ChessGame game;
                String username;
                // Try to get username from auth token
                try {
                    username = playService.getAuth(command.getAuthToken()).username();
                } catch (Exception e) {
                    // Invalid auth token
                    ErrorServerMessage errorServerMessage = new ErrorServerMessage("Invalid auth token");
                    context.send(GSON.toJson(errorServerMessage));
                    break;
                }
                // Try to get game from game id
                try {
                    game = playService.getGame(command.getGameID()).game();
                } catch (Exception e) {
                    // Invalid game id
                    ErrorServerMessage errorServerMessage = new ErrorServerMessage("Invalid game ID");
                    context.send(GSON.toJson(errorServerMessage));
                    break;
                }
                ServerMessage serverMessage = new LoadGameMessage(game);
                context.send(GSON.toJson(serverMessage));
                // Send notification to concerned parties
                String msg = getJoinNotification(username, command.getGameID());
                connections.broadcast(command.getGameID(), new NotificationServerMessage(msg));
                // Add current session to concerned parties
                connections.add(command.getGameID(), context.session);
            }
            case MAKE_MOVE -> {
                System.out.println("Making move!");
                MakeMoveCommand moveCommand = GSON.fromJson(context.message(), MakeMoveCommand.class);
                GameData gameData;
                String username;
                // Try to get username from auth token
                try {
                    username = playService.getAuth(command.getAuthToken()).username();
                } catch (Exception e) {
                    // Invalid auth token
                    ErrorServerMessage errorServerMessage = new ErrorServerMessage("Invalid auth token");
                    context.send(GSON.toJson(errorServerMessage));
                    break;
                }
                // Try to get game from game id
                try {
                    gameData = playService.getGame(command.getGameID());
                } catch (Exception e) {
                    // Invalid game id
                    ErrorServerMessage errorServerMessage = new ErrorServerMessage("Invalid game ID");
                    context.send(GSON.toJson(errorServerMessage));
                    break;
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
                    break;
                }
                // Try to make move
                try {
                    gameData.game().makeMove(moveCommand.getMove());
                } catch (Exception e) {
                    ErrorServerMessage errorServerMessage = new ErrorServerMessage("Invalid move");
                    context.send(GSON.toJson(errorServerMessage));
                    break;
                }
                playService.updateGame(gameData);
                // Send load game to all parties
                LoadGameMessage loadGameMessage = new LoadGameMessage(gameData.game());
                connections.broadcast(gameData.gameID(), loadGameMessage);
                // Remove current session for move made broadcast
                connections.remove(context.session);
                // Broadcast move made
                String moveMessage = getMoveNotification(username, gameData.gameID(), moveCommand.getMove());
                // Check status of game
                String statusMessage = getStatusNotification(gameData);
                NotificationServerMessage notificationServerMessage = new NotificationServerMessage(moveMessage);
                connections.broadcast(gameData.gameID(), notificationServerMessage);
                if (statusMessage != null) {
                    notificationServerMessage = new NotificationServerMessage(statusMessage);
                    connections.broadcast(gameData.gameID(), notificationServerMessage);
                }
                // Add current session back in for future broadcasts
                connections.add(command.getGameID(), context.session);


            }
            case LEAVE -> {
                String username;
                GameData game;
                // Try to get username from auth token
                try {
                    username = playService.getAuth(command.getAuthToken()).username();
                } catch (Exception e) {
                    // Invalid auth token
                    ErrorServerMessage errorServerMessage = new ErrorServerMessage("Invalid auth token");
                    context.send(GSON.toJson(errorServerMessage));
                    break;
                }
                // Try to get game from game id
                try {
                    game = playService.getGame(command.getGameID());
                } catch (Exception e) {
                    // Invalid game id
                    ErrorServerMessage errorServerMessage = new ErrorServerMessage("Invalid game ID");
                    context.send(GSON.toJson(errorServerMessage));
                    break;
                }
                // Update game to remove user
                String whiteUsername = game.whiteUsername();
                String blackUsername = game.blackUsername();
                if (username.equals(game.blackUsername())) {
                    blackUsername = null;
                }
                if (username.equals(game.whiteUsername())) {
                    whiteUsername = null;
                }
                GameData new_game = new GameData(game.gameID(), whiteUsername, blackUsername, game.gameName(), game.game(), game.complete());
                // Generate left notification before removing username
                String msg = getLeftNotification(username, command.getGameID());
                // Update game
                playService.updateGame(new_game);
                // Remove session from concerned parties
                connections.remove(context.session);
                // Send notification to concerned parties
                connections.broadcast(command.getGameID(), new NotificationServerMessage(msg));
            }
            case RESIGN -> {
                System.out.println("Resign");
            }
        }


    }

    @Override
    public void handleClose(@NotNull WsCloseContext context) throws Exception {
        System.out.println("Websocket closed");
    }

}
