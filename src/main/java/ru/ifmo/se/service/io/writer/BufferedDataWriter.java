package ru.ifmo.se.service.io.writer;

import ru.ifmo.se.service.io.exceptions.RuntimeIOException;
import ru.ifmo.se.service.io.reader.Mode;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class BufferedDataWriter implements DataWriter {
    private final BufferedWriter writer;
    private final Mode mode;

    public static final String ANSI_BRIGHT_PURPLE = "\u001B[95m";
    public static final String ANSI_BRIGHT_CYAN = "\u001B[96m";
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BRIGHT_RED = "\u001B[91m";
    public static final String ANSI_BRIGHT_YELLOW = "\u001B[93m";

    public BufferedDataWriter(Mode mode) {
        this.writer = new BufferedWriter(new OutputStreamWriter(System.out));
        this.mode = mode;
    }

    @Override
    public void writeLine(String line) {
        try {
            writer.write(line);
            writer.newLine();
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeIOException("Error writing line", e);
        }
    }

    @Override
    public void writeLineWithoutSpace(String line) {
        try {
            writer.write(line);
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeIOException("Error writing line without space", e);
        }
    }

    @Override
    public void writeErrorMessage(String message) {
        try {
            writer.write(ANSI_BRIGHT_RED + message + ANSI_RESET);
            writer.newLine();
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeIOException("Error writing error message", e);
        }
    }

    @Override
    public void writeMessage(String message) {
        if (mode.equals(Mode.CONSOLE)) {
            try {
                writer.write(ANSI_BRIGHT_PURPLE + message + ANSI_RESET);
                writer.newLine();
                writer.flush();
            } catch (IOException e) {
                throw new RuntimeIOException("Error writing message", e);
            }
        }
    }

    @Override
    public void writeInfo(String message) {
        try {
            writer.write(ANSI_BRIGHT_CYAN + message + ANSI_RESET);
            writer.newLine();
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeIOException("Error writing message", e);
        }
    }


    @Override
    public void writeDebugMessage(String message) {
        try {
            writer.write("---" + ANSI_BRIGHT_YELLOW + message + ANSI_RESET);
            writer.newLine();
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeIOException("Error writing message", e);
        }
    }

    @Override
    public void writeSpace() {
        try {
            writer.newLine();
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeIOException("Error writing message", e);
        }
    }
}