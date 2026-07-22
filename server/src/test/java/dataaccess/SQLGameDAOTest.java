package dataaccess;

import chess.ChessGame;
import model.GameData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import service.BadRequestException;

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
    void listGames() {
    }

    @Test
    void updateGame() {
    }

    @Test
    void clear() {
    }
}