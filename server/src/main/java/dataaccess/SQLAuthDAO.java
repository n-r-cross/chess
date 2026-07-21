package dataaccess;

import model.AuthData;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLAuthDAO implements AuthDAO {

    public SQLAuthDAO() {
        try {
            configureDatabase();
        } catch (SQLException e) {
            throw new RuntimeException("Database could not connect");
        }
    }

    private void configureDatabase() throws SQLException {
        try (var conn = getConnection()) {
            var createDbStatement = conn.prepareStatement("CREATE DATABASE IF NOT EXISTS chess_database");
            createDbStatement.executeUpdate();
            // Statements will automatically take effect in chess_database
            conn.setCatalog("chess_database");
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

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://localhost:3306", "root", "grinnings");
    }

    @Override
    public void createAuth(AuthData a) throws Exception {
        System.out.println(a);
        // Check for existing username
        try (var conn = getConnection()) {
            conn.setCatalog("chess_database");
            // Add auth
            String statement = "INSERT INTO auths (authToken, username) VALUES (?, ?)";
            // Execute SQL statements on the connection here
            try (var insertStatement = conn.prepareStatement(statement)) {
                insertStatement.setString(1, a.authToken());
                insertStatement.setString(2, a.username());
                System.out.println("About to execute");
                insertStatement.executeUpdate();
                System.out.println("Executed!");
            } catch (SQLException e2) {
                throw new Exception("Insert failed");
            }
        }
    }

    @Override
    public AuthData getAuth(String token) throws DataAccessException {
        return null;
    }

    @Override
    public void deleteAuth(String token) throws DataAccessException {

    }

    @Override
    public void clear() throws Exception {
        // Clear data! GA!
        try {
            try (var conn = getConnection()) {
                conn.setCatalog("chess_database");
                // Execute SQL statements on the connection here
                try (var createStatement = conn.prepareStatement("TRUNCATE auths")) {
                    createStatement.executeUpdate();
                }
            }
        } catch (SQLException e) {
            throw new Exception("clear failed");
        }
    }
}
