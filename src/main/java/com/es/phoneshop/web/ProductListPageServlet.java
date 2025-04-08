package com.es.phoneshop.web;

import com.es.phoneshop.dao.ArrayListProductDao;

import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.enums.SortField;
import com.es.phoneshop.enums.SortOrder;
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
import java.util.Optional;

public class ProductListPageServlet extends HttpServlet {

    private ProductDao productDao;

    private CartService cartService;

    @Override
    public void init() throws ServletException {
        super.init();
        productDao = ArrayListProductDao.getInstance();
        cartService = DefaultCartService.getInstance();
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String query = request.getParameter("query");
        String sortField = request.getParameter("sort");
        String sortOrder = request.getParameter("order");
        request.setAttribute("products", productDao.findProducts(query, parseEnum(sortField, SortField.class),
                parseEnum(sortOrder, SortOrder.class)));
        request.setAttribute("cart", cartService.getCart(request));
        request.getRequestDispatcher("/WEB-INF/pages/productList.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String quantityString = request.getParameter("quantity");
        String productIdString = request.getParameter("productId");
        Long productId = Long.parseLong(productIdString);

        Integer quantity = parseQuantityFromString(quantityString, request);
        if (quantity == null || quantity <= 0) {
            request.setAttribute("error", "Invalid number value");
            request.setAttribute("errorProductId", productId);
            doGet(request, response);
            return;
        }

        Cart cart = cartService.getCart(request);
        try {
            cartService.add(cart, productId, quantity);
        } catch (OutOfStockException e) {
            request.setAttribute("error", "Out of stock, available = " + e.getQuantityAvailable());
            request.setAttribute("errorProductId", productId);
            doGet(request, response);
            return;
        }

        response.sendRedirect(getServletContext().getContextPath() + "/products" + "?message=Product added to cart successfully!");
    }

    private <T extends Enum<T>> T parseEnum(String stringValue, Class<T> enumClass) {
        return Optional.ofNullable(stringValue)
                .map(String::toUpperCase)
                .map(val -> Enum.valueOf(enumClass, val))
                .orElse(null);
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
