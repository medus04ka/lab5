package ru.ifmo.se.db.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class IdManager {
    private final List<Long> idList;
    private final List<Long> availableIds;

    public IdManager(List<Long> ids) {
        idList = new ArrayList<>(ids);
        Collections.sort(idList);
        availableIds = new ArrayList<>();
        updateAvailableIds();
    }

    public void removeId(long id) {
        if (idList.contains(id)) {
            idList.remove((Long) id);
            availableIds.add(id);
            Collections.sort(availableIds);
        }
    }

    public long getAvailableId() {
        if (!availableIds.isEmpty()) {
            long id = availableIds.remove(0);
            idList.add(id);
            Collections.sort(idList);
            return id;
        } else {
            long newId = idList.isEmpty() ? 0 : idList.get(idList.size() - 1) + 1;
            idList.add(newId);
            return newId;
        }
    }

    public void clear() {
        idList.clear();
        availableIds.clear();
    }

    private void updateAvailableIds() {
        availableIds.clear();
        long maxId = idList.isEmpty() ? 0 : idList.get(idList.size() - 1);
        for (long i = 0; i < maxId; i++) {
            if (!idList.contains(i)) {
                availableIds.add(i);
            }
        }
    }
}