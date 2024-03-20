package ru.ifmo.se.controller.command.modification;

import ru.ifmo.se.controller.command.AbstractCommand;
import ru.ifmo.se.service.io.writer.BufferedDataWriter;
import ru.ifmo.se.service.receiver.ModificationReceiver;

public class ClearCommand extends AbstractCommand {
    private final ModificationReceiver modificationReceiver;
    private final BufferedDataWriter infoWriter;


    public ClearCommand(String name, String description, ModificationReceiver modificationReceiver, BufferedDataWriter infoWriter) {
        super(name, description, 0);
        this.modificationReceiver = modificationReceiver;
        this.infoWriter = infoWriter;
        
    }

    @Override
    public void execute(String[] args) {
        if (!validateCountOfArguments(args)) {
            infoWriter.writeErrorMessage("Неправильное количество аргументов. Команда " + getName() + " не требует дополнительных аргументов. ");
            return;
        }
        modificationReceiver.clear();
        infoWriter.writeMessage("Данные успешно удалены");
    }
}
