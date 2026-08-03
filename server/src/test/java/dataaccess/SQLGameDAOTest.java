package dataaccess;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import chess.InvalidMoveException;
import model.GameData;
import org.junit.jupiter.api.*;
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
        assertDoesNotThrow(() -> gameDAO.getGame(finalID));
    }

    @Test
    @DisplayName("Invalid Game Creation")
    void createGameFail() {
        GameDAO gameDAO = new SQLGameDAO();
        assertThrows(Exception.class, () -> gameDAO.createGame(null));
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
        assertEquals(gd, new GameData(1, null, null, "new_game", game, false));
    }

    @Test
    @DisplayName("Invalid Get Game")
    void getGameFail() {
        GameDAO gameDAO = new SQLGameDAO();
        int finalID = 1;
        assertThrows(BadRequestException.class, () -> gameDAO.getGame(finalID));
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
        GameData game_1 = new GameData(1, null, null, "game_1", game, false);
        List<GameData> list = new ArrayList<>();
        list.add(game_1);
        try {
            assertEquals(list, gameDAO.listGames());
        } catch (Exception e) {
            fail();
        }
        // Add second game
        try {
            gameDAO.createGame("game_2");
        } catch (Exception e) {
            fail();
        }
        GameData game_2 = new GameData(2, null, null, "game_2", game, false);
        list.add(game_2);
        try {
            assertEquals(list, gameDAO.listGames());
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    @DisplayName("Empty list games")
    void listGamesEmpty() {
        GameDAO gameDAO = new SQLGameDAO();
        try {
            assertEquals(new ArrayList<>(), gameDAO.listGames());
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    @DisplayName("Valid update game")
    void updateGameSuccess() {
        GameDAO gameDAO = new SQLGameDAO();
        GameData game = null;
        try {
            int id = gameDAO.createGame("new_game");
            game = gameDAO.getGame(id);
        } catch (Exception e) {
            fail();
        }
        ChessGame chessGame = new ChessGame();
        try {
            chessGame.makeMove(new ChessMove(new ChessPosition(1, 1, true), new ChessPosition(2, 1, true), null));
        } catch (InvalidMoveException e) {
            fail();
        }
        GameData altered = new GameData(game.gameID(), "Sir-Gasalot", "Sir-Asalot",
                "new_game", chessGame, false);
        Assertions.assertDoesNotThrow(() -> gameDAO.updateGame(altered));
        try {
            Assertions.assertEquals(altered, gameDAO.getGame(game.gameID()));
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    @DisplayName("Invalid update game")
    void updateGameFail() {
        GameDAO gameDAO = new SQLGameDAO();
        GameData game = null;
        try {
            int id = gameDAO.createGame("new_game");
            game = gameDAO.getGame(id);
        } catch (Exception e) {
            fail();
        }
        GameData altered = new GameData(game.gameID() + 1, "Sir-Gasalot", "Sir-Asalot",
                "new_game", new ChessGame(), false);
        Assertions.assertThrows(BadRequestException.class, () -> gameDAO.updateGame(altered));
    }

    @Test
    void clear() {
        GameDAO gameDAO = new SQLGameDAO();
        int id = -1;
        try {
            id = gameDAO.createGame("new_game");
        } catch (Exception e) {
            fail();
        }
        int finalID = id;
        assertDoesNotThrow(() -> gameDAO.getGame(finalID));
        try {
            gameDAO.clear();
        } catch (Exception e) {
            fail();
        }
        try {
            assertEquals(new ArrayList<>(), gameDAO.listGames());
        } catch (Exception e) {
            fail();
        }
    }

    @AfterEach
    void tearDown() {
        GameDAO gameDAO = new SQLGameDAO();
        try {
            gameDAO.clear();
        } catch (Exception e) {
            System.out.println("Clear failed!");
        }
    }
}