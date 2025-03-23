package com.es.phoneshop.model;

import com.es.phoneshop.dao.ArrayListProductDao;
import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.exceptions.OutOfStockException;
import com.es.phoneshop.locker.ReadWriteActionLocker;
import jakarta.servlet.http.HttpServletRequest;

public class DefaultCartService implements CartService {
    private static final String CART_SESSION_ATTRIBUTE = DefaultCartService.class.getName() + ".cart";

    private ProductDao productDao;

    private final ReadWriteActionLocker readWriteActionLocker = new ReadWriteActionLocker();

    private static final class DefaultCartServiceHolder {
        private static final DefaultCartService instance = new DefaultCartService();
    }

    public static DefaultCartService getInstance() {
        return DefaultCartServiceHolder.instance;
    }

    private DefaultCartService() {
        productDao = ArrayListProductDao.getInstance();
    }

    @Override
    public Cart getCart(HttpServletRequest request) {
        return readWriteActionLocker.read(() ->
            {
                Cart cart = (Cart) request.getSession().getAttribute(CART_SESSION_ATTRIBUTE);
                if (cart == null) {
                    request.getSession().setAttribute(CART_SESSION_ATTRIBUTE, cart = new Cart());
                }
                return cart;
            }
        );
    }

    @Override
    public void add(Cart cart, Long productId, int quantity) throws OutOfStockException {
        readWriteActionLocker.writeWithOutOfStockException(() ->
            {
                if (quantity <= 0) {
                    throw new IllegalArgumentException("Quantity must be greater than zero");
                }
                CartItem existingCartItem = cart.getItems().stream()
                        .filter(item -> productId.equals(item.getProduct().getId()))
                        .findAny()
                        .orElse(null);
                Product product = productDao.getProduct(productId);
                int summaryQuantity = existingCartItem == null ? quantity : existingCartItem.getQuantity() + quantity;
                if (summaryQuantity > product.getStock()) {
                    throw new OutOfStockException(product, summaryQuantity, product.getStock());
                }
                if (existingCartItem != null) {
                    existingCartItem.setQuantity(existingCartItem.getQuantity() + quantity);
                } else {
                    cart.getItems().add(new CartItem(product, quantity));
                }
            });
    }
}
