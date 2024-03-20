package ru.ifmo.se.controller.command.modification;

import ru.ifmo.se.controller.command.AbstractCommand;
import ru.ifmo.se.service.io.writer.BufferedDataWriter;

public class ExitCommand extends AbstractCommand {
    private final BufferedDataWriter infoWriter;

    public ExitCommand(String name, String description, BufferedDataWriter infoPrinter) {
        super(name, description, 0);
        this.infoWriter = infoPrinter;
    }

    @Override
    public void execute(String[] args) {
        if (!validateCountOfArguments(args)) {
            infoWriter.writeErrorMessage("Неправильное количество аргументов. Команда " + getName() + " не требует дополнительных аргументов. ");
            return;
        }

        infoWriter.writeMessage("До свидания!");
        System.exit(0);

    }
}
