package com.es.phoneshop.dao;

import com.es.phoneshop.comparators.ProductMatchingQueryComparator;
import com.es.phoneshop.comparators.ProductSortComparator;
import com.es.phoneshop.enums.QueryIncludeType;
import com.es.phoneshop.enums.SortField;
import com.es.phoneshop.enums.SortOrder;
import com.es.phoneshop.exceptions.ProductNotFoundException;
import com.es.phoneshop.model.Product;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

public class ArrayListProductDao extends GenericArrayListDao<Product, ProductNotFoundException> implements ProductDao {

    private static final class ProductDaoHolder {
        private static final ProductDao instance = new ArrayListProductDao();
    }

    public static ProductDao getInstance() {
        return ProductDaoHolder.instance;
    }

    private ArrayListProductDao() {
        items = new ArrayList<>();
        maxItemId = 1L;
    }

    @Override
    public Product getProduct(Long id) throws NoSuchElementException {
        return super.getItem(id);
    }

    private boolean isValidProduct(Product product) {
        return product.getPrice() != null && product.getStock() > 0;
    }

    @Override
    public List<Product> findProducts(String query, SortField sortField, SortOrder sortOrder) {
        return readWriteActionLocker.read(() ->
            items.stream()
                    .filter(this::isValidProduct)
                    .filter(product -> IsProductDescriptionSatisfyQueryAny(product, query))
                    .sorted(new ProductMatchingQueryComparator(query))
                    .sorted(new ProductSortComparator(sortField, sortOrder))
                    .collect(Collectors.toList())
        );
    }

    @Override
    public List<Product> findProducts(String queryDescription, QueryIncludeType queryIncludeType, BigDecimal minPrice, BigDecimal maxPrice) {
        return readWriteActionLocker.read(() ->
                items.stream()
                        .filter(this::isValidProduct)
                        .filter(product -> queryIncludeType == QueryIncludeType.ANY_WORD ? IsProductDescriptionSatisfyQueryAny(product, queryDescription)
                                : IsProductDescriptionSatisfyQueryAll(product, queryDescription))
                        .filter(product -> (minPrice == null || product.getPrice().compareTo(minPrice) >= 0) &&
                                (maxPrice == null || product.getPrice().compareTo(maxPrice) <= 0))
                        .collect(Collectors.toList())
        );
    }

    private boolean IsProductDescriptionSatisfyQueryAny(Product product, String query) {
        return query == null || query.isBlank() ||
                Arrays.stream(query.split(" "))
                        .filter(word -> !word.isBlank())
                        .anyMatch(word -> product.getDescription().contains(word));
    }

    private boolean IsProductDescriptionSatisfyQueryAll(Product product, String query) {
        return query == null || query.isBlank() ||
                Arrays.stream(query.split(" "))
                        .filter(word -> !word.isBlank())
                        .allMatch(word -> product.getDescription().contains(word));
    }

    @Override
    public void save(Product product) {
        super.saveItem(product);
    }

    @Override
    public void delete(Long id) {
        super.deleteItem(id);
    }

    @Override
    protected ProductNotFoundException getException(Long id) throws RuntimeException {
        return new ProductNotFoundException("Product with id = " + id + " not found");
    }
}
