package com.es.phoneshop.web;

import com.es.phoneshop.dao.ArrayListProductDao;
import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.enums.QueryIncludeType;
import com.es.phoneshop.model.cart.CartService;
import com.es.phoneshop.model.cart.DefaultCartService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class AdvancedSearchPageServlet extends HttpServlet {

    private static final String ADVANCED_SEARCH_PAGE = "/WEB-INF/pages/advancedSearch.jsp";
    private static final String DESCRIPTION_PARAMETER = "description";
    private static final String MIN_PRICE_PARAMETER = "minPrice";
    private static final String MAX_PRICE_PARAMETER = "maxPrice";

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
        Map<String, String> errors = new HashMap<>();
        String queryDescription = request.getParameter(DESCRIPTION_PARAMETER);
        if (queryDescription != null) {
            QueryIncludeType queryIncludeType = parseEnum(request.getParameter("queryIncludeType"), QueryIncludeType.class);

            BigDecimal minPrice = parsePriceFromRequest(MIN_PRICE_PARAMETER, request, errors);
            BigDecimal maxPrice = parsePriceFromRequest(MAX_PRICE_PARAMETER, request, errors);

            if (errors.isEmpty()){
                request.setAttribute("products", productDao.findProducts(queryDescription, queryIncludeType, minPrice, maxPrice));
            }
            request.setAttribute("errors", errors);
            request.setAttribute("cart", cartService.getCart(request));
        }
        request.getRequestDispatcher(ADVANCED_SEARCH_PAGE).forward(request, response);
    }

    private <T extends Enum<T>> T parseEnum(String stringValue, Class<T> enumClass) {
        return Optional.ofNullable(stringValue)
                .map(String::toUpperCase)
                .map(val -> val.replace(" ", "_"))
                .map(val -> Enum.valueOf(enumClass, val))
                .orElse(null);
    }

    private BigDecimal parsePriceFromRequest(String parameter, HttpServletRequest request, Map<String, String> errors){
        String price = request.getParameter(parameter);
        if (price.isEmpty()) {
            return null;
        }
        NumberFormat numberFormat = NumberFormat.getInstance(request.getLocale());
        try {
            return BigDecimal.valueOf(numberFormat.parse(price).doubleValue());
        }
        catch (ParseException e){
            errors.put(parameter, "Not a number");
            return null;
        }
    }
}
