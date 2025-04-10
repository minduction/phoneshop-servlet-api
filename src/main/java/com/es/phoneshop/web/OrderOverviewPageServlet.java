package com.es.phoneshop.web;

import com.es.phoneshop.dao.ArrayListOrderDao;
import com.es.phoneshop.dao.OrderDao;
import com.es.phoneshop.model.order.DefaultOrderService;
import com.es.phoneshop.model.order.Order;
import com.es.phoneshop.model.order.OrderService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class OrderOverviewPageServlet extends HttpServlet {

    private static final String OVERVIEW_JSP = "/WEB-INF/pages/orderOverview.jsp";
    private static final int INDEX_FOR_ID_SUBSTRING = 1;

    private OrderService orderService;
    private OrderDao orderDao;

    @Override
    public void init() throws ServletException {
        super.init();
        orderService = DefaultOrderService.getInstance();
        orderDao = ArrayListOrderDao.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Order order = orderDao.getOrderBySecureId(request.getPathInfo().substring(INDEX_FOR_ID_SUBSTRING));

        request.setAttribute("order", order);
        request.setAttribute("paymentMethods", orderService.getPaymentMethods());

        request.getRequestDispatcher(OVERVIEW_JSP).forward(request, response);
    }
}
