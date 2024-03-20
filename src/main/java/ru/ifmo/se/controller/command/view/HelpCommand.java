package ru.ifmo.se.controller.command.view;

import ru.ifmo.se.controller.command.AbstractCommand;
import ru.ifmo.se.service.io.writer.BufferedDataWriter;
import ru.ifmo.se.service.receiver.ViewReceiver;

/**
 * The type Help command.
 */
public class HelpCommand extends AbstractCommand {
    private final ViewReceiver viewReceiver;
    private final BufferedDataWriter infoWriter;
    private String helpManual;

    /**
     * Instantiates a new Help command.
     *
     * @param name         the name
     * @param description  the description
     * @param viewReceiver the view receiver
     * @param infoWriter   the info writer
     */
    public HelpCommand(String name, String description, ViewReceiver viewReceiver, BufferedDataWriter infoWriter) {
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

        infoWriter.writeInfo(helpManual);
    }

    /**
     * Sets help manual.
     *
     * @param helpManual the help manual
     */
    public void setHelpManual(String helpManual) {
        this.helpManual = helpManual;
    }
}
