package ru.ifmo.se.controller.exceptions;

/**
 * The type Command not found.
 */
public class CommandNotFound extends RuntimeException {
    /**
     * Instantiates a new Command not found.
     *
     * @param message the message
     */
    public CommandNotFound(String message) {
        super(message);
    }
}
