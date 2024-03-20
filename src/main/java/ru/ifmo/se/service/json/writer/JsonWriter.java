package ru.ifmo.se.service.json.writer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ru.ifmo.se.service.json.exceptions.JsonWritingException;
import ru.ifmo.se.service.json.serialization.ClassAdapter;
import ru.ifmo.se.service.json.serialization.LocalDateAdapter;
import ru.ifmo.se.service.json.serialization.LocalDateTimeAdapter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class JsonWriter<T> {
    private final Gson gson;
    private final Type dataType;

    public JsonWriter(Type dataType) {
        this.gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .registerTypeAdapter(Class.class, new ClassAdapter())
                .setPrettyPrinting()
                .serializeNulls()
                .create();
        this.dataType = dataType;
    }

    public void writeToFile(T data, File file) throws JsonWritingException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            gson.toJson(data, dataType, writer);
        } catch (IOException e) {
            throw new JsonWritingException("Error writing to file", e);
        }
    }
}