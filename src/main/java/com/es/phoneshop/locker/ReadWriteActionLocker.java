package com.es.phoneshop.locker;

import com.es.phoneshop.exceptions.OutOfStockException;

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

    public void writeWithOutOfStockException(WriteActionWithOutOfStockException action) throws OutOfStockException {
        lock.writeLock().lock();
        try{
            action.write();
        }
        finally {
            lock.writeLock().unlock();
        }
    }
}
