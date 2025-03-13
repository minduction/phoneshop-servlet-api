package com.es.phoneshop.model.product;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Supplier;

public class ReadWriteActionLocker {
    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    public <T> T read(Supplier<T> action){
        lock.readLock().lock();
        try{
            return action.get();
        } finally {
            lock.readLock().unlock();
        }
    }

    public void write(WriteAction action){
        lock.writeLock().lock();
        try{
            action.run();
        }
        finally {
            lock.writeLock().unlock();
        }
    }
}
