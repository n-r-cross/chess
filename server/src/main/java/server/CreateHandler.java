package server;

import com.google.gson.Gson;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;
import server.request.CreateRequest;
import server.result.CreateResult;
import service.GameService;

public class CreateHandler implements Handler {
    private static final GameService GAME_SERVICE = new GameService();
    private static final Gson GSON = new Gson();

    @Override
    public void handle(@NotNull Context context) throws Exception {
        // Check authToken
        GAME_SERVICE.validate(context.header("Authorization"));
        // Create request
        CreateRequest request = GSON.fromJson(context.body(), CreateRequest.class);
        // Create game
        CreateResult response = GAME_SERVICE.create(request);
        context.result(GSON.toJson(response));
    }
}
