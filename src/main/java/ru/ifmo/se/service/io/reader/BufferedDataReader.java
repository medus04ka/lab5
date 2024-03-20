package ru.ifmo.se.service.io.reader;

import lombok.RequiredArgsConstructor;
import ru.ifmo.se.service.io.exceptions.RuntimeIOException;

import java.io.BufferedReader;
import java.io.IOException;

@RequiredArgsConstructor
public class BufferedDataReader implements DataReader {
    private final BufferedReader reader;
    private int lastReadLineNumber = 0;

    @Override
    public String readLine() {
        try {
            String line = reader.readLine();
            lastReadLineNumber++;
            return line;
        } catch (IOException e) {
            throw new RuntimeIOException("Error reading line", e);
        }
    }

    @Override
    public int getLastReadLineNumber() {
        return lastReadLineNumber;
    }
}
