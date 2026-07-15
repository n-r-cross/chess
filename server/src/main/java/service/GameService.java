package service;

import server.ListRequest;
import server.ListResult;

import java.util.ArrayList;

public class GameService extends Service {

    public ListResult list(ListRequest r) {
        // TODO: create actual ListResult with games
        return new ListResult(new ArrayList<String>());
    }
}
