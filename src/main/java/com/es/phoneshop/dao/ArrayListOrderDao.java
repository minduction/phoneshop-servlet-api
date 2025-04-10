package com.es.phoneshop.dao;

import com.es.phoneshop.exceptions.OrderNotFoundException;
import com.es.phoneshop.model.order.Order;

import java.util.ArrayList;

public class ArrayListOrderDao extends GenericArrayListDao<Order, OrderNotFoundException> implements OrderDao {

    private static final class OrderDaoHolder {
        private static final OrderDao instance = new ArrayListOrderDao();
    }

    public static OrderDao getInstance() {
        return OrderDaoHolder.instance;
    }

    private ArrayListOrderDao() {
        items = new ArrayList<>();
        maxItemId = 1L;
    }

    @Override
    public Order getOrder(Long id) {
        return super.getItem(id);
    }

    @Override
    public void save(Order order) {
        super.saveItem(order);
    }

    @Override
    public Order getOrderBySecureId(String secureId) throws OrderNotFoundException {
        return readWriteActionLocker.read(() ->
            items.stream()
                    .filter(order -> secureId.equals(order.getSecureId()))
                    .findAny()
                    .orElseThrow(() -> new OrderNotFoundException("Order with SID = " + secureId + " not found"))
        );
    }

    @Override
    public void deleteItem(Long id) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected OrderNotFoundException getException(Long id) throws OrderNotFoundException {
        return new OrderNotFoundException("Order with id = " + id + " not found");
    }
}
