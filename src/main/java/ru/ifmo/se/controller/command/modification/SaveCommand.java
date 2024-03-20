package ru.ifmo.se.controller.command.modification;

import com.google.gson.reflect.TypeToken;
import ru.ifmo.se.controller.command.AbstractCommand;
import ru.ifmo.se.db.data.DatabaseDump;
import ru.ifmo.se.model.HumanBeing;
import ru.ifmo.se.service.io.writer.BufferedDataWriter;
import ru.ifmo.se.service.json.exceptions.JsonWritingException;
import ru.ifmo.se.service.json.writer.JsonWriter;
import ru.ifmo.se.service.receiver.ModificationReceiver;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;

public class SaveCommand extends AbstractCommand {
    private final ModificationReceiver modificationReceiver;
    private final BufferedDataWriter infoWriter;
    
    private File dbFile;

    public SaveCommand(String name, String description, ModificationReceiver modificationReceiver, File dbFile, BufferedDataWriter infoWriter) {
        super(name, description, 0);
        this.modificationReceiver = modificationReceiver;
        this.dbFile = dbFile;
        this.infoWriter = infoWriter;
        
    }

    @Override
    public void execute(String[] args) {
        if (!validateCountOfArguments(args)) {
            infoWriter.writeErrorMessage("Неправильное количество аргументов. Команда " + getName() + " не требует дополнительных аргументов. ");
            return;
        }

        if (!dbFile.canWrite()) {
            infoWriter.writeErrorMessage("Отсутствуют права на запись в файл: %s ".formatted(dbFile));
            try {
                dbFile = new File("backupDatabase.json");
                dbFile.createNewFile();
                if (!dbFile.exists()) {
                    return;
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        try {
            DatabaseDump<HumanBeing> dump = modificationReceiver.getDump();
            Type dataType = new TypeToken<DatabaseDump<HumanBeing>>() {
            }.getType();
            JsonWriter<DatabaseDump<HumanBeing>> jsonWriter = new JsonWriter<>(dataType);
            jsonWriter.writeToFile(dump, dbFile);
            if (dbFile.getName().equals("backupDatabase.json")){
                infoWriter.writeInfo("Мы позаботились о ваших данных и сохранили их в другом файле, чтобы не потерялись !");
            }
            infoWriter.writeInfo("Данные успешно сохранены в файл: " + dbFile);
        } catch (JsonWritingException e) {
            infoWriter.writeErrorMessage("Ошибка при записи данных в файл: " + e.getMessage());
        }
    }
}
