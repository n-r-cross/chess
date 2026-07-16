package service;

/**
 * Indicates that username or playerColor is already taken
 */
public class ForbiddenException extends Exception {
    public ForbiddenException(String message) {
        super(message);
    }

    public ForbiddenException(String message, Throwable ex) {
        super(message, ex);
    }
}