package com.es.phoneshop.dao;

import com.es.phoneshop.locker.ReadWriteActionLocker;
import com.es.phoneshop.model.GenericUniqueItem;

import java.util.List;

public abstract class GenericArrayListDao<T extends GenericUniqueItem, E extends RuntimeException> {

    protected long maxItemId;
    protected List<T> items;
    protected final ReadWriteActionLocker readWriteActionLocker = new ReadWriteActionLocker();

    public T getItem(Long id) throws E {
        return readWriteActionLocker.read(() ->
                items.stream()
                        .filter(order -> id.equals(order.getId()))
                        .findAny()
                        .orElseThrow(() -> getException(id))
        );
    }

    public void saveItem(T item) {
        readWriteActionLocker.write(() -> {
            if (item.getId() == null) {
                item.setId(maxItemId++);
                items.add(item);
            } else {
                items.stream()
                        .filter(pr -> item.getId().equals(pr.getId()))
                        .findAny()
                        .ifPresentOrElse(val -> items.set(items.indexOf(val), item), () -> items.add(item));
            }
        });
    }

    public void deleteItem(Long id) {
        readWriteActionLocker.write(() ->
                items.removeIf(item -> id.equals(item.getId()))
        );
    }

    protected abstract E getException (Long id) throws E;
}
