package com.es.phoneshop.web;

import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartService;
import com.es.phoneshop.model.cart.DefaultCartService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class DeleteCartItemServlet extends HttpServlet {

    private CartService cartService;
    private static final int INDEX_FOR_ID_SUBSTRING = 1;

    @Override
    public void init() throws ServletException {
        super.init();
        cartService = DefaultCartService.getInstance();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Cart cart = cartService.getCart(request);
        cartService.remove(cart, parseProductIdFromRequest(request));

        response.sendRedirect(request.getContextPath() + "/cart?message=Cart item removed successfully");
    }

    private Long parseProductIdFromRequest(HttpServletRequest request) {
        String productId = request.getPathInfo().substring(INDEX_FOR_ID_SUBSTRING);
        return Long.parseLong(productId);
    }
}
