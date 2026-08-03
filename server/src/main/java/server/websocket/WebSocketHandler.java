package server.websocket;

import chess.ChessGame;
import com.google.gson.Gson;
import io.javalin.websocket.*;
import org.jetbrains.annotations.NotNull;
import service.PlayService;
import websocket.commands.UserGameCommand;
import websocket.messages.*;

import java.util.Objects;

public class WebSocketHandler implements WsConnectHandler, WsMessageHandler, WsCloseHandler {
    private final ConnectionManager connections = new ConnectionManager();
    private final PlayService playService = new PlayService();
    private static final Gson GSON = new Gson();

    @Override
    public void handleConnect(@NotNull WsConnectContext context) throws Exception {
        context.enableAutomaticPings();
        System.out.println("Connected to game");
    }

    private String getJoinNotification(String username, ChessGame.TeamColor color) {
        String msg = username + " has joined the game as ";
        msg += Objects.requireNonNullElse(color, "OBSERVER!");
        return msg;
    }

    @Override
    public void handleMessage(@NotNull WsMessageContext context) throws Exception {
        System.out.println(context.message());
        UserGameCommand command = GSON.fromJson(context.message(), UserGameCommand.class);
        switch (command.getCommandType()) {
            case CONNECT -> {
                System.out.println("Connecting!");
                ChessGame game;
                String username;
                try {
                    game = playService.getGame(command.getGameID()).game();
                } catch (Exception e) {
                    ErrorServerMessage errorServerMessage = new ErrorServerMessage("Invalid game ID");
                    context.send(GSON.toJson(errorServerMessage));
                    break;
                }
                try {
                    username = playService.getAuth(command.getAuthToken()).username();
                } catch (Exception e) {
                    ErrorServerMessage errorServerMessage = new ErrorServerMessage("Invalid auth token");
                    context.send(GSON.toJson(errorServerMessage));
                    break;
                }
                ServerMessage serverMessage = new LoadGameMessage(game);
                System.out.println(serverMessage);
                context.send(GSON.toJson(serverMessage));
                // Send notification to concerned parties
                ChessGame.TeamColor color = null;
                if (Objects.equals(playService.getGame(command.getGameID()).whiteUsername(), username)) {
                    color = ChessGame.TeamColor.WHITE;
                }
                if (Objects.equals(playService.getGame(command.getGameID()).blackUsername(), username)) {
                    color = ChessGame.TeamColor.BLACK;
                }
                String msg = getJoinNotification(username, color);
                connections.broadcast(command.getGameID(), new NotificationServerMessage(msg));
                //
                connections.add(command.getGameID(), context.session);
            }
            case MAKE_MOVE -> {
                System.out.println("Making move!");
            }
            case LEAVE -> {
                System.out.println("Leaving");
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
