package server;

import com.google.gson.Gson;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;
import service.GameService;

public class JoinHandler implements Handler {
    private final GameService gameService = new GameService();
    private final Gson gson = new Gson();

    @Override
    public void handle(@NotNull Context context) throws Exception {
        System.out.println("Handling join!");
        gameService.validate(context.header("Authorization"));
        JoinRequest request = gson.fromJson(context.body(), JoinRequest.class);
        gameService.join(request, context.header("Authorization"));
        context.result();
    }
}
