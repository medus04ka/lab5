package ru.ifmo.se.service.io.exceptions;

public class FileReadPermissionException extends RuntimeException {

    public FileReadPermissionException(String message) {
        super(message);
    }
}
