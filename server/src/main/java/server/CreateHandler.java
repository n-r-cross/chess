package server;

import com.google.gson.Gson;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;
import server.request.CreateRequest;
import server.result.CreateResult;
import service.GameService;

public class CreateHandler implements Handler {
    private final GameService gameService = new GameService();
    private final Gson gson = new Gson();

    @Override
    public void handle(@NotNull Context context) throws Exception {
        System.out.println("Handling create!");
        gameService.validate(context.header("Authorization"));
        CreateRequest request = gson.fromJson(context.body(), CreateRequest.class);
        CreateResult response = gameService.create(request);
        context.result(gson.toJson(response));
    }
}
