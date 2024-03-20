package ru.ifmo.se.controller;

import ru.ifmo.se.controller.command.AbstractCommand;
import ru.ifmo.se.controller.command.CommandManager;
import ru.ifmo.se.controller.command.view.HistoryCommand;
import ru.ifmo.se.controller.exceptions.CommandNotFound;

import java.util.Arrays;

public class Invoker {
    private final CommandManager commandManager;
    private final HistoryCommand historyCommand;

    public Invoker(CommandManager commandManager) {
        this.commandManager = commandManager;
        this.historyCommand = (HistoryCommand) commandManager.getCommand("history");
    }

    public void execute(String line) {
        String[] args = simplifyIncomingLine(line);
        String commandName = args[0];
        historyCommand.addToCommandList(commandName);
        AbstractCommand command = commandManager.getCommand(args[0]);
        if (command == null) {
            throw new CommandNotFound("Команда с именем %s не существует. Воспользуйтесь help для вывода доступных команд"
                    .formatted(commandName));
        }
        command.execute(prepareParameters(args));
    }

    private String[] prepareParameters(String[] arguments) {
        return Arrays.copyOfRange(arguments, 1, arguments.length);
    }

    private String[] simplifyIncomingLine(String line) {
        if (line == null || line.isEmpty() || line.trim().isEmpty()) {
            throw new CommandNotFound("\nКоманда с таким именем %s не существует. Воспользуйтесь help для вывода доступных команд".formatted(line));
        }
        return line.trim().replaceAll("\\s+", " ").split(" ");
    }
}
