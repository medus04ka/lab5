package ru.ifmo.se.service.io.writer;

/**
 * The interface Data writer.
 */
public interface DataWriter {

    void writeLine(String line);

    void writeLineWithoutSpace(String line);

    void writeErrorMessage(String message);

    void writeMessage(String message);

    void writeInfo(String message);

    void writeDebugMessage(String message);

    void writeSpace();
}
