package com.es.phoneshop.web;

import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.util.stream.IntStream;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DosFilterTest {
    private static final int THRESHOLD = 20;

    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private FilterChain filterChain;
    @Mock
    private FilterConfig filterConfig;

    private final DosFilter dosFilter = new DosFilter();
    private static final int TOO_MANY_REQUESTS_STATUS = 429;

    @Before
    public void setUp() throws ServletException {
        dosFilter.init(filterConfig);
    }

    @Test
    public void testDoFilterAccepts() throws ServletException, IOException {
        when(request.getRemoteAddr()).thenReturn("127.0.0.1");
        dosFilter.doFilter(request, response, filterChain);

        verify(filterChain).doFilter(eq(request), eq(response));
    }

    @Test
    public void testDoFilterRejects() throws ServletException, IOException {
        when(request.getRemoteAddr()).thenReturn("127.0.0.2");
        IntStream.range(0, THRESHOLD).forEach(i -> {
            try {
                dosFilter.doFilter(request, response, filterChain);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        dosFilter.doFilter(request, response, filterChain);

        verify(response, times(1)).setStatus(TOO_MANY_REQUESTS_STATUS);
    }
}
