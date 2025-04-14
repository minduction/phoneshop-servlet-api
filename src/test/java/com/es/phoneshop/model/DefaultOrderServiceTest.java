package com.es.phoneshop.model;

import com.es.phoneshop.exceptions.OutOfStockException;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartService;
import com.es.phoneshop.model.cart.DefaultCartService;
import com.es.phoneshop.model.order.DefaultOrderService;
import com.es.phoneshop.model.order.Order;
import com.es.phoneshop.model.order.OrderService;
import com.es.phoneshop.web.DemoDataServletContextListener;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DefaultOrderServiceTest {

    @Mock
    private HttpServletRequest request;
    @Mock
    private ServletContext servletContext;
    @Mock
    private HttpSession session;

    private final OrderService orderService = DefaultOrderService.getInstance();
    private final CartService cartService = DefaultCartService.getInstance();
    private static final Long EXISTING_PRODUCT_ID = 1L;

    @Before
    public void setup() {
        when(servletContext.getInitParameter("insertDemoData")).thenReturn("true");
        when(request.getSession()).thenReturn(session);

        DemoDataServletContextListener listener = new DemoDataServletContextListener();
        ServletContextEvent event = new ServletContextEvent(servletContext);
        listener.contextInitialized(event);
    }

    @Test
    public void testGetOrderCreatesOrderFromCart() throws OutOfStockException {
        Cart cart = cartService.getCart(request);
        cartService.update(cart, EXISTING_PRODUCT_ID, 2);
        Order order = orderService.getOrder(cart);

        Assert.assertEquals(order.getItems().size(), cart.getItems().size());
        Assert.assertEquals(order.getSubTotalCost(), cart.getTotalCost());
    }

    @Test
    public void testPlaceOrderSetsSecureId() throws OutOfStockException {
        Cart cart = cartService.getCart(request);
        cartService.update(cart, EXISTING_PRODUCT_ID, 2);
        Order order = orderService.getOrder(cart);
        orderService.placeOrder(order);

        Assert.assertNotNull(order.getSecureId());
    }
}
