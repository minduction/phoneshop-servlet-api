package com.es.phoneshop.exceptions;

import com.es.phoneshop.model.Product;

public class OutOfStockException extends Exception {
    private final Product product;
    private final int quantityRequested;
    private final int quantityAvailable;

    public OutOfStockException(Product product, int quantityRequested, int quantityAvailable) {
        this.product = product;
        this.quantityRequested = quantityRequested;
        this.quantityAvailable = quantityAvailable;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantityRequested() {
        return quantityRequested;
    }

    public int getQuantityAvailable() {
        return quantityAvailable;
    }
}
