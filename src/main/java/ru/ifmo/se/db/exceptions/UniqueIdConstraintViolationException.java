package ru.ifmo.se.db.exceptions;

import jakarta.validation.ValidationException;

public class UniqueIdConstraintViolationException extends ValidationException {
    public UniqueIdConstraintViolationException(String message) {
        super(message);
    }
}
