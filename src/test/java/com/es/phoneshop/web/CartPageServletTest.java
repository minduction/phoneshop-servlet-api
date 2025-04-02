package com.es.phoneshop.web;

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
import java.util.Locale;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CartPageServletTest {
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
    private static final Locale LOCALE = Locale.US;
    private static final String VALID_QUANTITY = "1";
    private static final String VALID_PRODUCT_ID = "1";
    private static final String NOT_A_NUMBER_QUANTITY = "asd";
    private static final String TOO_BIG_QUANTITY = "1000000";
    private static final String NEGATIVE_QUANTITY = "-1";
    private static final String QUANTITY_PARAMETER_NAME = "quantity";
    private static final String PRODUCT_ID_PARAMETER_NAME = "productId";

    private final CartPageServlet servlet = new CartPageServlet();

    @Before
    public void setup() throws ServletException {
        servlet.init(servletConfig);
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
        when(servletContext.getInitParameter("insertDemoData")).thenReturn("true");
        when(request.getSession()).thenReturn(session);
        when(request.getLocale()).thenReturn(LOCALE);
        when(servlet.getServletContext()).thenReturn(servletContext);


        DemoDataServletContextListener listener = new DemoDataServletContextListener();
        ServletContextEvent event = new ServletContextEvent(servletContext);
        listener.contextInitialized(event);
    }

    @Test
    public void testDoGet() throws ServletException, IOException {
        servlet.doGet(request, response);

        verify(requestDispatcher).forward(request, response);
        verify(request).setAttribute(eq("cart"), any());
    }

    @Test
    public void testDoPostSuccess() throws ServletException, IOException {
        String[] quantities = {VALID_QUANTITY};
        String[] productIds = {VALID_PRODUCT_ID};
        when(request.getParameterValues(QUANTITY_PARAMETER_NAME)).thenReturn(quantities);
        when(request.getParameterValues(PRODUCT_ID_PARAMETER_NAME)).thenReturn(productIds);

        servlet.doPost(request, response);

        verify(response).sendRedirect(anyString());
    }

    @Test
    public void testDoPostOutOfStock() throws ServletException, IOException {
        String[] quantities = {TOO_BIG_QUANTITY};
        String[] productIds = {VALID_PRODUCT_ID};
        when(request.getParameterValues(QUANTITY_PARAMETER_NAME)).thenReturn(quantities);
        when(request.getParameterValues(PRODUCT_ID_PARAMETER_NAME)).thenReturn(productIds);

        servlet.doPost(request, response);

        verify(request).setAttribute(eq("errors"), any());
    }

    @Test
    public void testDoPostNumberFormat() throws ServletException, IOException {
        String[] quantities = {NOT_A_NUMBER_QUANTITY};
        String[] productIds = {VALID_PRODUCT_ID};
        when(request.getParameterValues(QUANTITY_PARAMETER_NAME)).thenReturn(quantities);
        when(request.getParameterValues(PRODUCT_ID_PARAMETER_NAME)).thenReturn(productIds);

        servlet.doPost(request, response);

        verify(request).setAttribute(eq("errors"), any());
    }

    @Test
    public void testDoPostNegativeNumber() throws ServletException, IOException {
        String[] quantities = {NEGATIVE_QUANTITY};
        String[] productIds = {VALID_PRODUCT_ID};
        when(request.getParameterValues(QUANTITY_PARAMETER_NAME)).thenReturn(quantities);
        when(request.getParameterValues(PRODUCT_ID_PARAMETER_NAME)).thenReturn(productIds);

        servlet.doPost(request, response);

        verify(request).setAttribute(eq("errors"), any());
    }

}
