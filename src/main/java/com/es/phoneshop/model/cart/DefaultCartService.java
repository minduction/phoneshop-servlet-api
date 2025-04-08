package com.es.phoneshop.model.cart;

import com.es.phoneshop.dao.ArrayListProductDao;
import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.exceptions.OutOfStockException;
import com.es.phoneshop.locker.ReadWriteActionLocker;
import com.es.phoneshop.model.Product;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.math.BigDecimal;
import java.util.ArrayList;

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
                CartItem existingCartItem = findExistingCartItem(cart, productId);
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
        recalculateTotalQuantity(cart);
        recalculateTotalPrice(cart);
    }

    @Override
    public void update(Cart cart, Long productId, int quantity) throws OutOfStockException {
        readWriteActionLocker.writeWithOutOfStockException(() ->
        {
            if (quantity <= 0) {
                throw new IllegalArgumentException("Quantity must be greater than zero");
            }
            CartItem existingCartItem = findExistingCartItem(cart, productId);
            Product product = productDao.getProduct(productId);
            if (quantity > product.getStock()) {
                throw new OutOfStockException(product, quantity, product.getStock());
            }
            if (existingCartItem != null) {
                existingCartItem.setQuantity(quantity);
            } else {
                cart.getItems().add(new CartItem(product, quantity));
            }
        });
        recalculateTotalQuantity(cart);
        recalculateTotalPrice(cart);
    }

    @Override
    public void remove(Cart cart, Long productId) {
        readWriteActionLocker.write(() -> {
            cart.getItems().removeIf(
                    cartItem -> productId.equals(cartItem.getProduct().getId())
            );
            recalculateTotalQuantity(cart);
            recalculateTotalPrice(cart);
        });
    }

    private CartItem findExistingCartItem(Cart cart, Long productId) {
        return cart.getItems().stream()
                .filter(item -> productId.equals(item.getProduct().getId()))
                .findAny()
                .orElse(null);
    }

    private void recalculateTotalQuantity(Cart cart) {
        cart.setTotalQuantity(cart.getItems().stream()
            .map(CartItem::getQuantity)
            .mapToInt(Integer::intValue)
            .sum());
    }

    private void recalculateTotalPrice(Cart cart) {
        cart.setTotalCost(
            cart.getItems().stream()
                .map(cartItem ->
                    cartItem.getProduct().getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity()))
                )
                .reduce(BigDecimal.ZERO, BigDecimal::add)
        );
    }

    @Override
    public void clear(HttpSession session, Cart cart) {
        readWriteActionLocker.write(() -> {
            cart.setItems(new ArrayList<>());
            cart.setTotalCost(BigDecimal.ZERO);
            cart.setTotalQuantity(0);
            session.removeAttribute(CART_SESSION_ATTRIBUTE);
        });
    }
}
