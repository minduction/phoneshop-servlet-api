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
        when(request.getParameter(eq("quantity"))).thenReturn("1");
        when(request.getParameter(eq("productId"))).thenReturn("1");
        when(servlet.getServletContext()).thenReturn(servletContext);
        when(servletContext.getContextPath()).thenReturn("");
        servlet.doPost(request, response);

        verify(response).sendRedirect(servletContext.getContextPath() + "/products" + "?message=Product added to cart successfully!");
    }

    @Test
    public void testDoPostWithNotANumber() throws ServletException, IOException {
        when(request.getParameter(eq("quantity"))).thenReturn("asd");
        when(request.getParameter(eq("productId"))).thenReturn("1");
        servlet.doPost(request, response);

        verify(request).setAttribute("error", "Not a number");
        verify(response, times(0)).sendRedirect(any());
    }

    @Test
    public void testDoPostWithBigQuantity() throws ServletException, IOException {
        when(request.getParameter(eq("quantity"))).thenReturn("10000");
        when(request.getParameter(eq("productId"))).thenReturn("1");
        servlet.doPost(request, response);

        verify(request).setAttribute(eq("error"), anyString());
        verify(response, times(0)).sendRedirect(any());
    }
}
