package com.es.phoneshop.web;

import com.es.phoneshop.enums.PaymentMethod;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CheckoutPageServletTest {
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private RequestDispatcher requestDispatcher;
    @Mock
    private ServletConfig servletConfig;
    @Mock
    private ServletContext servletContext;
    @Mock
    private HttpSession session;
    private static final String ORDER_ATTRIBUTE_NAME = "order";
    private static final String PAYMENT_METHODS_ATTRIBUTE_NAME = "paymentMethods";

    private static final String FIRSTNAME_PARAMETER_NAME = "firstName";
    private static final String LASTNAME_PARAMETER_NAME = "lastName";
    private static final String PHONE_PARAMETER_NAME = "phone";
    private static final String DELIVERY_DATE_PARAMETER_NAME = "deliveryDate";
    private static final String DELIVERY_ADDRESS_PARAMETER_NAME = "deliveryAddress";
    private static final String PAYMENT_METHOD_PARAMETER_NAME = "paymentMethod";

    private static final String VALID_STRING = "Example";
    private static final String VALID_PHONE = "+375444821914";
    private static final LocalDate VALID_DATE = LocalDate.of(2026, 1, 1);
    private static final PaymentMethod VALID_PAYMENT_METHOD = PaymentMethod.CASH;

    private static final String INVALID_DATE = "12.02.2020";
    private static final String EMPTY_STRING = "";


    private final CheckoutPageServlet servlet = new CheckoutPageServlet();

    @Before
    public void setup() throws ServletException {
        servlet.init(servletConfig);
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
        when(servletContext.getInitParameter("insertDemoData")).thenReturn("true");
        when(request.getSession()).thenReturn(session);
        when(servlet.getServletContext()).thenReturn(servletContext);

        DemoDataServletContextListener listener = new DemoDataServletContextListener();
        ServletContextEvent event = new ServletContextEvent(servletContext);
        listener.contextInitialized(event);
    }

    @Test
    public void testDoGet() throws ServletException, IOException {
        servlet.doGet(request, response);

        verify(requestDispatcher).forward(request, response);
        verify(request).setAttribute(eq(ORDER_ATTRIBUTE_NAME), any());
        verify(request).setAttribute(eq(PAYMENT_METHODS_ATTRIBUTE_NAME), any());
    }

    @Test
    public void testDoPostSuccess() throws ServletException, IOException {
        when(request.getParameter(eq(FIRSTNAME_PARAMETER_NAME))).thenReturn(VALID_STRING);
        when(request.getParameter(eq(LASTNAME_PARAMETER_NAME))).thenReturn(VALID_STRING);
        when(request.getParameter(eq(PHONE_PARAMETER_NAME))).thenReturn(VALID_PHONE);
        when(request.getParameter(eq(DELIVERY_DATE_PARAMETER_NAME))).thenReturn(VALID_DATE.toString());
        when(request.getParameter(eq(DELIVERY_ADDRESS_PARAMETER_NAME))).thenReturn(VALID_STRING);
        when(request.getParameter(eq(PAYMENT_METHOD_PARAMETER_NAME))).thenReturn(VALID_PAYMENT_METHOD.toString());

        servlet.doPost(request, response);

        verify(response).sendRedirect(anyString());
    }

    @Test
    public void testDoPostEmptyFirstName() throws ServletException, IOException {
        when(request.getParameter(eq(FIRSTNAME_PARAMETER_NAME))).thenReturn(EMPTY_STRING);
        when(request.getParameter(eq(LASTNAME_PARAMETER_NAME))).thenReturn(VALID_STRING);
        when(request.getParameter(eq(PHONE_PARAMETER_NAME))).thenReturn(VALID_PHONE);
        when(request.getParameter(eq(DELIVERY_DATE_PARAMETER_NAME))).thenReturn(VALID_DATE.toString());
        when(request.getParameter(eq(DELIVERY_ADDRESS_PARAMETER_NAME))).thenReturn(VALID_STRING);
        when(request.getParameter(eq(PAYMENT_METHOD_PARAMETER_NAME))).thenReturn(VALID_PAYMENT_METHOD.toString());

        servlet.doPost(request, response);

        verify(request).setAttribute(eq("errors"), any());
    }

    @Test
    public void testDoPostEmptyLastName() throws ServletException, IOException {
        when(request.getParameter(eq(FIRSTNAME_PARAMETER_NAME))).thenReturn(VALID_STRING);
        when(request.getParameter(eq(LASTNAME_PARAMETER_NAME))).thenReturn(EMPTY_STRING);
        when(request.getParameter(eq(PHONE_PARAMETER_NAME))).thenReturn(VALID_PHONE);
        when(request.getParameter(eq(DELIVERY_DATE_PARAMETER_NAME))).thenReturn(VALID_DATE.toString());
        when(request.getParameter(eq(DELIVERY_ADDRESS_PARAMETER_NAME))).thenReturn(VALID_STRING);
        when(request.getParameter(eq(PAYMENT_METHOD_PARAMETER_NAME))).thenReturn(VALID_PAYMENT_METHOD.toString());

        servlet.doPost(request, response);

        verify(request).setAttribute(eq("errors"), any());
    }

    @Test
    public void testDoPostEmptyPhone() throws ServletException, IOException {
        when(request.getParameter(eq(FIRSTNAME_PARAMETER_NAME))).thenReturn(VALID_STRING);
        when(request.getParameter(eq(LASTNAME_PARAMETER_NAME))).thenReturn(VALID_STRING);
        when(request.getParameter(eq(PHONE_PARAMETER_NAME))).thenReturn(EMPTY_STRING);
        when(request.getParameter(eq(DELIVERY_DATE_PARAMETER_NAME))).thenReturn(VALID_DATE.toString());
        when(request.getParameter(eq(DELIVERY_ADDRESS_PARAMETER_NAME))).thenReturn(VALID_STRING);
        when(request.getParameter(eq(PAYMENT_METHOD_PARAMETER_NAME))).thenReturn(VALID_PAYMENT_METHOD.toString());

        servlet.doPost(request, response);

        verify(request).setAttribute(eq("errors"), any());
    }

    @Test
    public void testDoPostEmptyDate() throws ServletException, IOException {
        when(request.getParameter(eq(FIRSTNAME_PARAMETER_NAME))).thenReturn(VALID_STRING);
        when(request.getParameter(eq(LASTNAME_PARAMETER_NAME))).thenReturn(VALID_STRING);
        when(request.getParameter(eq(PHONE_PARAMETER_NAME))).thenReturn(VALID_PHONE);
        when(request.getParameter(eq(DELIVERY_DATE_PARAMETER_NAME))).thenReturn(EMPTY_STRING);
        when(request.getParameter(eq(DELIVERY_ADDRESS_PARAMETER_NAME))).thenReturn(VALID_STRING);
        when(request.getParameter(eq(PAYMENT_METHOD_PARAMETER_NAME))).thenReturn(VALID_PAYMENT_METHOD.toString());

        servlet.doPost(request, response);

        verify(request).setAttribute(eq("errors"), any());
    }

    @Test
    public void testDoPostEmptyAddress() throws ServletException, IOException {
        when(request.getParameter(eq(FIRSTNAME_PARAMETER_NAME))).thenReturn(VALID_STRING);
        when(request.getParameter(eq(LASTNAME_PARAMETER_NAME))).thenReturn(VALID_STRING);
        when(request.getParameter(eq(PHONE_PARAMETER_NAME))).thenReturn(VALID_PHONE);
        when(request.getParameter(eq(DELIVERY_DATE_PARAMETER_NAME))).thenReturn(VALID_DATE.toString());
        when(request.getParameter(eq(DELIVERY_ADDRESS_PARAMETER_NAME))).thenReturn(EMPTY_STRING);
        when(request.getParameter(eq(PAYMENT_METHOD_PARAMETER_NAME))).thenReturn(VALID_PAYMENT_METHOD.toString());

        servlet.doPost(request, response);

        verify(request).setAttribute(eq("errors"), any());
    }

    @Test
    public void testDoPostEmptyPaymentMethod() throws ServletException, IOException {
        when(request.getParameter(eq(FIRSTNAME_PARAMETER_NAME))).thenReturn(VALID_STRING);
        when(request.getParameter(eq(LASTNAME_PARAMETER_NAME))).thenReturn(VALID_STRING);
        when(request.getParameter(eq(PHONE_PARAMETER_NAME))).thenReturn(VALID_PHONE);
        when(request.getParameter(eq(DELIVERY_DATE_PARAMETER_NAME))).thenReturn(VALID_DATE.toString());
        when(request.getParameter(eq(DELIVERY_ADDRESS_PARAMETER_NAME))).thenReturn(VALID_STRING);
        when(request.getParameter(eq(PAYMENT_METHOD_PARAMETER_NAME))).thenReturn(EMPTY_STRING);

        servlet.doPost(request, response);

        verify(request).setAttribute(eq("errors"), any());
    }

    @Test
    public void testDoPostInvalidDate() throws ServletException, IOException {
        when(request.getParameter(eq(FIRSTNAME_PARAMETER_NAME))).thenReturn(VALID_STRING);
        when(request.getParameter(eq(LASTNAME_PARAMETER_NAME))).thenReturn(VALID_STRING);
        when(request.getParameter(eq(PHONE_PARAMETER_NAME))).thenReturn(VALID_PHONE);
        when(request.getParameter(eq(DELIVERY_DATE_PARAMETER_NAME))).thenReturn(INVALID_DATE);
        when(request.getParameter(eq(DELIVERY_ADDRESS_PARAMETER_NAME))).thenReturn(VALID_STRING);
        when(request.getParameter(eq(PAYMENT_METHOD_PARAMETER_NAME))).thenReturn(VALID_PAYMENT_METHOD.toString());

        servlet.doPost(request, response);

        verify(request).setAttribute(eq("errors"), any());
    }
}
