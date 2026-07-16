package server;

import com.google.gson.Gson;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;
import server.request.LoginRequest;
import server.result.LoginResult;
import service.BadRequestException;
import service.UserService;

public class LoginHandler implements Handler {
    private static final UserService USER_SERVICE = new UserService();

    @Override
    public void handle(@NotNull Context context) throws Exception {
        System.out.println("Called handle in ClearHandler!");
        System.out.println(context.body());
        LoginRequest request = new Gson().fromJson(context.body(), LoginRequest.class);
        if (!request.complete()) {
            throw new BadRequestException("bad request");
        }
        USER_SERVICE.validate(request.username(), request.password());
        LoginResult result = USER_SERVICE.login(request);
        System.out.println(result);

        context.result(new Gson().toJson(result));
    }
}
