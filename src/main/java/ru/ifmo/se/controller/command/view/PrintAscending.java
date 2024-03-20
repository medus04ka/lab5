package ru.ifmo.se.controller.command.view;

import ru.ifmo.se.controller.command.AbstractCommand;
import ru.ifmo.se.model.HumanBeing;
import ru.ifmo.se.service.io.utils.HumanBeingOutputHelper;
import ru.ifmo.se.service.io.writer.BufferedDataWriter;
import ru.ifmo.se.service.receiver.ViewReceiver;

import java.util.Collection;

/**
 * The type Print ascending.
 */
public class PrintAscending extends AbstractCommand {
    private final ViewReceiver viewReceiver;
    private final BufferedDataWriter infoWriter;
    private final HumanBeingOutputHelper humanBeingOutputHelper;

    /**
     * Instantiates a new Print ascending.
     *
     * @param name         the name
     * @param description  the description
     * @param viewReceiver the view receiver
     * @param infoWriter   the info writer
     */
    public PrintAscending(String name, String description, ViewReceiver viewReceiver, BufferedDataWriter infoWriter) {
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

        Collection<HumanBeing> humanBeings = viewReceiver.getAscendingOrder();
        if (humanBeings.isEmpty()) {
            infoWriter.writeInfo("В коллекции еще нет данных");
        } else {
            humanBeingOutputHelper.showCollectionHumanBeing(humanBeings);
        }
    }
}
