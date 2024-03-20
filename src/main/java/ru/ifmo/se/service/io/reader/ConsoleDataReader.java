package ru.ifmo.se.service.io.reader;

import java.io.Console;

/**
 * The type Console data reader.
 */
public class ConsoleDataReader implements DataReader {
    private final Console console;
    private int lastReadLineNumber = 0;

    public ConsoleDataReader() {
        this.console = System.console();
        if (console == null) {
            throw new RuntimeException("Console is not available");
        }
    }

    @Override
    public String readLine() {
        lastReadLineNumber++;
        return console.readLine();
    }

    @Override
    public int getLastReadLineNumber() {
        return lastReadLineNumber;
    }
}
