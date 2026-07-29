package server;

import com.google.gson.Gson;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;
import result.ListResult;
import service.GameService;

public class ListHandler implements Handler {
    private static final GameService GAME_SERVICE = new GameService();
    private static final Gson GSON = new Gson();

    @Override
    public void handle(@NotNull Context context) throws Exception {
        // Check authToken
        GAME_SERVICE.validate(context.header("Authorization"));
        // Get list
        ListResult response = GAME_SERVICE.list();
        context.result(GSON.toJson(response));
    }
}
