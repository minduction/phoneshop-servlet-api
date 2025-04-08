package com.es.phoneshop.model;

import com.es.phoneshop.dao.ArrayListOrderDao;
import com.es.phoneshop.dao.OrderDao;
import com.es.phoneshop.exceptions.OrderNotFoundException;
import com.es.phoneshop.model.order.Order;
import com.es.phoneshop.web.DemoDataServletContextListener;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.UUID;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ArrayListOrderDaoTest
{
    @Mock
    private ServletContext servletContext;

    private OrderDao orderDao;
    private static final Long EXISTING_ORDER_ID = 4L;
    private static final Long NOT_EXISTING_ORDER_ID = 1000L;
    private static final String NOT_EXISTING_SECURE_ORDER_ID = "Do not exist";


    @Before
    public void setup() {
        orderDao = ArrayListOrderDao.getInstance();
        when(servletContext.getInitParameter("insertDemoData")).thenReturn("true");

        DemoDataServletContextListener listener = new DemoDataServletContextListener();
        ServletContextEvent event = new ServletContextEvent(servletContext);
        listener.contextInitialized(event);
    }

    @Test
    public void testGetOrderById(){
        Order order = new Order();
        order.setId(EXISTING_ORDER_ID);
        orderDao.save(order);
        Order result = orderDao.getOrder(EXISTING_ORDER_ID);

        Assert.assertNotNull(result);
    }

    @Test(expected = OrderNotFoundException.class)
    public void testGetOrderByWrongId(){
        orderDao.getOrder(NOT_EXISTING_ORDER_ID);
    }

    @Test
    public void testGetOrderBySecureId(){
        Order order = new Order();
        String secureId = UUID.randomUUID().toString();
        order.setSecureId(secureId);
        order.setId(EXISTING_ORDER_ID);
        orderDao.save(order);

        Assert.assertNotNull(orderDao.getOrderBySecureId(secureId));
    }

    @Test(expected = OrderNotFoundException.class)
    public void testGetOrderByWrongSecureId(){
        orderDao.getOrderBySecureId(NOT_EXISTING_SECURE_ORDER_ID);
    }

    @Test
    public void testOrderSaveIncrementsId(){
        Order order1 = new Order();
        Order order2 = new Order();
        orderDao.save(order1);
        orderDao.save(order2);

        Assert.assertEquals(1, order2.getId() - order1.getId());
    }

}
