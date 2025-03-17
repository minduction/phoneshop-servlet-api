package com.es.phoneshop.dao;

import com.es.phoneshop.model.Product;
import com.es.phoneshop.comparators.ProductMatchingQueryComparator;
import com.es.phoneshop.comparators.ProductSortComparator;
import com.es.phoneshop.locker.ReadWriteActionLocker;
import com.es.phoneshop.enums.SortField;
import com.es.phoneshop.enums.SortOrder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

public class ArrayListProductDao implements ProductDao {

    private static ProductDao instance;

    public static synchronized ProductDao getInstance() {
        if (instance == null) {
            instance = new ArrayListProductDao();
        }
        return instance;
    }

    private long maxProductId;
    private final List<Product> products;
    private final ReadWriteActionLocker readWriteActionLocker = new ReadWriteActionLocker();

    private ArrayListProductDao() {
        products = new ArrayList<>();
        maxProductId = 1L;
    }

    @Override
    public Product getProduct(Long id) throws NoSuchElementException {
        return readWriteActionLocker.read(() ->
            products.stream()
                .filter(product -> id.equals(product.getId()))
                .findAny()
                .get()
        );
    }

    private boolean isValidProduct(Product product) {
        return product.getPrice() != null && product.getStock() > 0;
    }

    @Override
    public List<Product> findProducts(String query, SortField sortField, SortOrder sortOrder) {
        return readWriteActionLocker.read(() ->
            products.stream()
                    .filter(this::isValidProduct)
                    .filter(product -> IsProductDescriptionSatisfyQuery(product, query))
                    .sorted(new ProductMatchingQueryComparator(query))
                    .sorted(new ProductSortComparator(sortField, sortOrder))
                    .collect(Collectors.toList())
        );
    }

    private boolean IsProductDescriptionSatisfyQuery(Product product, String query) {
        return query == null || query.isBlank() ||
                Arrays.stream(query.split(" "))
                        .filter(w -> !w.isBlank())
                        .anyMatch(w -> product.getDescription().contains(w));
    }

    @Override
    public void save(Product product) {
        readWriteActionLocker.write(() -> {
            if (product.getId() == null) {
                product.setId(maxProductId++);
                products.add(product);
            } else {
                products.stream()
                        .filter(pr -> product.getId().equals(pr.getId()))
                        .findAny()
                        .ifPresentOrElse(val -> products.set(products.indexOf(val), product), () -> products.add(product));
            }
        });
    }

    @Override
    public void delete(Long id) {
        readWriteActionLocker.write(() ->
            products.removeIf(product -> id.equals(product.getId()))
        );
    }

}
