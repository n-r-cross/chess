package server;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import io.javalin.*;
import io.javalin.http.Context;

import java.util.Map;

public class Server {

    private final Javalin javalin;

    private final RegisterHandler registerHandler = new RegisterHandler();
    private final ClearHandler clearHandler = new ClearHandler();

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"));

        // Register your endpoints and exception handlers here.
        javalin.post("/user", registerHandler);
        javalin.delete("/db", clearHandler);
        javalin.exception(Exception.class, this::exceptionHandler);

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

}
