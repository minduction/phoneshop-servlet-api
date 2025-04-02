package com.es.phoneshop.utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LimitedList <T> implements Serializable {
    private final List<T> items;

    private int limit;
    private boolean returnReversed = false;

    public void setReturnReversed(boolean returnReversed) {
        this.returnReversed = returnReversed;
    }

    public boolean isReturnReversed() {
        return returnReversed;
    }

    public LimitedList(int limit) {
        items = new ArrayList<T>();
        this.limit = limit;
    }

    public int getLimit() {
        return limit;
    }

    public List<T> getItems() {
        if (!returnReversed){
            return items;
        }
        List<T> reversedList = new ArrayList<>(items);
        Collections.reverse(reversedList);
        return reversedList;
    }

    public void remove(T item) {
        items.remove(item);
    }

    public void add(T item){
        if (items.size() < limit){
            items.add(item);
        } else {
            items.remove(0);
            items.add(item);
        }
    }
}
