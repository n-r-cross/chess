package server;

import dataaccess.DataAccessException;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import service.Service;

public class ClearHandler implements Handler {
    public void handle(Context context) throws DataAccessException {
        System.out.println("Called handle in ClearHandler!");
        System.out.println(context.body());
        Service.clear();
        context.result();
    }
}
