package com.es.phoneshop.comparators;

import com.es.phoneshop.model.Product;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Optional;

public class ProductMatchingQueryComparator implements Comparator<Product> {

    private final String[] queryWords;
    public ProductMatchingQueryComparator(String query) {
        this.queryWords = Optional.ofNullable(query)
                .filter(s -> !s.isBlank())
                .map(str -> str.split(" "))
                .map(strings -> Arrays.stream(strings)
                        .filter(word -> !word.isBlank())
                        .toArray(String[]::new))
                .orElse(null);
    }

    @Override
    public int compare(Product o1, Product o2) {
        if (queryWords == null || queryWords.length == 0) {
            return 0;
        }
        return Integer.compare(Arrays.stream(queryWords)
                        .filter(word -> o2.getDescription().contains(word))
                        .toList()
                        .size() * 100 / Arrays.stream(o2.getDescription().split(" "))
                        .filter(word -> !word.isBlank())
                        .toList()
                        .size(),
                Arrays.stream(queryWords)
                        .filter(word -> o1.getDescription().contains(word))
                        .toList()
                        .size() * 100 / Arrays.stream(o1.getDescription().split(" "))
                        .filter(word -> !word.isBlank())
                        .toList()
                        .size());
    }
}
