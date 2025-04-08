package com.es.phoneshop.model.order;

import com.es.phoneshop.enums.PaymentMethod;
import com.es.phoneshop.model.cart.Cart;

import java.util.List;

public interface OrderService {
    Order getOrder(Cart cart);
    void placeOrder(Order order);
    List<PaymentMethod> getPaymentMethods();
}
