package com.es.phoneshop.web;

import com.es.phoneshop.exceptions.OutOfStockException;
import com.es.phoneshop.model.Cart;
import com.es.phoneshop.model.CartService;
import com.es.phoneshop.model.DefaultCartService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

public class CartPageServlet extends HttpServlet {

    private static final String CART_JSP = "/WEB-INF/pages/cart.jsp";

    private CartService cartService;

    @Override
    public void init() throws ServletException {
        super.init();
        cartService = DefaultCartService.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        request.setAttribute("cart", cartService.getCart(request));

        request.getRequestDispatcher(CART_JSP).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String[] quantities = request.getParameterValues("quantity");
        String[] productIds = request.getParameterValues("productId");
        Map<Long, String> errors = new HashMap<>();
        if (quantities == null || productIds == null || quantities.length != productIds.length) {
            doGet(request, response);
            return;
        }
        Cart cart = cartService.getCart(request);
        IntStream.range(0, quantities.length).forEach(i -> {
            Long productId = parseProductIdFromRequest(productIds[i]);

            Integer quantity = parseQuantityFromString(quantities[i], request);
            if (quantity == null || quantity <= 0) {
                errors.put(productId, "Invalid number value");
                return;
            }
            try {
                cartService.update(cart, productId, quantity);
            } catch (OutOfStockException e) {
                errors.put(productId, "Out of stock, available = " + e.getQuantityAvailable());
            }
        });
        if (errors.isEmpty()) {
            response.sendRedirect(getServletContext().getContextPath() + "/cart" + "?message=Cart updated successfully!");
        } else {
            request.setAttribute("errors", errors);
            doGet(request, response);
        }
    }

    private Long parseProductIdFromRequest(String productId) {
        try{
            return Long.parseLong(productId);
        }
        catch (NumberFormatException e){
            return null;
        }
    }

    private Integer parseQuantityFromString(String quantity, HttpServletRequest request){
        NumberFormat numberFormat = NumberFormat.getInstance(request.getLocale());
        try{
            return numberFormat.parse(quantity).intValue();
        }
        catch (ParseException e){
            return null;
        }
    }
}
