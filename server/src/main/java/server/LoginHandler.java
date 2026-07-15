package server;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;
import service.UserService;

public class LoginHandler implements Handler {
    private static final UserService userService = new UserService();

    @Override
    public void handle(@NotNull Context context) throws DataAccessException {
        System.out.println("Called handle in ClearHandler!");
        System.out.println(context.body());
        LoginRequest request = new Gson().fromJson(context.body(), LoginRequest.class);
        userService.validate(request.username(), request.password());
        LoginResult result = userService.login(request);
        System.out.println(result);

        context.result(new Gson().toJson(result));
    }
}
