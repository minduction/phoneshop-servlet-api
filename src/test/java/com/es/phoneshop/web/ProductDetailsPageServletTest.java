package com.es.phoneshop.web;

import com.es.phoneshop.exceptions.ProductNotFoundException;
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
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ProductDetailsPageServletTest {
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

    private final ProductDetailsPageServlet servlet = new ProductDetailsPageServlet();

    private static final String RECENTLY_VISITED_SESSION_ATTRIBUTE = "recentlyVisitedProducts";
    private static final String EXISTING_PRODUCT_PATH_INFO = "/1";
    private static final String NON_EXISTING_PRODUCT_PATH_INFO = "/9999";
    private static final String VALID_QUANTITY = "1";
    private static final String NOT_A_NUMBER_QUANTITY = "asd";
    private static final String TOO_BIG_QUANTITY = "1000000";
    private static final String QUANTITY_PARAMETER_NAME = "quantity";
    private static final String INVALID_NUMBER_VALUE_MESSAGE = "Invalid number value";

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
        when(request.getPathInfo()).thenReturn(EXISTING_PRODUCT_PATH_INFO);
        servlet.doGet(request, response);

        verify(requestDispatcher).forward(request, response);
        verify(request).setAttribute(eq("product"), any());
        verify(request).getPathInfo();
    }

    @Test
    public void testDoGetGetsRecentlyVisitedAttribute() throws ServletException, IOException {
        when(request.getPathInfo()).thenReturn(EXISTING_PRODUCT_PATH_INFO);
        servlet.doGet(request, response);

        verify(request.getSession()).getAttribute(eq(RECENTLY_VISITED_SESSION_ATTRIBUTE));
    }

    @Test
    public void testDoGetSetsCart() throws ServletException, IOException {
        when(request.getPathInfo()).thenReturn(EXISTING_PRODUCT_PATH_INFO);
        servlet.doGet(request, response);

        verify(request).setAttribute(eq("cart"), any());
    }

    @Test(expected = ProductNotFoundException.class)
    public void testDoGetNonExistingProduct() throws ServletException, IOException {
        when(request.getPathInfo()).thenReturn(NON_EXISTING_PRODUCT_PATH_INFO);

        servlet.doGet(request, response);
    }

    @Test
    public void testDoPostWithValidData() throws ServletException, IOException {
        when(request.getPathInfo()).thenReturn(EXISTING_PRODUCT_PATH_INFO);
        when(request.getParameter(eq(QUANTITY_PARAMETER_NAME))).thenReturn(VALID_QUANTITY);
        servlet.doPost(request, response);

        verify(response).sendRedirect(servletContext.getContextPath() + "/products" + EXISTING_PRODUCT_PATH_INFO + "?message=Product added to cart successfully!");
    }

    @Test
    public void testDoPostWithNotANumber() throws ServletException, IOException {
        when(request.getPathInfo()).thenReturn(EXISTING_PRODUCT_PATH_INFO);
        when(request.getParameter(eq(QUANTITY_PARAMETER_NAME))).thenReturn(NOT_A_NUMBER_QUANTITY);
        servlet.doPost(request, response);

        verify(request).setAttribute("error", INVALID_NUMBER_VALUE_MESSAGE);
        verify(response, times(0)).sendRedirect(any());
    }

    @Test
    public void testDoPostWithBigQuantity() throws ServletException, IOException {
        when(request.getPathInfo()).thenReturn(EXISTING_PRODUCT_PATH_INFO);
        when(request.getParameter(eq(QUANTITY_PARAMETER_NAME))).thenReturn(TOO_BIG_QUANTITY);
        servlet.doPost(request, response);

        verify(request).setAttribute(eq("error"), anyString());
        verify(response, times(0)).sendRedirect(any());
    }
}
