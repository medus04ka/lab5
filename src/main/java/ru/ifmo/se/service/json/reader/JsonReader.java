package ru.ifmo.se.service.json.reader;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ru.ifmo.se.service.json.exceptions.JsonReadingException;
import ru.ifmo.se.service.json.serialization.ClassAdapter;
import ru.ifmo.se.service.json.serialization.LocalDateAdapter;
import ru.ifmo.se.service.json.serialization.LocalDateTimeAdapter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class JsonReader<T> {
    private final Gson gson;
    private final Type dataType;

    public JsonReader(Type dataType) {
        this.gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .registerTypeAdapter(Class.class, new ClassAdapter())
                .setPrettyPrinting()
                .create();
        this.dataType = dataType;
    }

    public T readFromFile(File file) throws JsonReadingException {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            return gson.fromJson(reader, dataType);
        } catch (IOException e) {
            throw new JsonReadingException("Error reading from file", e);
        }
    }
}
