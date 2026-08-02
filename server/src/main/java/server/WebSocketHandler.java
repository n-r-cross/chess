package server;

import com.google.gson.Gson;
import io.javalin.websocket.*;
import org.jetbrains.annotations.NotNull;
import websocket.commands.UserGameCommand;
import websocket.messages.LoadGameMessage;
import websocket.messages.ServerMessage;

public class WebSocketHandler implements WsConnectHandler, WsMessageHandler, WsCloseHandler {
    private static final Gson GSON = new Gson();

    @Override
    public void handleConnect(@NotNull WsConnectContext context) throws Exception {
        context.enableAutomaticPings();
        context.send("Connected, ga!");
        System.out.println("Connected to game");
    }

    @Override
    public void handleMessage(@NotNull WsMessageContext context) throws Exception {
        System.out.println(context.message());
        UserGameCommand userGameCommand = GSON.fromJson(context.message(), UserGameCommand.class);
        ServerMessage serverMessage = new LoadGameMessage(userGameCommand.getGameID());
        System.out.println(serverMessage.toString());
        context.send(GSON.toJson(serverMessage));

    }

    @Override
    public void handleClose(@NotNull WsCloseContext context) throws Exception {
        System.out.println("Websocket closed");
    }

}
