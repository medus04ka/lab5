package ru.ifmo.se.db.exceptions;

public class DatabaseLoadingException extends RuntimeException {
    public DatabaseLoadingException(String message) {
        super(message);
    }

    public DatabaseLoadingException(String message, Throwable cause) {
        super(message, cause);
    }
}
