package com.es.phoneshop.model;

import com.es.phoneshop.exceptions.OutOfStockException;
import jakarta.servlet.http.HttpServletRequest;

public interface CartService {

    Cart getCart(HttpServletRequest request);
    void add(Cart cart, Long productId, int quantity) throws OutOfStockException;
}
