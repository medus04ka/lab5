package ru.ifmo.se.service.io.utils;

import lombok.RequiredArgsConstructor;
import ru.ifmo.se.model.*;
import ru.ifmo.se.service.io.exceptions.ScriptDataReadException;
import ru.ifmo.se.service.io.reader.DataReader;
import ru.ifmo.se.service.io.reader.Mode;
import ru.ifmo.se.service.io.writer.BufferedDataWriter;

@RequiredArgsConstructor
public class HumanBeingReaderImpl implements HumanBeingReader {
    private final DataReader reader;
    private final BufferedDataWriter writer;
    private final Mode mode;


    @Override
    public HumanBeing readHumanBeing() {
        String name = readName();
        Coordinates coordinates = readCoordinates();
        Boolean realHero = readRealHero();
        boolean hasToothpick = readHasToothpick();
        double impactSpeed = readImpactSpeed();
        WeaponType weaponType = readWeaponType();
        Mood mood = readMood();
        Car car = readCar();

        return new HumanBeing(name, coordinates, realHero, hasToothpick, impactSpeed, weaponType, mood, car);
    }


    private String readName() {
        writer.writeMessage("Введите никнаме (не пустая строка):");
        String name = reader.readLine();
        while (name == null || name.trim().isEmpty()) {
            writer.writeErrorMessage("Значение имени не может быть пустым. Пожалуйста, введите никнаме снова:");
            handleScriptDataReadError();
            name = reader.readLine();
        }
        return name.trim();
    }

    private Coordinates readCoordinates() {
        writer.writeMessage("Введите координату X (число, минимум -534):");
        double x = readDouble();
        while (x < -534) {
            writer.writeErrorMessage("Координата X должна быть больше -534. Пожалуйста, введите координату X снова:");
            handleScriptDataReadError();
            x = readDouble();
        }

        writer.writeMessage("Введите координату Y (любое вещественное число):");
        double y = readDouble();
        return new Coordinates(x, y);
    }

    private Boolean readRealHero() {
        return readBoolean("Введите является ли персонаж убийцей? (да/нет):");
    }

    private boolean readHasToothpick() {
        return readBoolean("Имеет ли у персонажа крутая шляпка? (да/нет):");
    }

    private double readImpactSpeed() {
        writer.writeMessage("Введите скорость удара (вещественное число, максимум 650):");
        double impactSpeed = readDouble();
        while (impactSpeed > 650) {
            writer.writeErrorMessage("Максимальное значение скорости удара может быть 650. Пожалуйста, введите еще раз число поменьше:");
            handleScriptDataReadError();
            impactSpeed = readDouble();
        }
        return impactSpeed;
    }

    private WeaponType readWeaponType() {
        writer.writeMessage("Выберите тип оружия (можно ввести: AXE, SHOTGUN, RIFLE, KNIFE, MACHINE_GUN):");
        WeaponType weaponType = null;
        while (weaponType == null) {
            try {
                String inputLine = reader.readLine();
                if (inputLine == null || inputLine.isBlank()) return null;
                weaponType = WeaponType.valueOf(inputLine.trim().toUpperCase());
            } catch (IllegalArgumentException e) {
                writer.writeErrorMessage("Ошибка: введите один из предложенных типов оружия. (можно ввести: AXE, SHOTGUN, RIFLE, KNIFE, MACHINE_GUN)");
                handleScriptDataReadError();
            }
        }
        return weaponType;
    }

    private Mood readMood() {
        writer.writeMessage("Выберите настроение (можно ввести: SADNESS, LONGING, CALM, RAGE):");
        Mood mood = null;
        while (mood == null) {
            try {
                String inputLine = reader.readLine();
                if (inputLine == null || inputLine.isBlank()) {
                    writer.writeErrorMessage("Значение настроения не может быть пустой. (можно ввести: SADNESS, LONGING, CALM, RAGE): ");
                    handleScriptDataReadError();
                } else {
                    mood = Mood.valueOf(inputLine.trim().toUpperCase());

                }
            } catch (IllegalArgumentException e) {
                writer.writeErrorMessage("Ошибка: введите одно из предложенных настроений. (можно ввести: SADNESS, LONGING, CALM, RAGE):");
                handleScriptDataReadError();
            }
        }
        return mood;
    }

    private Car readCar() {
        writer.writeMessage("Введите название автомобиля (может быть пустым):");
        String name = reader.readLine();
        if (name == null || name.isBlank()) return null;
        return new Car(name.trim());
    }

    private double readDouble() {
        double value = 0;
        boolean validInput = false;
        while (!validInput) {
            String input = reader.readLine();
            if (input != null && !input.isBlank()) {
                try {
                    input = input.replace(",", ".");
                    value = Double.parseDouble(input.trim());
                    validInput = true;
                } catch (NumberFormatException e) {
                    writer.writeErrorMessage("Ошибка: введите вещественное число.");
                    handleScriptDataReadError();
                }
            } else {
                writer.writeErrorMessage("Ошибка: значение не может быть пустым. Введите вещественное число.");
                handleScriptDataReadError();
            }
        }
        return value;
    }

    private Boolean readBoolean(String message) {
        writer.writeMessage(message);
        Boolean result = null;
        while (result == null) {
            String input = reader.readLine();
            if (input != null && !input.isBlank()) {
                input = input.trim().toLowerCase();
                if (input.equals("да") || input.equals("нет")) {
                    result = stringToBoolean(input);
                } else {
                    writer.writeErrorMessage("Ошибка: введите да или нет.");
                    handleScriptDataReadError();
                }
            } else {
                writer.writeErrorMessage("Ошибка: значение не может быть пустым. Введите да или нет.");
                handleScriptDataReadError();
            }
        }
        return result;
    }

    private boolean stringToBoolean(String value) {
        return value.trim().equalsIgnoreCase("да");
    }

    private void handleScriptDataReadError() {
        if (mode.equals(Mode.FILE)) {
            throw new ScriptDataReadException("Возникла ошибка при выполнении скрипта на строчке: %d. Исправьте невалидные данные".formatted(reader.getLastReadLineNumber()));
        }
    }
}
