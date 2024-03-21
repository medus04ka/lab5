package ru.ifmo.se.controller.command.view;

import ru.ifmo.se.controller.command.AbstractCommand;
import ru.ifmo.se.model.HumanBeing;
import ru.ifmo.se.service.io.utils.HumanBeingOutputHelper;
import ru.ifmo.se.service.io.writer.BufferedDataWriter;
import ru.ifmo.se.service.receiver.ViewReceiver;

import java.util.Collection;
import java.util.Comparator;
import java.util.stream.Collectors;

/**
 * The type Show command.
 */
public class ShowCommand extends AbstractCommand {
    private final ViewReceiver viewReceiver;
    private final BufferedDataWriter infoWriter;
    private final HumanBeingOutputHelper humanBeingOutputHelper;


    public ShowCommand(String name, String description, ViewReceiver viewReceiver, BufferedDataWriter infoWriter) {
        super(name, description, 0);
        this.viewReceiver = viewReceiver;
        this.infoWriter = infoWriter;
        this.humanBeingOutputHelper = new HumanBeingOutputHelper(infoWriter);
    }

    @Override
    public void execute(String[] args) {
        if (!validateCountOfArguments(args)) {
            infoWriter.writeErrorMessage("Неправильное количество аргументов. Команда " + getName() + " не требует дополнительных аргументов. ");
            return;
        }
        Collection<HumanBeing> humanBeings = viewReceiver.findAll();
        humanBeings = humanBeings.stream().sorted(Comparator.comparingLong(HumanBeing::getId)).collect(Collectors.toList());
        humanBeingOutputHelper.showCollectionHumanBeing(humanBeings);
    }
}
