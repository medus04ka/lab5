package ru.ifmo.se.db.dao;

import ru.ifmo.se.db.data.DatabaseDump;
import ru.ifmo.se.db.data.DatabaseMetaData;

import java.util.Collection;
import java.util.Optional;

public interface CrudDao<T> {
    void create(T element);

    boolean update(T element);

    Optional<T> findById(long id);

    boolean removeById(long id);

    void clear();

    long count();

    Collection<T> getAll();

    DatabaseDump<T> getDump();

    DatabaseMetaData getMetaData();
}
