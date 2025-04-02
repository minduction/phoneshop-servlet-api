package com.es.phoneshop.web;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.http.HttpSession;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Locale;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ProductListPageServletTest {

    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private RequestDispatcher requestDispatcher;
    @Mock
    private ServletConfig config;
    @Mock
    private HttpSession session;
    @Mock
    private ServletContext servletContext;

    private static final Locale LOCALE = Locale.US;
    private static final String VALID_QUANTITY = "1";
    private static final String VALID_PRODUCT_ID = "1";
    private static final String NOT_A_NUMBER_QUANTITY = "asd";
    private static final String TOO_BIG_QUANTITY = "1000000";
    private static final String QUANTITY_PARAMETER_NAME = "quantity";
    private static final String PRODUCT_ID_PARAMETER_NAME = "productId";
    private static final String INVALID_NUMBER_VALUE_MESSAGE = "Invalid number value";

    private final ProductListPageServlet servlet = new ProductListPageServlet();

    @Before
    public void setup() throws ServletException {
        servlet.init(config);
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
        when(request.getSession()).thenReturn(session);
        when(request.getLocale()).thenReturn(LOCALE);
        when(servletContext.getInitParameter("insertDemoData")).thenReturn("true");

        DemoDataServletContextListener listener = new DemoDataServletContextListener();
        ServletContextEvent event = new ServletContextEvent(servletContext);
        listener.contextInitialized(event);
    }

    @Test
    public void testDoGetSetsProductsAttribute() throws ServletException, IOException {
        servlet.doGet(request, response);

        verify(requestDispatcher).forward(request, response);
        verify(request).setAttribute(eq("products"), any());
    }

    @Test
    public void testDoGetGetsQueryParam() throws ServletException, IOException {
        servlet.doGet(request, response);

        verify(request).getParameter(eq("query"));
    }

    @Test
    public void testDoGetGetsSortParam() throws ServletException, IOException {
        servlet.doGet(request, response);

        verify(request).getParameter(eq("sort"));
    }

    @Test
    public void testDoGetGetsOrderParam() throws ServletException, IOException {
        servlet.doGet(request, response);

        verify(request).getParameter(eq("order"));
    }

    @Test
    public void testDoPostWithValidData() throws ServletException, IOException {
        when(request.getParameter(eq(QUANTITY_PARAMETER_NAME))).thenReturn(VALID_QUANTITY);
        when(request.getParameter(eq(PRODUCT_ID_PARAMETER_NAME))).thenReturn(VALID_PRODUCT_ID);
        when(servlet.getServletContext()).thenReturn(servletContext);
        when(servletContext.getContextPath()).thenReturn("");
        servlet.doPost(request, response);

        verify(response).sendRedirect(servletContext.getContextPath() + "/products" + "?message=Product added to cart successfully!");
    }

    @Test
    public void testDoPostWithNotANumber() throws ServletException, IOException {
        when(request.getParameter(eq(QUANTITY_PARAMETER_NAME))).thenReturn(NOT_A_NUMBER_QUANTITY);
        when(request.getParameter(eq(PRODUCT_ID_PARAMETER_NAME))).thenReturn(VALID_QUANTITY);
        servlet.doPost(request, response);

        verify(request).setAttribute("error", INVALID_NUMBER_VALUE_MESSAGE);
        verify(response, times(0)).sendRedirect(any());
    }

    @Test
    public void testDoPostWithBigQuantity() throws ServletException, IOException {
        when(request.getParameter(eq(QUANTITY_PARAMETER_NAME))).thenReturn(TOO_BIG_QUANTITY);
        when(request.getParameter(eq(PRODUCT_ID_PARAMETER_NAME))).thenReturn(VALID_PRODUCT_ID);
        servlet.doPost(request, response);

        verify(request).setAttribute(eq("error"), anyString());
        verify(response, times(0)).sendRedirect(any());
    }
}
