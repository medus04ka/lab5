package ru.ifmo.se.service.receiver;

import lombok.RequiredArgsConstructor;
import ru.ifmo.se.db.dao.CrudDao;
import ru.ifmo.se.db.data.DatabaseDump;
import ru.ifmo.se.model.HumanBeing;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class ModificationReceiver {
    private final CrudDao<HumanBeing> crudDao;

    public void add(HumanBeing humanBeing) {
        crudDao.create(humanBeing);
    }

    public boolean update(long id, HumanBeing updatedHumanBeing) {
        Optional<HumanBeing> maybeHumanBeing = crudDao.findById(id);
        if (maybeHumanBeing.isPresent()) {
            updatedHumanBeing.setId(id);
            updatedHumanBeing.setCreationDate(maybeHumanBeing.get().getCreationDate());
        } else {
            return false;
        }
        return crudDao.update(updatedHumanBeing);
    }

    public Optional<HumanBeing> findById(long id) {
        return crudDao.findById(id);
    }

    public boolean removeById(long id) {
        return crudDao.removeById(id);
    }

    public boolean removeFirst() {
        return crudDao.removeById(crudDao.getAll().iterator().next().getId());
    }

    public long removeGreater(HumanBeing humanBeing) {
        List<HumanBeing> toBeRemovedList = crudDao.getAll().stream()
                .filter(toBeRemoved -> toBeRemoved.compareTo(humanBeing) > 0)
                .toList();

        toBeRemovedList.forEach(toBeRemoved -> crudDao.removeById(toBeRemoved.getId()));
        return toBeRemovedList.size();
    }

    public void clear() {
        crudDao.clear();
    }

    public DatabaseDump<HumanBeing> getDump() {
        return crudDao.getDump();
    }
}
