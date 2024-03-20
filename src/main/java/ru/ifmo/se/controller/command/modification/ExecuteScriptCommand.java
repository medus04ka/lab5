package ru.ifmo.se.controller.command.modification;

import ru.ifmo.se.Runner;
import ru.ifmo.se.controller.Invoker;
import ru.ifmo.se.controller.command.AbstractCommand;
import ru.ifmo.se.controller.command.CommandManager;
import ru.ifmo.se.controller.exceptions.RecursionException;
import ru.ifmo.se.service.io.exceptions.RuntimeIOException;
import ru.ifmo.se.service.io.reader.BufferedDataReader;
import ru.ifmo.se.service.io.reader.Mode;
import ru.ifmo.se.service.io.writer.BufferedDataWriter;
import ru.ifmo.se.service.receiver.ModificationReceiver;
import ru.ifmo.se.service.receiver.ViewReceiver;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.Duration;
import java.util.HashSet;
import java.util.Set;

public class ExecuteScriptCommand extends AbstractCommand {
    private final static Set<String> executedScripts = new HashSet<>();
    private final File dbFilePath;
    private final ModificationReceiver modificationReceiver;
    private final ViewReceiver viewReceiver;
    private final BufferedDataWriter infoWriter;


    public ExecuteScriptCommand(String name, String description, File dbFilePath, ModificationReceiver modificationReceiver, ViewReceiver viewReceiver, BufferedDataWriter infoWriter) {
        super(name, description, 1);
        this.dbFilePath = dbFilePath;
        this.modificationReceiver = modificationReceiver;
        this.viewReceiver = viewReceiver;
        this.infoWriter = infoWriter;
    }

    @Override
    public void execute(String[] args) {
        if (!validateCountOfArguments(args)) {
            infoWriter.writeErrorMessage("Неправильное количество аргументов. Нужно передать только путь к файлу. ");
            return;
        }

        String filePath = args[0];
        File script = new File(filePath);
        if (!script.exists()) {
            infoWriter.writeErrorMessage("Файл не существует: " + filePath);
            return;
        }
        if (!script.canRead()) {
            infoWriter.writeErrorMessage("Отсутствуют права на чтение из файла: " + filePath);
            return;
        }

        if (!executedScripts.contains(filePath)) {
            executedScripts.add(filePath);
            infoWriter.writeInfo("Начинаем выполнение скрипта из файла по пути %s".formatted(filePath));
            long start = System.currentTimeMillis();
            try {
                Runner runner = getRunner(script);
                runner.runProcess();
                long end = System.currentTimeMillis();
                String duration = formatDuration(end - start);
                infoWriter.writeMessage("Выполнение скрипта по пути %s завершено за %s".formatted(filePath, duration));
                executedScripts.remove(filePath);
            } catch (IOException e) {
                throw new RuntimeIOException(e.getMessage(), e);
            }
        } else {
            throw new RecursionException("Хотите поломать комп с помощью рекурсии?");
        }
    }

    private Runner getRunner(File file) throws IOException {
        BufferedDataReader dataReader = new BufferedDataReader(new BufferedReader(new FileReader(file)));
        BufferedDataWriter dataWriter = new BufferedDataWriter(Mode.FILE);
        CommandManager commandManager = new CommandManager(modificationReceiver, viewReceiver, dataReader, dataWriter, dbFilePath, Mode.FILE);
        Invoker invoker = new Invoker(commandManager);
        return new Runner(invoker, dataReader, dataWriter, dbFilePath, Mode.FILE);
    }

    private String formatDuration(long durationMillis) {
        Duration duration = Duration.ofMillis(durationMillis);
        long hours = duration.toHours();
        long minutes = duration.toMinutesPart();
        long seconds = duration.toSecondsPart();
        long millis = duration.toMillisPart();
        return String.format("%d часов, %d минут, %d секунд, %d миллисекунд", hours, minutes, seconds, millis);
    }

}
