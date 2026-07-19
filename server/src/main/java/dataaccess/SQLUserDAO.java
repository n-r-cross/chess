package dataaccess;

import model.UserData;
import service.ForbiddenException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLUserDAO implements UserDAO {

    public SQLUserDAO() {
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
            var createUsersTable = """
                    CREATE TABLE  IF NOT EXISTS users (
                        username VARCHAR(255) NOT NULL,
                        password VARCHAR(255) NOT NULL,
                        string VARCHAR(255) NOT NULL,
                        PRIMARY KEY (username)
                    )""";
            // Execute command to create users table
            try (var createTableStatement = conn.prepareStatement(createUsersTable)) {
                createTableStatement.executeUpdate();
            }
        }
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://localhost:3306", "root", "grinnings");
    }

    private void makeSQLStatementCall(String command) throws SQLException {
        //TODO: add check for sql injection
        try (var conn = getConnection()) {
            conn.setCatalog("chess_database");
            // Execute SQL statements on the connection here
            try (var createStatement = conn.prepareStatement(command)) {
                createStatement.executeUpdate();
            }
        }
    }

    private java.sql.ResultSet makeSQLQueryCall(String command) throws SQLException {
        //TODO: add check for sql injection
        try (var conn = getConnection()) {
            conn.setCatalog("chess_database");
            try (var preparedStatement = conn.prepareStatement(command)) {
                try (var rs = preparedStatement.executeQuery()) {
                    return rs;
                }
            }
        }
    }

    @Override
    public void insertUser(UserData u) throws ForbiddenException {
        // Check for existing username
        throw new ForbiddenException("already taken");
        // Add user
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        // Check for existing username and return UserData
        try (var rs = makeSQLQueryCall(
                "SELECT username, password, email FROM users WHERE username='" + username + "'")) {
            return new UserData(rs.getString("username"), rs.getString("password"), rs.getString("email"));
        } catch (SQLException e) {
            // throw exception if not found
            throw new DataAccessException("unauthorized");
        }

    }

    @Override
    public void clear() {
        // Clear data! GA!
        try {
            makeSQLStatementCall("TRUNCATE users");
        } catch (SQLException e) {
            throw new RuntimeException("clear failed");
        }
    }
}
