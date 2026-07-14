package server;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import io.javalin.http.Context;
import io.javalin.http.Handler;

import service.UserService;

public class RegisterHandler implements Handler {
    private final UserService userService = new UserService();

    public void handle(Context context) throws DataAccessException {
        System.out.println("Called handle in RegisterHandler!");
        System.out.println(context.body());
        RegisterRequest request = new Gson().fromJson(context.body(), RegisterRequest.class);
        System.out.println(request);
        RegisterResponse response = userService.register(request);
        context.result(new Gson().toJson(response));
    }
}