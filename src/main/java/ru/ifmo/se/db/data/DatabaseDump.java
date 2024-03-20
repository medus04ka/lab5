package ru.ifmo.se.db.data;

import lombok.Data;

import java.util.PriorityQueue;

@Data
public class DatabaseDump<T> {
    private final DatabaseMetaData databaseMetaData;
    private final PriorityQueue<T>  queue;
}