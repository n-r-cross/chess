package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;
import service.BadRequestException;
import service.ForbiddenException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

public class SQLGameDAO implements GameDAO {
    private final Gson gson = new Gson();

    public SQLGameDAO() {
        try {
            configureDatabase();
        } catch (SQLException e) {
            throw new RuntimeException("Database could not connect");
        }
    }

    private void configureDatabase() throws SQLException {
        try (var conn = getConnection()) {
            // Create database
            var createDbStatement = conn.prepareStatement("CREATE DATABASE IF NOT EXISTS chess");
            createDbStatement.executeUpdate();
            // Statements will automatically take effect in chess_database
            conn.setCatalog("chess");
            // SQL code to create table of user data
            var createGamesTable = """
                    CREATE TABLE IF NOT EXISTS games (
                        gameID INT NOT NULL AUTO_INCREMENT,
                        whiteUsername VARCHAR(255),
                        blackUsername VARCHAR(255),
                        gameName VARCHAR(255) NOT NULL,
                        game longtext NOT NULL,
                        PRIMARY KEY (gameID)
                    )""";
            // Execute command to create users table
            try (var createTableStatement = conn.prepareStatement(createGamesTable)) {
                createTableStatement.executeUpdate();
            }
        }
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://localhost:3306", "root", "grinnings");
    }

    @Override
    public int createGame(String gameName) throws Exception {
        try (var conn = getConnection()) {
            conn.setCatalog("chess");
            // Add game
            String statement = "INSERT INTO games (gameName, game) VALUES (?, ?)";
            // Execute SQL statements on the connection here
            try (var insertStatement = conn.prepareStatement(statement)) {
                insertStatement.setString(1, gameName);
                ChessGame game = new ChessGame();
                String gameString = gson.toJson(game);
                insertStatement.setString(2, gameString);
                insertStatement.executeUpdate();
            } catch (SQLException e2) {
                throw new Exception("Insert failed");
            }
        }
        return 0;
    }

    @Override
    public GameData getGame(int gameID) throws BadRequestException {
        return null;
    }

    @Override
    public List<GameData> listGames() {
        return List.of();
    }

    @Override
    public void updateGame(GameData game) throws BadRequestException {

    }

    @Override
    public void clear() throws Exception {
        // Clear data! GA!
        try {
            try (var conn = getConnection()) {
                conn.setCatalog("chess");
                // Execute SQL statements on the connection here
                try (var createStatement = conn.prepareStatement("TRUNCATE users")) {
                    createStatement.executeUpdate();
                }
            }
        } catch (SQLException e) {
            throw new Exception("clear failed");
        }
    }
}
