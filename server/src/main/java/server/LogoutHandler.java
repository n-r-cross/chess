package server;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;
import request.LogoutRequest;
import service.BadRequestException;
import service.UserService;

public class LogoutHandler implements Handler {
    private static final UserService USER_SERVICE = new UserService();

    @Override
    public void handle(@NotNull Context context) throws Exception {
        // Check if body was included and throw exception
        if (!context.body().isEmpty()) {
            throw new BadRequestException("bad request");
        }
        // Create request
        LogoutRequest request = new LogoutRequest(context.header("Authorization"));
        USER_SERVICE.logout(request);
    }
}
