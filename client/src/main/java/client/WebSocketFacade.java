package client;

import chess.ChessMove;
import com.google.gson.Gson;
import jakarta.websocket.*;
import ui.EscapeSequences.*;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorServerMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationServerMessage;

import java.net.URI;
import java.util.Locale;

import static ui.EscapeSequences.RESET_TEXT_COLOR;

public class WebSocketFacade extends Endpoint {
    Session session;
    private static final Gson GSON = new Gson();
    final NotificationHandler notificationHandler;

    public WebSocketFacade(String url, int port, NotificationHandler notificationHandler) throws Exception {
        String urlString = String.format(Locale.getDefault(), "ws://%s:%d/ws", url, port);
        URI socketURI = new URI(urlString);
        this.notificationHandler = notificationHandler;

        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        session = container.connectToServer(this, socketURI);
        session.addMessageHandler(new MessageHandler.Whole<String>() {
            @Override
            public void onMessage(String message) {
                NotificationServerMessage notification = GSON.fromJson(message, NotificationServerMessage.class);
                switch (notification.getServerMessageType()) {
                    case LOAD_GAME -> {
                        LoadGameMessage loadGameMessage = GSON.fromJson(message, LoadGameMessage.class);
                        notificationHandler.notify(loadGameMessage);
                    }
                    case ERROR -> {
                        ErrorServerMessage errorServerMessage = GSON.fromJson(message, ErrorServerMessage.class);
                        notificationHandler.notify(errorServerMessage);
                    }
                    case NOTIFICATION -> {
                        NotificationServerMessage notificationServerMessage = GSON.fromJson(message, NotificationServerMessage.class);
                        notificationHandler.notify(notificationServerMessage);
                    }
                }
            }
        });
    }

    // Endpoint requires method
    @Override
    public void onOpen(Session session, EndpointConfig config) {
    }

    public void connect(String token, int gameID) throws Exception {
        UserGameCommand command = new UserGameCommand(UserGameCommand.CommandType.CONNECT, token, gameID);
        session.getBasicRemote().sendText(GSON.toJson(command));
        System.out.println(RESET_TEXT_COLOR + "Connected!");
    }

    public void leave(String token, int gameID) throws Exception {
        UserGameCommand command = new UserGameCommand(UserGameCommand.CommandType.LEAVE, token, gameID);
        session.getBasicRemote().sendText(GSON.toJson(command));
    }

    public void resign(String token, int gameID) throws Exception {
        UserGameCommand command = new UserGameCommand(UserGameCommand.CommandType.RESIGN, token, gameID);
        session.getBasicRemote().sendText(GSON.toJson(command));
    }

    public void makeMove(String token, int gameID, ChessMove move) throws Exception {
        MakeMoveCommand command = new MakeMoveCommand(token, gameID, move);
        session.getBasicRemote().sendText(GSON.toJson(command));
    }
}
