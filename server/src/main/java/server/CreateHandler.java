package server;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;

public class CreateHandler implements Handler {
    @Override
    public void handle(@NotNull Context context) throws Exception {
        System.out.println("Handling create!");
    }
}
