package com.es.phoneshop.model;

import com.es.phoneshop.exceptions.OutOfStockException;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.DefaultCartService;
import com.es.phoneshop.web.DemoDataServletContextListener;
import com.es.phoneshop.web.ProductDetailsPageServlet;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DefaultCartServiceTest {

    @Mock
    private HttpServletRequest request;
    @Mock
    private ServletConfig servletConfig;
    @Mock
    private ServletContext servletContext;
    @Mock
    private HttpSession session;

    private final ProductDetailsPageServlet servlet = new ProductDetailsPageServlet();
    private final DefaultCartService defaultCartService = DefaultCartService.getInstance();
    private static final String CART_SESSION_ATTRIBUTE = DefaultCartService.class.getName() + ".cart";

    @Before
    public void setup() throws ServletException {
        servlet.init(servletConfig);
        when(servletContext.getInitParameter("insertDemoData")).thenReturn("true");
        when(request.getSession()).thenReturn(session);

        DemoDataServletContextListener listener = new DemoDataServletContextListener();
        ServletContextEvent event = new ServletContextEvent(servletContext);
        listener.contextInitialized(event);
    }

    @Test
    public void testGetCartGetsCartAttribute(){
        defaultCartService.getCart(request);

        verify(request.getSession()).getAttribute(CART_SESSION_ATTRIBUTE);
    }

    @Test
    public void testGetCartSetsCartAttribute(){
        request.setAttribute(CART_SESSION_ATTRIBUTE, null);
        defaultCartService.getCart(request);

        verify(request.getSession()).setAttribute(eq(CART_SESSION_ATTRIBUTE), any());
    }

    @Test
    public void testAddToCart() throws OutOfStockException {
        Cart cart = defaultCartService.getCart(request);
        Long productId = 1L;
        defaultCartService.add(cart, productId, 1);

        Assert.assertEquals(1, cart.getItems().size());
    }

    @Test(expected = OutOfStockException.class)
    public void testAddToCartTooBigValue() throws OutOfStockException {
        Cart cart = defaultCartService.getCart(request);
        Long productId = 1L;

        defaultCartService.add(cart, productId, 1000000);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddToCartNegativeValue() throws OutOfStockException {
        Cart cart = defaultCartService.getCart(request);
        Long productId = 1L;

        defaultCartService.add(cart, productId, -111);
    }

    @Test
    public void testAddToCartSeveralTimes() throws OutOfStockException {
        Cart cart = defaultCartService.getCart(request);
        Long productId = 1L;
        defaultCartService.add(cart, productId, 1);
        defaultCartService.add(cart, productId, 2);

        Assert.assertEquals(3, cart.getItems().stream()
                .filter(item -> productId.equals(item.getProduct().getId()))
                .findAny().get().getQuantity());
    }

    @Test
    public void testUpdateCartSeveralTimes() throws OutOfStockException {
        Cart cart = defaultCartService.getCart(request);
        Long productId = 1L;
        defaultCartService.update(cart, productId, 1);
        defaultCartService.update(cart, productId, 1);

        Assert.assertEquals(1, cart.getItems().stream()
                .filter(item -> productId.equals(item.getProduct().getId()))
                .findAny().get().getQuantity());
    }

    @Test(expected = OutOfStockException.class)
    public void testUpdateOutOfStock() throws OutOfStockException {
        Cart cart = defaultCartService.getCart(request);
        Long productId = 1L;

        defaultCartService.update(cart, productId, 1000000);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateNegativeQuantity() throws OutOfStockException {
        Cart cart = defaultCartService.getCart(request);
        Long productId = 1L;

        defaultCartService.update(cart, productId, -1);
    }

    @Test
    public void testRemove() throws OutOfStockException {
        Cart cart = defaultCartService.getCart(request);
        Long productId = 1L;
        defaultCartService.update(cart, productId, 1);
        defaultCartService.remove(cart, productId);

        Assert.assertEquals(0, cart.getItems().size());
    }

    @Test
    public void testClear() throws OutOfStockException {
        Cart cart = defaultCartService.getCart(request);
        defaultCartService.update(cart, 1L, 2);
        defaultCartService.update(cart, 3L, 1);
        defaultCartService.clear(request.getSession(), cart);

        verify(request.getSession()).removeAttribute(CART_SESSION_ATTRIBUTE);
        Assert.assertEquals(0, cart.getItems().size());
        Assert.assertEquals(0, cart.getTotalQuantity());
        Assert.assertEquals(BigDecimal.ZERO, cart.getTotalCost());
    }
}
