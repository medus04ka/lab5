package ru.ifmo.se;

import java.io.File;

/**
 * Класс main с которого все начинается
 */
public class Main {
    public static void main(String[] args) {
        String filePath = System.getenv("FILE_PATH");
        if (filePath != null) {
            System.out.println("База данных будет загружена из файла по пути " + filePath);
            Runner runner = new Runner(new File(filePath));
            runner.start();
        } else {
            System.out.println("Переменная окружения FILE_PATH не установлена.");
        }
    }
}
