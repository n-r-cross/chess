package server;

import com.google.gson.Gson;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;
import server.request.JoinRequest;
import service.GameService;

public class JoinHandler implements Handler {
    private static final GameService GAME_SERVICE = new GameService();
    private static final Gson GSON = new Gson();

    @Override
    public void handle(@NotNull Context context) throws Exception {
        // Check authToken
        GAME_SERVICE.validate(context.header("Authorization"));
        // Rehydrate request
        JoinRequest request = GSON.fromJson(context.body(), JoinRequest.class);
        if (!request.complete()) {
        }
        // Call join, including authToken to get username
        GAME_SERVICE.join(request, context.header("Authorization"));
        context.result();
    }
}
