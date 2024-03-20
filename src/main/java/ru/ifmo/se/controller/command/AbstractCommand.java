package ru.ifmo.se.controller.command;

import lombok.Data;

@Data
public abstract class AbstractCommand {
    private final String name;
    private final String description;
    private final int countOfArguments;

    public abstract void execute(String[] args);

    public boolean validateCountOfArguments(String[] args) {
        return args.length == countOfArguments;
    }
}
