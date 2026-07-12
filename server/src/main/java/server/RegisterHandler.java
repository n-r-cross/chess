package server;

import com.google.gson.Gson;
import io.javalin.http.Context;
import io.javalin.http.Handler;

record RegisterRequest(String username, String password, String email){}

public class RegisterHandler implements Handler {
    public void handle(Context context) {
        System.out.println("Called handle in RegisterHandler!");
        System.out.println(context.body());
        RegisterRequest request = new Gson().fromJson(context.body(), RegisterRequest.class);
        System.out.println(request);

        context.result(new Gson().toJson(request));
    }
}