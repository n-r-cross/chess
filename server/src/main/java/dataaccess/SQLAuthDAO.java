package dataaccess;

import model.AuthData;
import service.UnauthorizedException;

import java.sql.Connection;
import java.sql.SQLException;

public class SQLAuthDAO implements AuthDAO {

    public SQLAuthDAO() {
        try {
            configureDatabase();
        } catch (Exception e) {
            throw new RuntimeException("Database could not connect");
        }
    }

    private void configureDatabase() throws Exception {
        try (var conn = getConnection()) {
            // Create database
            DatabaseManager.createDatabase();
            // Statements will automatically take effect in chess
            conn.setCatalog("chess");
            // SQL code to create table of user data
            var createAuthsTable = """
                    CREATE TABLE  IF NOT EXISTS auths (
                        authToken VARCHAR(255) NOT NULL,
                        username VARCHAR(255) NOT NULL,
                        PRIMARY KEY (authToken)
                    )""";
            // Execute command to create users table
            try (var createTableStatement = conn.prepareStatement(createAuthsTable)) {
                createTableStatement.executeUpdate();
            }
        }
    }

    private Connection getConnection() throws Exception {
        return DatabaseManager.getConnection();
    }

    @Override
    public void createAuth(AuthData a) throws Exception {
        try (var conn = getConnection()) {
            conn.setCatalog("chess");
            // Add auth
            String statement = "INSERT INTO auths (authToken, username) VALUES (?, ?)";
            // Execute SQL statements on the connection here
            try (var insertStatement = conn.prepareStatement(statement)) {
                insertStatement.setString(1, a.authToken());
                insertStatement.setString(2, a.username());
                insertStatement.executeUpdate();
            } catch (SQLException e2) {
                throw new DataAccessException("Insert failed");
            }
        }
    }

    @Override
    public AuthData getAuth(String token) throws Exception {
        try (var conn = getConnection()) {
            conn.setCatalog("chess");
            // Check for existing auth and return AuthData
            String command = "SELECT authToken,username FROM auths WHERE authToken=?";
            try (var preparedStatement = conn.prepareStatement(command)) {
                preparedStatement.setString(1, token);
                try (var rs = preparedStatement.executeQuery()) {
                    if (!rs.next()) {
                        // throw exception if not found
                        throw new UnauthorizedException("unauthorized");
                    }
                    return new AuthData(rs.getString("authToken"), rs.getString("username"));
                }
            }
        } catch (SQLException e) {
            // throw exception if something went wrong
            throw new DataAccessException("Get auth failed!");
        }
    }

    @Override
    public void deleteAuth(String token) throws Exception {
        try (var conn = getConnection()) {
            conn.setCatalog("chess");
            // Delete from auths
            String command = "DELETE FROM auths WHERE authToken=?";
            try (var preparedStatement = conn.prepareStatement(command)) {
                preparedStatement.setString(1, token);
                // Execute update and check how many rows were deleted
                if (preparedStatement.executeUpdate() == 0) {
                    // If none deleted, token isn't in database
                    throw new UnauthorizedException("unauthorized");
                }
            }
        } catch (SQLException e) {
            // throw exception if something went wrong
            throw new DataAccessException("Get auth failed!");
        }
    }

    @Override
    public void clear() throws DataAccessException {
        // Clear data! GA!
        try {
            try (var conn = getConnection()) {
                conn.setCatalog("chess");
                // Execute SQL statements on the connection here
                try (var createStatement = conn.prepareStatement("TRUNCATE auths")) {
                    createStatement.executeUpdate();
                }
            }
        } catch (Exception e) {
            throw new DataAccessException("clear failed");
        }
    }
}
