package com.es.phoneshop.utils;

import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;

public class LimitedListTest {

    @Test
    public void testLimitedList(){
        LimitedList<Integer> list = new LimitedList<Integer>(3);
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);

        assertArrayEquals(new Integer[]{2, 3, 4}, list.getItems().toArray());
    }

}
