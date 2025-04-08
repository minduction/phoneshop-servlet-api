package com.es.phoneshop.model.order;

import com.es.phoneshop.dao.ArrayListOrderDao;
import com.es.phoneshop.dao.OrderDao;
import com.es.phoneshop.enums.PaymentMethod;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartItem;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class DefaultOrderService implements OrderService {
    private OrderDao orderDao = ArrayListOrderDao.getInstance();

    private static final class DefaultOrderServiceHolder {
        private static final DefaultOrderService instance = new DefaultOrderService();
    }

    public static DefaultOrderService getInstance() {
        return DefaultOrderServiceHolder.instance;
    }

    private DefaultOrderService() {

    }

    @Override
    public Order getOrder(Cart cart) {
        Order order = new Order();
        order.setItems(cart.getItems().stream()
                .map(this::cloneCartItem)
                .toList());
        order.setSubTotalCost(cart.getTotalCost());
        order.setDeliveryCost(calculateDeliveryCost());
        order.setTotalCost(order.getSubTotalCost() != null ? order.getSubTotalCost().add(order.getDeliveryCost()) : BigDecimal.ZERO);
        return order;
    }

    private CartItem cloneCartItem(CartItem cartItem) {
        try{
            return (CartItem) cartItem.clone();
        }
        catch (CloneNotSupportedException e){
            throw new RuntimeException(e);
        }
    }

    private BigDecimal calculateDeliveryCost() {
        return new BigDecimal(5);
    }

    @Override
    public void placeOrder(Order order) {
        orderDao.save(order);
        order.setSecureId(UUID.randomUUID().toString());
    }

    @Override
    public List<PaymentMethod> getPaymentMethods() {
        return Arrays.asList(PaymentMethod.values());
    }
}
