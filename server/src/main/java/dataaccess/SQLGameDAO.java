package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;
import service.BadRequestException;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class SQLGameDAO implements GameDAO {
    private final Gson gson = new Gson();

    public SQLGameDAO() {
        try {
            // Create database
            DatabaseManager.createDatabase();
            configureDatabase();
        } catch (Exception e) {
            throw new RuntimeException("Database could not connect");
        }
    }

    private void configureDatabase() throws Exception {
        try (var conn = getConnection()) {
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

    private Connection getConnection() throws Exception {
        return DatabaseManager.getConnection();
    }

    @Override
    public int createGame(String gameName) throws Exception {
        try (var conn = getConnection()) {
            conn.setCatalog("chess");
            // Add game
            String statement = "INSERT INTO games (gameName, game) VALUES (?, ?)";
            // Execute SQL statements on the connection here
            try (var insertStatement = conn.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS)) {
                insertStatement.setString(1, gameName);
                ChessGame game = new ChessGame();
                String gameString = gson.toJson(game);
                insertStatement.setString(2, gameString);
                insertStatement.executeUpdate();
                // Get gameID
                var resultSet = insertStatement.getGeneratedKeys();
                var id = 0;
                if (resultSet.next()) {
                    id = resultSet.getInt(1);
                }
                return id;
            } catch (SQLException e2) {
                throw new DataAccessException("Create game failed");
            }
        }
    }

    @Override
    public GameData getGame(int gameID) throws Exception {
        try (var conn = getConnection()) {
            conn.setCatalog("chess");
            // Get game from game ID
            String command = "SELECT gameID, whiteUsername, blackUsername, gameName, game FROM games WHERE gameID=?";
            try (var preparedStatement = conn.prepareStatement(command)) {
                preparedStatement.setInt(1, gameID);
                try (var rs = preparedStatement.executeQuery()) {
                    if (!rs.next()) {
                        // throw exception if not found
                        throw new BadRequestException("bad request");
                    }
                    ChessGame game = gson.fromJson(rs.getString("game"), ChessGame.class);
                    return new GameData(rs.getInt("gameID"), rs.getString("whiteUsername"),
                            rs.getString("blackUsername"), rs.getString("gameName"), game);
                }
            }
        } catch (SQLException e) {
            // throw exception if something went wrong
            throw new DataAccessException("Get game failed!");
        }
    }

    @Override
    public List<GameData> listGames() throws Exception {
        List<GameData> list = new ArrayList<>();
        try (var conn = getConnection()) {
            conn.setCatalog("chess");
            // Get game from game ID
            String command = "SELECT gameID, whiteUsername, blackUsername, gameName, game FROM games";
            try (var preparedStatement = conn.prepareStatement(command)) {
                try (var rs = preparedStatement.executeQuery()) {
                    while (rs.next()) {
                        ChessGame game = gson.fromJson(rs.getString("game"), ChessGame.class);
                        GameData gd = new GameData(rs.getInt("gameID"), rs.getString("whiteUsername"),
                                rs.getString("blackUsername"), rs.getString("gameName"), game);
                        list.add(gd);
                    }
                }
            }
        } catch (SQLException e) {
            // throw exception if something went wrong
            throw new DataAccessException("List games failed!");
        }
        return list;
    }

    @Override
    public void updateGame(GameData game) throws Exception {
        try (var conn = getConnection()) {
            conn.setCatalog("chess");
            String updateString = "UPDATE games SET whiteUsername=?, blackUsername=?, game=? WHERE gameID=?";
            try (var preparedStatement = conn.prepareStatement(updateString)) {
                preparedStatement.setString(1, game.whiteUsername());
                preparedStatement.setString(2, game.blackUsername());
                preparedStatement.setString(3, gson.toJson(game.game()));
                preparedStatement.setInt(4, game.gameID());
                int effect = preparedStatement.executeUpdate();
                if (effect == 0) {
                    throw new BadRequestException("bad request");
                }
            }
        } catch (SQLException e) {
            // throw exception if something went wrong
            throw new DataAccessException("Update game failed!");
        }
    }

    @Override
    public void clear() throws DataAccessException {
        // Clear data! GA!
        try {
            try (var conn = getConnection()) {
                conn.setCatalog("chess");
                // Execute SQL statements on the connection here
                try (var createStatement = conn.prepareStatement("TRUNCATE games")) {
                    createStatement.executeUpdate();
                }
            }
        } catch (Exception e) {
            throw new DataAccessException("clear failed");
        }
    }
}
