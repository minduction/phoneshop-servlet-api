package com.es.phoneshop.model;


import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Cart implements Serializable {

    private List<CartItem> items;
    private int totalQuantity;

    public void setTotalQuantity(int totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public int getTotalQuantity() {
        return totalQuantity;
    }

    private BigDecimal totalPrice;

    public Cart() {
        this.items = new ArrayList<CartItem>();
    }

    public List<CartItem> getItems() {
        return items;
    }

    @Override
    public String toString() {
        return "Cart [" + items + "]";
    }
}
