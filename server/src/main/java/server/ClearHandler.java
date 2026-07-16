package server;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import service.BadRequestException;
import service.ClearService;

public class ClearHandler implements Handler {
    private static final ClearService CLEAR_SERVICE = new ClearService();

    public void handle(Context context) throws Exception {
        // Check request
        if (!context.body().isEmpty()) {
            throw new BadRequestException("bad request");
        }
        CLEAR_SERVICE.clear();
        context.result();
    }
}
