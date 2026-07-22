package dataaccess;

import chess.ChessGame;
import model.GameData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import service.BadRequestException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SQLGameDAOTest {
    @BeforeEach
    void setUp() {
        GameDAO gameDAO = new SQLGameDAO();
        try {
            gameDAO.clear();
        } catch (Exception e) {
            System.out.println("Clear failed!");
            fail();
        }
    }

    @Test
    @DisplayName("Valid Game Creation")
    void createGameSuccess() {
        GameDAO gameDAO = new SQLGameDAO();
        int id = -1;
        try {
            id = gameDAO.createGame("new_game");
        } catch (Exception e) {
            fail();
        }
        int finalID = id;
        Assertions.assertDoesNotThrow(() -> gameDAO.getGame(finalID));
    }

    @Test
    @DisplayName("Invalid Game Creation")
    void createGameFail() {
        GameDAO gameDAO = new SQLGameDAO();
        Assertions.assertThrows(Exception.class, () -> gameDAO.createGame(null));
    }

    @Test
    @DisplayName("Valid Get Game")
    void getGameSuccess() {
        GameDAO gameDAO = new SQLGameDAO();
        int id = -1;
        try {
            id = gameDAO.createGame("new_game");
        } catch (Exception e) {
            fail();
        }
        int finalID = id;
        GameData gd = null;
        try {
            gd = gameDAO.getGame(finalID);
        } catch (Exception e) {
            fail();
        }
        ChessGame game = new ChessGame();
        Assertions.assertEquals(gd, new GameData(1, null, null, "new_game", game));
    }

    @Test
    @DisplayName("Invalid Get Game")
    void getGameFail() {
        GameDAO gameDAO = new SQLGameDAO();
        int finalID = 1;
        Assertions.assertThrows(BadRequestException.class, () -> gameDAO.getGame(finalID));
    }

    @Test
    @DisplayName("Contains elements list games")
    void listGamesFull() {
        GameDAO gameDAO = new SQLGameDAO();
        try {
            gameDAO.createGame("game_1");
        } catch (Exception e) {
            fail();
        }
        ChessGame game = new ChessGame();
        GameData game_1 = new GameData(1, null, null, "game_1", game);
        List<GameData> list = new ArrayList<>();
        list.add(game_1);
        try {
            Assertions.assertEquals(list, gameDAO.listGames());
        } catch (Exception e) {
            fail();
        }
        // Add second game
        try {
            gameDAO.createGame("game_2");
        } catch (Exception e) {
            fail();
        }
        GameData game_2 = new GameData(2, null, null, "game_2", game);
        list.add(game_2);
        try {
            Assertions.assertEquals(list, gameDAO.listGames());
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    @DisplayName("Empty list games")
    void listGamesEmpty() {
        GameDAO gameDAO = new SQLGameDAO();
        try {
            Assertions.assertEquals(new ArrayList<>(), gameDAO.listGames());
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    void updateGame() {
    }

    @Test
    void clear() {
    }
}