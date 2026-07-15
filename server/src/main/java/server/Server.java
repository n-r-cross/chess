package server;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import io.javalin.*;
import io.javalin.http.Context;
import service.BadRequestException;
import service.ForbiddenException;

import java.util.Map;

public class Server {

    private final Javalin javalin;

    private final RegisterHandler registerHandler = new RegisterHandler();
    private final ClearHandler clearHandler = new ClearHandler();
    private final LoginHandler loginHandler = new LoginHandler();
    private final LogoutHandler logoutHandler = new LogoutHandler();

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"));

        // Register your endpoints and exception handlers here.
        javalin.post("/user", registerHandler);
        javalin.delete("/db", clearHandler);
        javalin.post("/session", loginHandler);
        javalin.delete("/session", logoutHandler);
        javalin.exception(Exception.class, this::exceptionHandler);
        javalin.exception(DataAccessException.class, this::dataAccessExceptionHandler);
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

    private void exceptionHandler(Exception e, Context context) {
        var body = new Gson().toJson(Map.of("message",
                String.format("Error: %s", e.getMessage()), "success", false));
        context.status(500);
        context.json(body);
    }

    private void dataAccessExceptionHandler(DataAccessException e, Context context) {
        var body = new Gson().toJson(Map.of("message",
                String.format("Error: %s", e.getMessage()), "success", false));
        context.status(401);
        context.json(body);
    }

    private void badRequestExceptionHandler(BadRequestException e, Context context) {
        var body = new Gson().toJson(Map.of("message",
                String.format("Error: %s", e.getMessage()), "success", false));
        context.status(400);
        context.json(body);
    }

    private void forbiddenExceptionHandler(ForbiddenException e, Context context) {
        var body = new Gson().toJson(Map.of("message",
                String.format("Error: %s", e.getMessage()), "success", false));
        context.status(403);
        context.json(body);
    }

}
