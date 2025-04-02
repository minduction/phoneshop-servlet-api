package com.es.phoneshop.web;

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

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DeleteCartItemServletTest {

    private static final String CORRECT_PATH = "/1";
    private static final String INVALID_PATH_INFO = "/abc";

    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private ServletConfig servletConfig;
    @Mock
    private ServletContext servletContext;
    @Mock
    private HttpSession session;

    private DeleteCartItemServlet servlet = new DeleteCartItemServlet();

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
    public void correctProductDeleteTest() throws Exception {
        when(request.getPathInfo()).thenReturn(CORRECT_PATH);
        when(request.getContextPath()).thenReturn("");

        servlet.doPost(request, response);

        verify(response).sendRedirect(anyString());
    }

    @Test(expected = NumberFormatException.class)
    public void doPostInvalidProductIdThrowsNumberFormatException() throws Exception {
        when(request.getPathInfo()).thenReturn(INVALID_PATH_INFO);

        servlet.doPost(request, response);
    }
}
