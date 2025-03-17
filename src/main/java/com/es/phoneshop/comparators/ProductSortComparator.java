package com.es.phoneshop.comparators;

import com.es.phoneshop.enums.SortField;
import com.es.phoneshop.enums.SortOrder;
import com.es.phoneshop.model.Product;

import java.util.Comparator;

public class ProductSortComparator implements Comparator<Product> {

    SortField sortField;
    SortOrder sortOrder;

    private static final int NO_SORT_FIELD = 0;

    public ProductSortComparator(SortField sortField, SortOrder sortOrder) {
        this.sortField = sortField;
        this.sortOrder = sortOrder;
    }

    @Override
    public int compare(Product o1, Product o2) {
        int res = NO_SORT_FIELD;

        if (sortField != null) {
            res = SortField.PRICE == sortField ?  o1.getPrice().compareTo(o2.getPrice()) : o1.getDescription().compareTo(o2.getDescription());
        }
        res = SortOrder.DESC == sortOrder ? -res : res;
        return res;
    }
}
