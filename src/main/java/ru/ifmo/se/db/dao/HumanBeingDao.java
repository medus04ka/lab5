package ru.ifmo.se.db.dao;

import ru.ifmo.se.db.data.DatabaseDump;
import ru.ifmo.se.db.data.DatabaseMetaData;
import ru.ifmo.se.db.utils.IdManager;
import ru.ifmo.se.model.HumanBeing;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;
import java.util.PriorityQueue;
import java.util.stream.Collectors;

public class HumanBeingDao implements CrudDao<HumanBeing> {
    private final PriorityQueue<HumanBeing> queue;
    private final IdManager idManager;
    private final DatabaseMetaData databaseMetaData;
    private final DatabaseDump<HumanBeing> databaseDump;

    public HumanBeingDao(DatabaseDump<HumanBeing> databaseDump) {
        this.queue = databaseDump.getQueue();
        this.databaseDump = databaseDump;
        this.databaseMetaData = databaseDump.getDatabaseMetaData();
        this.idManager = createIdManager(queue);
    }

    public HumanBeingDao() {
        this.queue = new PriorityQueue<>();
        this.idManager = createIdManager(queue);
        this.databaseMetaData = DatabaseMetaData.builder()
                .clazz(PriorityQueue.class)
                .localDateTime(LocalDateTime.now())
                .size(0L)
                .build();
        this.databaseDump = new DatabaseDump<HumanBeing>(this.databaseMetaData, this.queue);
    }

    @Override
    public void create(HumanBeing element) {
        element.setId(idManager.getAvailableId());
        element.setCreationDate(LocalDate.now());
        queue.offer(element);
    }

    @Override
    public boolean update(HumanBeing element) {
        Optional<HumanBeing> optionalHumanBeing = findById(element.getId());
        if (optionalHumanBeing.isPresent()) {
            // Удаляем старый элемент и добавляем новый. Так проще обновлять, на производительность не влияет особо
            queue.removeIf(humanBeing -> humanBeing.getId() == element.getId());
            queue.offer(element);
            return true;
        }
        return false; // Элемент с указанным id не найден
    }

    @Override
    public Optional<HumanBeing> findById(long id) {
        return queue.stream()
                .filter(humanBeing -> humanBeing.getId() == id)
                .findFirst();
    }

    @Override
    public boolean removeById(long id) {

        return queue.removeIf(humanBeing -> {
            if (humanBeing.getId() == id) {
                idManager.removeId(id);
                return true;
            }
            return false;
        });
    }

    @Override
    public void clear() {
        queue.clear();
        idManager.clear();
    }

    @Override
    public long count() {
        return queue.size();
    }

    @Override
    public Collection<HumanBeing> getAll() {
        return queue;
    }

    @Override
    public DatabaseDump<HumanBeing> getDump() {
        databaseDump.getDatabaseMetaData().setSize(count());
        return databaseDump;
    }

    @Override
    public DatabaseMetaData getMetaData() {
        databaseMetaData.setSize(count());
        return databaseMetaData;
    }

    private IdManager createIdManager(PriorityQueue<HumanBeing> queue) {
        return new IdManager(queue.stream().map(HumanBeing::getId).collect(Collectors.toList()));
    }

}
