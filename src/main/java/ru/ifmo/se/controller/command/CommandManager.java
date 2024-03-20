package ru.ifmo.se.controller.command;

import ru.ifmo.se.controller.command.modification.*;
import ru.ifmo.se.controller.command.view.*;
import ru.ifmo.se.service.io.reader.DataReader;
import ru.ifmo.se.service.io.reader.Mode;
import ru.ifmo.se.service.io.utils.HumanBeingReader;
import ru.ifmo.se.service.io.utils.HumanBeingReaderImpl;
import ru.ifmo.se.service.io.writer.BufferedDataWriter;
import ru.ifmo.se.service.receiver.ModificationReceiver;
import ru.ifmo.se.service.receiver.ViewReceiver;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CommandManager {
    private final Map<String, AbstractCommand> allCommands = new HashMap<>();
    private final ModificationReceiver modificationReceiver;
    private final ViewReceiver viewReceiver;
    private final HumanBeingReader humanBeingReader;
    private final BufferedDataWriter infoWriter;
    private final File dbFilePath;

    public CommandManager(ModificationReceiver modificationReceiver, ViewReceiver viewReceiver, DataReader dataReader, BufferedDataWriter infoWriter, File dbFilePath, Mode mode) {
        this.modificationReceiver = modificationReceiver;
        this.viewReceiver = viewReceiver;
        this.infoWriter = infoWriter;
        this.dbFilePath = dbFilePath;
        this.humanBeingReader = new HumanBeingReaderImpl(dataReader, infoWriter, mode);
        initializeAllCommand();
    }

    public AbstractCommand getCommand(String nameCommand) {
        return allCommands.get(nameCommand);
    }

    private void initializeAllCommand() {
        AddCommand add = new AddCommand("add", "добавить новый элемент в коллекцию", modificationReceiver, humanBeingReader, infoWriter);
        ClearCommand clear = new ClearCommand("clear", "очистить коллекцию", modificationReceiver, infoWriter);
        ExecuteScriptCommand executeScript = new ExecuteScriptCommand("execute_script", "считать и исполнить скрипт из указанного файла. В скрипте содержатся команды в таком же виде, в котором их вводит", dbFilePath, modificationReceiver, viewReceiver, infoWriter);
        ExitCommand exit = new ExitCommand("exit", "завершить программу (без сохранения в файл)", infoWriter);
        RemoveByIdCommand removeById = new RemoveByIdCommand("remove_by_id", "удалить элемент из коллекции по его id", modificationReceiver, infoWriter);
        RemoveFirstCommand removeFirst = new RemoveFirstCommand("remove_first", "удалить первый элемент из коллекции", modificationReceiver, infoWriter);
        RemoveGreater removeGreater = new RemoveGreater("remove_greater", "удалить из коллекции все элементы, превышающие заданный", modificationReceiver, humanBeingReader, infoWriter);
        SaveCommand save = new SaveCommand("save", "сохранить коллекцию в файл", modificationReceiver, dbFilePath, infoWriter);
        UpdateCommand update = new UpdateCommand("update", "обновить значение элемента коллекции, id которого равен заданному", modificationReceiver, humanBeingReader, infoWriter);

        FilterLessThanWeaponType filterLessThanWeaponTypeWeaponType = new FilterLessThanWeaponType("filter_less_than_weapon_type", "вывести элементы, значение поля weaponType которых меньше заданного", viewReceiver, infoWriter);
        HelpCommand helpCommand = new HelpCommand("help", "вывести справку по доступным командам", viewReceiver, infoWriter);
        HistoryCommand history = new HistoryCommand("history", "вывести последние 14 команд (без их аргументов)", infoWriter);
        InfoCommand info = new InfoCommand("info", "вывести в стандартный поток вывода информацию о коллекции (тип, дата инициализации, количество элементов и т.д.)", viewReceiver, infoWriter);
        PrintAscending printAscending = new PrintAscending("print_ascending", "вывести элементы коллекции в порядке возрастания", viewReceiver, infoWriter);
        ShowCommand show = new ShowCommand("show", "вывести в стандартный поток вывода все элементы коллекции в строковом представлении", viewReceiver, infoWriter);
        SumOfImpactSpeed sumOfImpactSpeed = new SumOfImpactSpeed("sum_of_impact_speed", "вывести сумму значений поля impactSpeed для всех элементов коллекции", viewReceiver, infoWriter);

        List<AbstractCommand> commands = Stream.of(
                add,
                clear,
                executeScript,
                exit,
                removeById,
                removeFirst,
                removeGreater,
                save,
                update,
                filterLessThanWeaponTypeWeaponType,
                helpCommand,
                history,
                info,
                printAscending,
                show,
                sumOfImpactSpeed
        ).toList();
        commands.forEach(command -> allCommands.put(command.getName(), command));
        helpCommand.setHelpManual(prepareHelpManual());
    }

    private String prepareHelpManual() {
        return allCommands.entrySet().stream()
                .map(entry -> entry.getKey() + " : " + entry.getValue().getDescription())
                .collect(Collectors.joining("\n"));
    }
}
