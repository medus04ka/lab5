package ru.ifmo.se.service.receiver;

import lombok.RequiredArgsConstructor;
import ru.ifmo.se.db.dao.CrudDao;
import ru.ifmo.se.db.data.DatabaseMetaData;
import ru.ifmo.se.model.HumanBeing;
import ru.ifmo.se.model.WeaponType;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class ViewReceiver {
    private final CrudDao<HumanBeing> crudDao;

    public Collection<HumanBeing> findAll() {
        return crudDao.getAll();
    }

    public double getSumOfImpactSpeed() {
        return crudDao.getAll().stream()
                .mapToDouble(HumanBeing::getImpactSpeed)
                .sum();
    }

    public List<HumanBeing> filterLessThanWeaponType(WeaponType weaponType) {
        return crudDao.getAll().stream()
                .filter(human -> human.getWeaponType() != null && human.getWeaponType().ordinal() < weaponType.ordinal())
                .collect(Collectors.toList());
    }

    public List<HumanBeing> getAscendingOrder() {
        return crudDao.getAll().stream()
                .sorted()
                .collect(Collectors.toList());
    }

    public DatabaseMetaData getMetaData() {
        return crudDao.getMetaData();
    }
}
