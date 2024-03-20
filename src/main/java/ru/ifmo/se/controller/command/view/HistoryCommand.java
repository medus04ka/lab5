package ru.ifmo.se.controller.command.view;

import ru.ifmo.se.controller.command.AbstractCommand;
import ru.ifmo.se.service.io.writer.BufferedDataWriter;

import java.util.LinkedList;

/**
 * The type History command.
 */
public class HistoryCommand extends AbstractCommand {
    private final BufferedDataWriter infoWriter;
    private final LinkedList<String> historyListOfCommand;

    /**
     * Instantiates a new History command.
     *
     * @param name        the name
     * @param description the description
     * @param infoWriter  the info writer
     */
    public HistoryCommand(String name, String description, BufferedDataWriter infoWriter) {
        super(name, description, 0);
        this.infoWriter = infoWriter;
        this.historyListOfCommand = new LinkedList<>();
    }

    @Override
    public void execute(String[] args) {
        if (!validateCountOfArguments(args)) {
            infoWriter.writeErrorMessage("Неправильное количество аргументов. Команда " + getName() + " не требует дополнительных аргументов. ");
            return;
        }

        LinkedList<String> last14Commands = new LinkedList<>(
                historyListOfCommand.subList(
                        Math.max(historyListOfCommand.size() - 14, 0), historyListOfCommand.size()));
        last14Commands.forEach(infoWriter::writeInfo);
    }

    /**
     * Add to command list.
     *
     * @param commandName the command name
     */
    public void addToCommandList(String commandName) {
        historyListOfCommand.add(commandName);
    }
}
