package ru.ifmo.se.controller.command.modification;

import ru.ifmo.se.controller.command.AbstractCommand;
import ru.ifmo.se.model.HumanBeing;
import ru.ifmo.se.service.io.utils.HumanBeingReader;
import ru.ifmo.se.service.io.writer.BufferedDataWriter;
import ru.ifmo.se.service.receiver.ModificationReceiver;

import java.util.Optional;

public class UpdateCommand extends AbstractCommand {
    private final ModificationReceiver modificationReceiver;
    private final HumanBeingReader humanBeingReader;
    private final BufferedDataWriter infoWriter;


    public UpdateCommand(String name, String description, ModificationReceiver modificationReceiver, HumanBeingReader humanBeingReader, BufferedDataWriter infoWriter) {
        super(name, description, 1);
        this.modificationReceiver = modificationReceiver;
        this.humanBeingReader = humanBeingReader;
        this.infoWriter = infoWriter;

    }

    @Override
    public void execute(String[] args) {
        if (!validateCountOfArguments(args)) {
            infoWriter.writeErrorMessage("Неправильное количество аргументов. Нужно передать только id обновляемого элемента");
            return;
        }

        try {
            long id = Long.parseLong(args[0]);
            Optional<HumanBeing> canBeHumanBeing = modificationReceiver.findById(id);

            if (!canBeHumanBeing.isPresent()) {
                infoWriter.writeErrorMessage(String.format("Элемента с id %d не существует", id));
            } else {
                HumanBeing humanBeing = humanBeingReader.readHumanBeing();
                boolean updated = modificationReceiver.update(id, humanBeing);
                infoWriter.writeInfo(updated ?
                        String.format("Элемент с id %d успешно обновлен", id) :
                        String.format("Элемента с id %d в коллекции не существует", id));
            }
        } catch (NumberFormatException e) {
            infoWriter.writeErrorMessage("Ошибка при парсинге id. Убедитесь, что id представляет собой целое число.");
        }
    }
}
