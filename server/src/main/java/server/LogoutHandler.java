package server;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;
import service.BadRequestException;
import service.UserService;

public class LogoutHandler implements Handler {
    private static final UserService USER_SERVICE = new UserService();

    @Override
    public void handle(@NotNull Context context) throws Exception {
        System.out.println("Handling logout");
        if (!context.body().isEmpty()) {
            throw new BadRequestException("bad request");
        }
        LogoutRequest request = new LogoutRequest(context.header("Authorization"));
        USER_SERVICE.logout(request);
    }
}
