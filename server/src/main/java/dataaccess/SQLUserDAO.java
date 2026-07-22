package dataaccess;

import model.UserData;
import org.mindrot.jbcrypt.BCrypt;
import service.ForbiddenException;
import service.UnauthorizedException;

import java.sql.Connection;
import java.sql.SQLException;

public class SQLUserDAO implements UserDAO {

    public SQLUserDAO() {
        try {
            // Create database
            DatabaseManager.createDatabase();
            configureDatabase();
        } catch (Exception e) {
            throw new RuntimeException("Database could not connect");
        }
    }

    private String hashPassword(String clearTextPassword) {
        // Return a hashed version of the password
        return BCrypt.hashpw(clearTextPassword, BCrypt.gensalt());
    }

    private void configureDatabase() throws Exception {
        try (var conn = getConnection()) {
            // Statements will automatically take effect in chess_database
            conn.setCatalog("chess");
            // SQL code to create table of user data
            var createUsersTable = """
                    CREATE TABLE  IF NOT EXISTS users (
                        username VARCHAR(255) NOT NULL,
                        password VARCHAR(255) NOT NULL,
                        email VARCHAR(255) NOT NULL,
                        PRIMARY KEY (username)
                    )""";
            // Execute command to create users table
            try (var createTableStatement = conn.prepareStatement(createUsersTable)) {
                createTableStatement.executeUpdate();
            }
        }
    }

    private Connection getConnection() throws Exception {
        return DatabaseManager.getConnection();
    }

    @Override
    public void insertUser(UserData u) throws Exception {
        // Check for existing username
        try (var conn = getConnection()) {
            conn.setCatalog("chess");
            String command = "SELECT username, password FROM users WHERE username=?";
            try (var preparedStatement = conn.prepareStatement(command)) {
                preparedStatement.setString(1, u.username());
                try (var rs = preparedStatement.executeQuery()) {
                    // throw exception if found
                    if (rs.next()) {
                        throw new ForbiddenException("already taken");
                    }
                }
            }
            // Add user
            String statement = "INSERT INTO users (username, password, email) VALUES (?, ?, ?)";
            // Execute SQL statements on the connection here
            try (var insertStatement = conn.prepareStatement(statement)) {
                insertStatement.setString(1, u.username());
                insertStatement.setString(2, hashPassword(u.password()));
                insertStatement.setString(3, u.email());
                insertStatement.executeUpdate();
            } catch (SQLException e2) {
                throw new DataAccessException("Insert failed");
            }
        }
    }

    @Override
    public UserData getUser(String username) throws Exception {
        try (var conn = getConnection()) {
            conn.setCatalog("chess");
            // Check for existing username and return UserData
            String command = "SELECT username, password, email FROM users WHERE username=?";
            try (var preparedStatement = conn.prepareStatement(command)) {
                preparedStatement.setString(1, username);
                try (var rs = preparedStatement.executeQuery()) {
                    if (!rs.next()) {
                        // throw exception if not found
                        throw new UnauthorizedException("unauthorized");
                    }
                    return new UserData(rs.getString("username"), rs.getString("password"), rs.getString("email"));
                }
            }
        } catch (SQLException e) {
            // throw exception if something went wrong
            throw new DataAccessException("Get user failed!");
        }

    }

    @Override
    public void clear() throws DataAccessException {
        // Clear data! GA!
        try {
            try (var conn = getConnection()) {
                conn.setCatalog("chess");
                // Execute SQL statements on the connection here
                try (var createStatement = conn.prepareStatement("TRUNCATE users")) {
                    createStatement.executeUpdate();
                }
            }
        } catch (Exception e) {
            throw new DataAccessException("clear failed");
        }
    }
}
