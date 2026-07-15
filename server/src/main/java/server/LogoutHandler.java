package server;

import dataaccess.DataAccessException;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;
import service.UserService;

public class LogoutHandler implements Handler {
    private static final UserService userService = new UserService();

    @Override
    public void handle(@NotNull Context context) throws DataAccessException {
        System.out.println("Handling logout");
        LogoutRequest request = new LogoutRequest(context.header("Authorization"));
        userService.logout(request);
    }
}
