package com.es.phoneshop.dao;

import com.es.phoneshop.enums.QueryIncludeType;
import com.es.phoneshop.model.Product;
import com.es.phoneshop.enums.SortField;
import com.es.phoneshop.enums.SortOrder;

import java.math.BigDecimal;
import java.util.List;

public interface ProductDao {
    Product getProduct(Long id);
    List<Product> findProducts(String query, SortField sortField, SortOrder sortOrder);
    List<Product> findProducts(String queryDescription, QueryIncludeType queryIncludeType, BigDecimal minPrice, BigDecimal maxPrice);
    void save(Product product);
    void delete(Long id);
}
