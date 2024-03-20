package ru.ifmo.se.controller.command.modification;

import ru.ifmo.se.controller.command.AbstractCommand;
import ru.ifmo.se.service.io.writer.BufferedDataWriter;
import ru.ifmo.se.service.receiver.ModificationReceiver;

public class RemoveByIdCommand extends AbstractCommand {
    private final ModificationReceiver modificationReceiver;
    private final BufferedDataWriter infoWriter;


    public RemoveByIdCommand(String name, String description, ModificationReceiver modificationReceiver, BufferedDataWriter infoWriter) {
        super(name, description, 1);
        this.modificationReceiver = modificationReceiver;
        this.infoWriter = infoWriter;
        
    }

    @Override
    public void execute(String[] args) {
        if (!validateCountOfArguments(args)) {
            infoWriter.writeErrorMessage("Неправильное количество аргументов. Нужно передать только id удаляемого элемента");
            return;
        }

        try {
            long id = Long.parseLong(args[0]);
            boolean removed = modificationReceiver.removeById(id);
            infoWriter.writeInfo(removed ?
                    String.format("Элемент с id: %d успешно удален", id) :
                    String.format("Элемента с id: %d в коллекции не существует", id));
        } catch (NumberFormatException e) {
            infoWriter.writeErrorMessage("Ошибка при парсинге id. Убедитесь, что id представляет собой целое число.");
        }
    }
}
