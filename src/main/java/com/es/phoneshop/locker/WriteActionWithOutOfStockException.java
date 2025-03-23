package com.es.phoneshop.locker;

import com.es.phoneshop.exceptions.OutOfStockException;

@FunctionalInterface
public interface WriteActionWithOutOfStockException {
    void write() throws OutOfStockException;
}
