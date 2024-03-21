package ru.ifmo.se.controller.command.view;

import ru.ifmo.se.controller.command.AbstractCommand;
import ru.ifmo.se.service.io.writer.BufferedDataWriter;
import ru.ifmo.se.service.receiver.ViewReceiver;

/**
 * The type Sum of impact speed.
 */
public class SumOfImpactSpeed extends AbstractCommand {
    private final ViewReceiver viewReceiver;
    private final BufferedDataWriter infoWriter;

    public SumOfImpactSpeed(String name, String description, ViewReceiver viewReceiver, BufferedDataWriter infoWriter) {
        super(name, description, 0);
        this.viewReceiver = viewReceiver;
        this.infoWriter = infoWriter;
    }

    @Override
    public void execute(String[] args) {
        if (!validateCountOfArguments(args)) {
            infoWriter.writeErrorMessage("Неправильное количество аргументов. Команда " + getName() + " не требует дополнительных аргументов. ");
            return;
        }

        double sumOfImpactSpeed = viewReceiver.getSumOfImpactSpeed();
        infoWriter.writeInfo("Cумма значений impactSpeed для всех элементов коллекции: %f".formatted(sumOfImpactSpeed));
    }
}
