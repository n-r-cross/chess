package server;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import service.BadRequestException;
import service.ClearService;

public class ClearHandler implements Handler {
    private final ClearService clearService = new ClearService();

    public void handle(Context context) throws Exception {
        System.out.println("Called handle in ClearHandler!");
        System.out.println(context.body());
        if (!context.body().isEmpty()) {
            throw new BadRequestException("bad request");
        }
        clearService.clear();
        context.result();
    }
}
