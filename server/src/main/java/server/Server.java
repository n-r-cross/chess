package server;

import com.google.gson.Gson;
import io.javalin.*;
import io.javalin.http.Context;
import server.websocket.WebSocketHandler;
import service.BadRequestException;
import service.ForbiddenException;
import service.UnauthorizedException;

import java.util.Map;

public class Server {

    private final Javalin javalin;

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"));

        // Register endpoints
        RegisterHandler registerHandler = new RegisterHandler();
        javalin.post("/user", registerHandler);
        ClearHandler clearHandler = new ClearHandler();
        javalin.delete("/db", clearHandler);
        LoginHandler loginHandler = new LoginHandler();
        javalin.post("/session", loginHandler);
        LogoutHandler logoutHandler = new LogoutHandler();
        javalin.delete("/session", logoutHandler);
        ListHandler listHandler = new ListHandler();
        javalin.get("/game", listHandler);
        CreateHandler createHandler = new CreateHandler();
        javalin.post("/game", createHandler);
        JoinHandler joinHandler = new JoinHandler();
        javalin.put("/game", joinHandler);
        // Register WebSocket endpoint
        WebSocketHandler webSocketHandler = new WebSocketHandler();
        javalin.ws("/ws", ws -> {
            ws.onConnect(webSocketHandler);
            ws.onMessage(webSocketHandler);
            ws.onClose(webSocketHandler);
        });

        // Register exceptions
        javalin.exception(Exception.class, this::exceptionHandler);
        javalin.exception(UnauthorizedException.class, this::unauthorizedExceptionHandler);
        javalin.exception(BadRequestException.class, this::badRequestExceptionHandler);
        javalin.exception(ForbiddenException.class, this::forbiddenExceptionHandler);

    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }

    /**
     * Handle general exceptions
     */
    private void exceptionHandler(Exception e, Context context) {
        var body = new Gson().toJson(Map.of("message",
                String.format("Error: %s", e.getMessage()), "success", false));
        context.status(500);
        context.json(body);
    }

    /**
     * Handle unauthorized exceptions (usually
     * unauthorized for wrong username/password
     * or no authToken)
     */
    private void unauthorizedExceptionHandler(UnauthorizedException e, Context context) {
        var body = new Gson().toJson(Map.of("message",
                String.format("Error: %s", e.getMessage()), "success", false));
        context.status(401);
        context.json(body);
    }

    /**
     * Handle bad request exceptions (usually
     * didn't include enough fields in request or
     * something out of range)
     */
    private void badRequestExceptionHandler(BadRequestException e, Context context) {
        var body = new Gson().toJson(Map.of("message",
                String.format("Error: %s", e.getMessage()), "success", false));
        context.status(400);
        context.json(body);
    }

    /**
     * Handle forbidden exceptions (usually
     * username or team color already taken)
     */
    private void forbiddenExceptionHandler(ForbiddenException e, Context context) {
        var body = new Gson().toJson(Map.of("message",
                String.format("Error: %s", e.getMessage()), "success", false));
        context.status(403);
        context.json(body);
    }

}
