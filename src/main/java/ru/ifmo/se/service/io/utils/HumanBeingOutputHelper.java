package ru.ifmo.se.service.io.utils;

import ru.ifmo.se.model.HumanBeing;
import ru.ifmo.se.service.io.writer.BufferedDataWriter;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * The type Human being output helper.
 */
public class HumanBeingOutputHelper {
    private final BufferedDataWriter infoWriter;
    public HumanBeingOutputHelper(BufferedDataWriter infoWriter) {
        this.infoWriter = infoWriter;
    }

    public void showHumanBeing(HumanBeing humanBeing) {
        if (humanBeing != null) {
            infoWriter.writeInfo(formatHumanBeing(humanBeing));
        } else {
            infoWriter.writeErrorMessage("Получен нулевой объект HumanBeing.");
        }
    }

    public void showCollectionHumanBeing(Collection<HumanBeing> humanBeings) {
        if (humanBeings.isEmpty()) {
            infoWriter.writeInfo("Коллекция пуста.");
        } else {
            String result = humanBeings.stream()
                    .map(this::formatHumanBeing)
                    .collect(Collectors.joining(System.lineSeparator()));
            infoWriter.writeInfo(result);
        }
    }

    private String formatHumanBeing(HumanBeing human) {
        String separatorLine = "=".repeat(50);
        StringBuilder formatted = new StringBuilder();
        formatted.append(separatorLine).append(System.lineSeparator())
                .append("Идентификатор: ").append(human.getId()).append(System.lineSeparator())
                .append("Имя: ").append(human.getName()).append(System.lineSeparator())
                .append("Координаты: ").append("(x = ").append(human.getCoordinates().getX()).append(", y = ").append(human.getCoordinates().getY()).append(")").append(System.lineSeparator())
                .append("Дата создания: ").append(human.getCreationDate()).append(System.lineSeparator())
                .append("Настоящий герой: ").append(booleanToString(human.getRealHero())).append(System.lineSeparator())
                .append("Имеет зуб: ").append(booleanToString(human.isHasToothpick())).append(System.lineSeparator())
                .append("Скорость удара: ").append(human.getImpactSpeed()).append(System.lineSeparator())
                .append("Оружие: ").append(human.getWeaponType() == null ? "отсутствует" : human.getWeaponType()).append(System.lineSeparator())
                .append("Настроение: ").append(human.getMood()).append(System.lineSeparator())
                .append("Автомобиль: ").append(human.getCar() == null ? "отсутствует" : human.getCar().getName()).append(System.lineSeparator())
                .append(separatorLine);
        return formatted.toString();


    }

    public String booleanToString(boolean value) {
        return value ? "Да" : "Нет";
    }
}
