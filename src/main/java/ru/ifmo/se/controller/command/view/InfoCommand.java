package ru.ifmo.se.controller.command.view;

import ru.ifmo.se.controller.command.AbstractCommand;
import ru.ifmo.se.db.data.DatabaseMetaData;
import ru.ifmo.se.service.io.writer.BufferedDataWriter;
import ru.ifmo.se.service.receiver.ViewReceiver;

import java.time.format.DateTimeFormatter;

/**
 * The type Info command.
 */
public class InfoCommand extends AbstractCommand {
    private final ViewReceiver viewReceiver;
    private final BufferedDataWriter infoWriter;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Instantiates a new Info command.
     *
     * @param name         the name
     * @param description  the description
     * @param viewReceiver the view receiver
     * @param infoWriter   the info writer
     */
    public InfoCommand(String name, String description, ViewReceiver viewReceiver, BufferedDataWriter infoWriter) {
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

        DatabaseMetaData metaData = viewReceiver.getMetaData();
        infoWriter.writeInfo(formatDatabaseMetaData(metaData));
    }

    private String formatDatabaseMetaData(DatabaseMetaData metaData) {
        StringBuilder sb = new StringBuilder();
        sb.append("Тип коллекции: ").append(metaData.getClazz().getSimpleName()).append(System.lineSeparator());
        sb.append("Дата создания: ").append(metaData.getLocalDateTime().format(formatter)).append(System.lineSeparator());
        sb.append("Количество элементов: ").append(metaData.getSize());
        return sb.toString();
    }
}
