package com.es.phoneshop.web;

import com.es.phoneshop.dao.ArrayListProductDao;

import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.enums.SortField;
import com.es.phoneshop.enums.SortOrder;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

public class ProductListPageServlet extends HttpServlet {

    private ProductDao productDao;

    @Override
    public void init() throws ServletException {
        super.init();
        productDao = ArrayListProductDao.getInstance();
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String query = request.getParameter("query");
        String sortField = request.getParameter("sort");
        String sortOrder = request.getParameter("order");
        request.setAttribute("products", productDao.findProducts(query, parseEnum(sortField, SortField.class),
                parseEnum(sortOrder, SortOrder.class)));
        request.getRequestDispatcher("/WEB-INF/pages/productList.jsp").forward(request, response);
    }

    private <T extends Enum<T>> T parseEnum(String stringValue, Class<T> enumClass) {
        return Optional.ofNullable(stringValue)
                .map(String::toUpperCase)
                .map(val -> Enum.valueOf(enumClass, val))
                .orElse(null);
    }
}
