package server;

import com.google.gson.Gson;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;
import server.request.ListRequest;
import server.result.ListResult;
import service.GameService;

public class ListHandler implements Handler {
    private static final GameService GAME_SERVICE = new GameService();
    private static final Gson GSON = new Gson();

    @Override
    public void handle(@NotNull Context context) throws Exception {
        // Check authToken
        GAME_SERVICE.validate(context.header("Authorization"));
        // Create request
        ListRequest request = new ListRequest(context.header("Authorization"));
        // Get list
        ListResult response = GAME_SERVICE.list(request);
        context.result(GSON.toJson(response));
    }
}
