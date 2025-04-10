package com.es.phoneshop.web;

import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartService;
import com.es.phoneshop.model.cart.DefaultCartService;
import com.es.phoneshop.model.order.DefaultOrderService;
import com.es.phoneshop.model.order.Order;
import com.es.phoneshop.model.order.OrderService;
import com.es.phoneshop.validation.PersonalInfoValidator;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CheckoutPageServlet extends HttpServlet {

    private static final String CHECKOUT_JSP = "/WEB-INF/pages/checkout.jsp";

    private static final String FIRSTNAME_PARAMETER_NAME = "firstName";
    private static final String LASTNAME_PARAMETER_NAME = "lastName";
    private static final String PHONE_PARAMETER_NAME = "phone";
    private static final String DELIVERY_DATE_PARAMETER_NAME = "deliveryDate";
    private static final String DELIVERY_ADDRESS_PARAMETER_NAME = "deliveryAddress";
    private static final String PAYMENT_METHOD_PARAMETER_NAME = "paymentMethod";

    private CartService cartService;
    private OrderService orderService;

    @Override
    public void init() throws ServletException {
        super.init();
        cartService = DefaultCartService.getInstance();
        orderService = DefaultOrderService.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        request.setAttribute("order", orderService.getOrder(cartService.getCart(request)));
        request.setAttribute("paymentMethods", orderService.getPaymentMethods());
        request.getRequestDispatcher(CHECKOUT_JSP).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Cart cart = cartService.getCart(request);
        Order order = orderService.getOrder(cart);
        Map<String, String> errors = new HashMap<>();

        PersonalInfoValidator.setCorrectString(request.getParameter(FIRSTNAME_PARAMETER_NAME),
                FIRSTNAME_PARAMETER_NAME, errors, order::setFirstName);
        PersonalInfoValidator.setCorrectString(request.getParameter(LASTNAME_PARAMETER_NAME),
                LASTNAME_PARAMETER_NAME, errors, order::setLastName);
        PersonalInfoValidator.setPhoneNumber(request.getParameter(PHONE_PARAMETER_NAME), errors, order::setPhone);
        PersonalInfoValidator.setDeliveryDate(request.getParameter(DELIVERY_DATE_PARAMETER_NAME), errors, order::setDeliveryDate);
        PersonalInfoValidator.setCorrectString(request.getParameter(DELIVERY_ADDRESS_PARAMETER_NAME),
                DELIVERY_ADDRESS_PARAMETER_NAME, errors, order::setDeliveryAddress);
        PersonalInfoValidator.setPaymentMethod(request.getParameter(PAYMENT_METHOD_PARAMETER_NAME), errors, order::setPaymentMethod);

        if (errors.isEmpty()) {
            orderService.placeOrder(order);
            cartService.clear(request.getSession(), cart);
            response.sendRedirect(getServletContext().getContextPath() + "/order/overview/" + order.getSecureId());
        } else {
            request.setAttribute("errors", errors);
            request.setAttribute("order", order);
            request.setAttribute("paymentMethods", orderService.getPaymentMethods());
            request.getRequestDispatcher(CHECKOUT_JSP).forward(request, response);
        }
    }
}
