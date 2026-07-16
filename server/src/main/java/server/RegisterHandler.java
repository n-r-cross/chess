package server;

import com.google.gson.Gson;
import io.javalin.http.Context;
import io.javalin.http.Handler;

import server.request.RegisterRequest;
import server.result.RegisterResult;
import service.BadRequestException;
import service.UserService;

public class RegisterHandler implements Handler {
    private final UserService userService = new UserService();

    public void handle(Context context) throws Exception {
        System.out.println("Called handle in RegisterHandler!");
        System.out.println(context.body());
        RegisterRequest request = new Gson().fromJson(context.body(), RegisterRequest.class);
        if (!request.complete()) {
            throw new BadRequestException("bad request");
        }
        System.out.println(request);
        RegisterResult response = userService.register(request);
        context.result(new Gson().toJson(response));
    }
}