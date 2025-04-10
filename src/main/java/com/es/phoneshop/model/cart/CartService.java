package com.es.phoneshop.model.cart;

import com.es.phoneshop.exceptions.OutOfStockException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

public interface CartService {

    Cart getCart(HttpServletRequest request);
    void add(Cart cart, Long productId, int quantity) throws OutOfStockException;
    void update(Cart cart, Long productId, int quantity) throws OutOfStockException;
    void remove(Cart cart, Long productId);
    void clear(HttpSession session, Cart cart);
}
