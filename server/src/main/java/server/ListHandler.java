package server;

import com.google.gson.Gson;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;
import server.request.ListRequest;
import server.result.ListResult;
import service.GameService;

public class ListHandler implements Handler {
    private final GameService gameService = new GameService();
    private final Gson gson = new Gson();


    @Override
    public void handle(@NotNull Context context) throws Exception {
        System.out.println("Handling list!");
        gameService.validate(context.header("Authorization"));
        ListRequest request = new ListRequest(context.header("Authorization"));
        ListResult response = gameService.list(request);
        context.result(gson.toJson(response));
    }
}
