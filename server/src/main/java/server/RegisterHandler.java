package server;

import com.google.gson.Gson;
import io.javalin.http.Context;
import io.javalin.http.Handler;

import request.RegisterRequest;
import result.RegisterResult;
import service.BadRequestException;
import service.UserService;

public class RegisterHandler implements Handler {
    private static final UserService USER_SERVICE = new UserService();
    private static final Gson GSON = new Gson();

    public void handle(Context context) throws Exception {
        // Rehydrate request
        RegisterRequest request = GSON.fromJson(context.body(), RegisterRequest.class);
        // Check for bad request
        if (!request.complete()) {
            throw new BadRequestException("bad request");
        }
        // Register user
        RegisterResult response = USER_SERVICE.register(request);
        context.result(GSON.toJson(response));
    }
}