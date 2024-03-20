package ru.ifmo.se.service.io.reader;

public interface DataReader {
    String readLine();

    int getLastReadLineNumber();
}
