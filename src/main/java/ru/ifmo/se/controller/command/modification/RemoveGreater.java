package ru.ifmo.se.controller.command.modification;

import ru.ifmo.se.controller.command.AbstractCommand;
import ru.ifmo.se.model.HumanBeing;
import ru.ifmo.se.service.io.utils.HumanBeingReader;
import ru.ifmo.se.service.io.writer.BufferedDataWriter;
import ru.ifmo.se.service.receiver.ModificationReceiver;

public class RemoveGreater extends AbstractCommand {
    private final ModificationReceiver modificationReceiver;
    private final HumanBeingReader humanBeingReader;
    private final BufferedDataWriter infoWriter;


    public RemoveGreater(String name, String description, ModificationReceiver modificationReceiver, HumanBeingReader humanBeingReader, BufferedDataWriter infoWriter) {
        super(name, description, 0);
        this.modificationReceiver = modificationReceiver;
        this.humanBeingReader = humanBeingReader;
        this.infoWriter = infoWriter;

    }

    @Override
    public void execute(String[] args) {
        if (!validateCountOfArguments(args)) {
            infoWriter.writeErrorMessage("Неправильное количество аргументов. Команда " + getName() + " не требует дополнительных аргументов. ");
            return;
        }

        HumanBeing humanBeing = humanBeingReader.readHumanBeing();
        long countRemoved = modificationReceiver.removeGreater(humanBeing);
        infoWriter.writeInfo("Количество удаленных элементов: %d".formatted(countRemoved));
    }
}
