package ru.ifmo.se.controller.command.view;

import ru.ifmo.se.controller.command.AbstractCommand;
import ru.ifmo.se.model.HumanBeing;
import ru.ifmo.se.model.WeaponType;
import ru.ifmo.se.service.io.utils.HumanBeingOutputHelper;
import ru.ifmo.se.service.io.writer.BufferedDataWriter;
import ru.ifmo.se.service.receiver.ViewReceiver;

import java.util.List;

/**
 * The type Filter less than weapon type.
 */
public class FilterLessThanWeaponType extends AbstractCommand {
    private final ViewReceiver viewReceiver;
    private final BufferedDataWriter infoWriter;
    private final HumanBeingOutputHelper humanBeingOutputHelper;

    /**
     * Instantiates a new Filter less than weapon type.
     *
     * @param name         the name
     * @param description  the description
     * @param viewReceiver the view receiver
     * @param infoWriter   the info writer
     */
    public FilterLessThanWeaponType(String name, String description, ViewReceiver viewReceiver, BufferedDataWriter infoWriter) {
        super(name, description, 1);
        this.viewReceiver = viewReceiver;
        this.infoWriter = infoWriter;
        this.humanBeingOutputHelper = new HumanBeingOutputHelper(infoWriter);
    }

    @Override
    public void execute(String[] args) {
        if (!validateCountOfArguments(args)) {
            infoWriter.writeErrorMessage("Неправильное количество аргументов. Команда " + getName() + " требует передачи типа оружия. Допустимые значения: AXE, SHOTGUN, RIFLE, KNIFE, MACHINE_GUN ");
            return;
        }

        try {
            String weaponTypeStr = args[0];
            WeaponType weaponType = WeaponType.valueOf(weaponTypeStr);
            List<HumanBeing> filteredList = viewReceiver.filterLessThanWeaponType(weaponType);
            if (filteredList.isEmpty()) {
                infoWriter.writeInfo("Нет элементов с типом оружия меньше " + weaponType);
            } else {
                humanBeingOutputHelper.showCollectionHumanBeing(filteredList);
            }
        } catch (IllegalArgumentException e) {
            infoWriter.writeErrorMessage("Ошибка при выполнении команды: неправильное значение типа оружия. \nДопустимые значения: AXE, SHOTGUN, RIFLE, KNIFE, MACHINE_GUN");
        }
    }
}
