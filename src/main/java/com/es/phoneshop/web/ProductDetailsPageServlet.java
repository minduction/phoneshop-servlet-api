package com.es.phoneshop.web;

import com.es.phoneshop.dao.ArrayListProductDao;
import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.exceptions.OutOfStockException;
import com.es.phoneshop.model.Cart;
import com.es.phoneshop.model.CartService;
import com.es.phoneshop.model.DefaultCartService;
import com.es.phoneshop.model.Product;
import com.es.phoneshop.utils.LimitedList;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;

public class ProductDetailsPageServlet extends HttpServlet {
    private static final String RECENTLY_VISITED_SESSION_ATTRIBUTE = "recentlyVisitedProducts";
    private static final int RECENTLY_VISITED_COUNT = 3;

    private ProductDao productDao;
    private CartService cartService;
    private static final int INDEX_FOR_ID_SUBSTRING = 1;

    @Override
    public void init() throws ServletException {
        super.init();
        productDao = ArrayListProductDao.getInstance();
        cartService = DefaultCartService.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Product productInfo = productDao.getProduct(parseProductIdFromRequest(request));
        LimitedList<Product> recentlyVisited = (LimitedList<Product>) request.getSession().getAttribute(RECENTLY_VISITED_SESSION_ATTRIBUTE);
        if (recentlyVisited == null) {
            recentlyVisited = new LimitedList<Product>(RECENTLY_VISITED_COUNT);
            recentlyVisited.setReturnReversed(true);
            request.getSession().setAttribute(RECENTLY_VISITED_SESSION_ATTRIBUTE, recentlyVisited);
        }
        if (!recentlyVisited.getItems().contains(productInfo)){
            recentlyVisited.add(productInfo);
        }
        else {
            recentlyVisited.remove(productInfo);
            recentlyVisited.add(productInfo);
        }
        request.setAttribute("product", productInfo);
        request.setAttribute("cart", cartService.getCart(request));

        request.getRequestDispatcher("/WEB-INF/pages/product.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String quantityString = request.getParameter("quantity");
        Long productId = parseProductIdFromRequest(request);

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
        response.sendRedirect(getServletContext().getContextPath() + "/products/" + productId + "?message=Product added to cart successfully!");
    }

    private Long parseProductIdFromRequest(HttpServletRequest request) {
        String productId = request.getPathInfo().substring(INDEX_FOR_ID_SUBSTRING);
        return Long.parseLong(productId);
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
