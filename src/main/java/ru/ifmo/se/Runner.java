package ru.ifmo.se;

import com.google.gson.reflect.TypeToken;
import jakarta.validation.ValidationException;
import ru.ifmo.se.controller.Invoker;
import ru.ifmo.se.controller.command.CommandManager;
import ru.ifmo.se.controller.exceptions.CommandNotFound;
import ru.ifmo.se.controller.exceptions.RecursionException;
import ru.ifmo.se.db.dao.HumanBeingDao;
import ru.ifmo.se.db.data.DatabaseDump;
import ru.ifmo.se.db.data.DatabaseMetaData;
import ru.ifmo.se.db.exceptions.DatabaseLoadingException;
import ru.ifmo.se.db.exceptions.UniqueIdConstraintViolationException;
import ru.ifmo.se.model.HumanBeing;
import ru.ifmo.se.service.ValidationService;
import ru.ifmo.se.service.io.exceptions.FileReadPermissionException;
import ru.ifmo.se.service.io.exceptions.RuntimeIOException;
import ru.ifmo.se.service.io.exceptions.ScriptDataReadException;
import ru.ifmo.se.service.io.reader.BufferedDataReader;
import ru.ifmo.se.service.io.reader.ConsoleDataReader;
import ru.ifmo.se.service.io.reader.DataReader;
import ru.ifmo.se.service.io.reader.Mode;
import ru.ifmo.se.service.io.writer.BufferedDataWriter;
import ru.ifmo.se.service.json.exceptions.JsonReadingException;
import ru.ifmo.se.service.json.reader.JsonReader;
import ru.ifmo.se.service.receiver.ModificationReceiver;
import ru.ifmo.se.service.receiver.ViewReceiver;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.PriorityQueue;

/**
 * Класс Runner - считывает и с консоли и с идеешки, крутой класс
 */
public class Runner {
    private final DataReader dataReader;
    private final BufferedDataWriter infoWriter;
    private final File dbFile;
    private final Mode mode;
    private Invoker invoker;
    private JsonReader<DatabaseDump<HumanBeing>> jsonReader;
    private ValidationService validationService;

    public Runner(Invoker invoker, BufferedDataReader dataReader, BufferedDataWriter infoWriter, File dbFile, Mode mode) {
        this.invoker = invoker;
        this.dataReader = dataReader;
        this.infoWriter = infoWriter;
        this.dbFile = dbFile;
        this.mode = mode;
    }

    public Runner(File dbFile) {
        this.dbFile = dbFile;
        this.mode = Mode.CONSOLE;
        this.jsonReader = new JsonReader<>(new TypeToken<DatabaseDump<HumanBeing>>() {
        }.getType());
        this.validationService = new ValidationService();
        this.dataReader = new BufferedDataReader(new BufferedReader(new InputStreamReader(System.in)));
  //      this.dataReader = new ConsoleDataReader();
        this.infoWriter = new BufferedDataWriter(Mode.CONSOLE);
    }

    private DatabaseDump<HumanBeing> loadDatabaseFromFile(File dbFile) throws JsonReadingException {
        if (!dbFile.exists()) {
            try {
                boolean createdNewFile = dbFile.createNewFile();
                if (createdNewFile) {
                    infoWriter.writeInfo("Файл базы данных по пути %s не существует. Поэтому данный файл был создан сейчас. \nВы можете продолжить работу!"
                            .formatted(dbFile.getAbsoluteFile()));
                } else {
                    throw new DatabaseLoadingException("Файл базы данных по пути %s не существует. Пожалуйста задайте правильный путь".formatted(dbFile.getAbsoluteFile()));
                }
                return createDatabaseDump();
            } catch (IOException e) {
                throw new DatabaseLoadingException("Ошибка при создании нового файла по пути %s".formatted(dbFile.getAbsoluteFile()), e);
            }
        } else {
            if (!dbFile.canRead()) {
                throw new FileReadPermissionException("Не удается прочитать файл с данными. Дайте права на чтение");
            }
            DatabaseDump<HumanBeing> dump = jsonReader.readFromFile(dbFile);
            if (dump == null) {
                dump = createDatabaseDump();
            }
            if (validationService.hasDuplicatesId(dump.getQueue())) {
                throw new UniqueIdConstraintViolationException("В данных содержатся повторяющиеся id. Сделайте данные валидными");
            }
            validationService.validateConstraints(dump.getQueue());
            return dump;
        }
    }

    private DatabaseDump<HumanBeing> createDatabaseDump() {
        DatabaseMetaData metaData = DatabaseMetaData.builder()
                .clazz(PriorityQueue.class)
                .localDateTime(LocalDateTime.now())
                .size(0L)
                .build();
        return new DatabaseDump<>(metaData, new PriorityQueue<>());
    }

    private void initAll() throws JsonReadingException {
        DatabaseDump<HumanBeing> dump;
        dump = loadDatabaseFromFile(dbFile);
        HumanBeingDao humanBeingDao = new HumanBeingDao(dump);
        ModificationReceiver modificationReceiver = new ModificationReceiver(humanBeingDao);
        ViewReceiver viewReceiver = new ViewReceiver(humanBeingDao);
        CommandManager commandManager = new CommandManager(modificationReceiver, viewReceiver, dataReader, infoWriter, dbFile, mode);
        invoker = new Invoker(commandManager);
    }

    public void start() {
        try {
            initAll();
            infoWriter.writeMessage("Воспользуйтесь help для вывода доступных команд");
            runProcess();
        } catch (UniqueIdConstraintViolationException | JsonReadingException | DatabaseLoadingException |
                 FileReadPermissionException e) {
            infoWriter.writeErrorMessage(e.getMessage());
        } catch (ValidationException e) {
            infoWriter.writeErrorMessage("Во входном файле содержатся нарушений ограничений данных. Сделайте данные валидными");
            infoWriter.writeErrorMessage(e.getMessage());
        }
    }

    public void runProcess() {
        while (true) {
            if (mode.equals(Mode.CONSOLE)) {
                infoWriter.writeLineWithoutSpace("> ");
            }
            try {
                String input = dataReader.readLine();
                if (input == null) {
                    break;
                }
                if (mode.equals(Mode.FILE)) {
                    if (Objects.equals(input, "")) {
                        continue;
                    }
                    infoWriter.writeDebugMessage(String.format("Выполняем команду из скрипта (строка %d): %s", dataReader.getLastReadLineNumber(), input));
                }
                invoker.execute(input);
            } catch (RecursionException e) {
                infoWriter.writeErrorMessage(e.getMessage());
                break;
            } catch (ScriptDataReadException e) {
                infoWriter.writeDebugMessage(e.getMessage());
                break;
            } catch (CommandNotFound | RuntimeIOException e) {
                infoWriter.writeDebugMessage(e.getMessage());
            }
        }
    }
}
