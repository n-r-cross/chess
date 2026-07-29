package server;

import com.google.gson.Gson;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;
import request.LoginRequest;
import result.LoginResult;
import service.BadRequestException;
import service.UserService;

public class LoginHandler implements Handler {
    private static final UserService USER_SERVICE = new UserService();
    private static final Gson GSON = new Gson();

    @Override
    public void handle(@NotNull Context context) throws Exception {
        // Rehydrate request
        LoginRequest request = new Gson().fromJson(context.body(), LoginRequest.class);
        if (!request.complete()) {
            throw new BadRequestException("bad request");
        }
        // Validate username and password
        USER_SERVICE.validate(request.username(), request.password());
        LoginResult result = USER_SERVICE.login(request);
        context.result(GSON.toJson(result));
    }
}
