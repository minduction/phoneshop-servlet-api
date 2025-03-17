package com.es.phoneshop.comparators;

import com.es.phoneshop.enums.SortField;
import com.es.phoneshop.enums.SortOrder;
import com.es.phoneshop.model.Product;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Currency;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ProductSortComparatorTest {

    public static final String USD = "USD";
    private static final Currency USD_CURRENCY = Currency.getInstance(USD);

    @Test
    public void testProductSortComparatorByDescriptionAscending() {
        Product testProduct1 = new Product("sgs", "ABC", new BigDecimal(100), USD_CURRENCY, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");
        Product testProduct2 = new Product("sgs2", "CBA", new BigDecimal(200), USD_CURRENCY, 0, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S%20II.jpg");
        ProductSortComparator comparator = new ProductSortComparator(SortField.DESCRIPTION, SortOrder.ASC);

        assertTrue(comparator.compare(testProduct1, testProduct2) < 0);
        assertTrue(comparator.compare(testProduct2, testProduct1) > 0);
    }

    @Test
    public void testProductSortComparatorByDescriptionDescending() {
        Product testProduct1 = new Product("sgs", "ABC", new BigDecimal(100), USD_CURRENCY, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");
        Product testProduct2 = new Product("sgs2", "CBA", new BigDecimal(200), USD_CURRENCY, 0, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S%20II.jpg");
        ProductSortComparator comparator = new ProductSortComparator(SortField.DESCRIPTION, SortOrder.DESC);

        assertTrue(comparator.compare(testProduct1, testProduct2) > 0);
        assertTrue(comparator.compare(testProduct2, testProduct1) < 0);
    }

    @Test public void testProductSortComparatorByPriceAscending() {
        Product testProduct1 = new Product("sgs", "ABC", new BigDecimal(100), USD_CURRENCY, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");
        Product testProduct2 = new Product("sgs2", "CBA", new BigDecimal(200), USD_CURRENCY, 0, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S%20II.jpg");
        ProductSortComparator comparator = new ProductSortComparator(SortField.PRICE, SortOrder.ASC);

        assertTrue(comparator.compare(testProduct1, testProduct2) < 0);
        assertTrue(comparator.compare(testProduct2, testProduct1) > 0);
    }

    @Test public void testProductSortComparatorByPriceDescending() {
        Product testProduct1 = new Product("sgs", "ABC", new BigDecimal(100), USD_CURRENCY, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");
        Product testProduct2 = new Product("sgs2", "CBA", new BigDecimal(200), USD_CURRENCY, 0, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S%20II.jpg");
        ProductSortComparator comparator = new ProductSortComparator(SortField.PRICE, SortOrder.DESC);

        assertTrue(comparator.compare(testProduct1, testProduct2) > 0);
        assertTrue(comparator.compare(testProduct2, testProduct1) < 0);
    }

    @Test
    public void testProductSortComparatorByDescriptionEquals() {
        Product testProduct1 = new Product("sgs", "ABC", new BigDecimal(100), USD_CURRENCY, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");
        Product testProduct2 = new Product("sgs2", "ABC", new BigDecimal(200), USD_CURRENCY, 0, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S%20II.jpg");
        ProductSortComparator comparator = new ProductSortComparator(SortField.DESCRIPTION, SortOrder.ASC);

        assertEquals(0, comparator.compare(testProduct1, testProduct2));
        assertEquals(0, comparator.compare(testProduct2, testProduct1));
    }

    @Test public void testProductSortComparatorByPriceEquals() {
        Product testProduct1 = new Product("sgs", "ABC", new BigDecimal(100), USD_CURRENCY, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");
        Product testProduct2 = new Product("sgs2", "CBA", new BigDecimal(100), USD_CURRENCY, 0, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S%20II.jpg");
        ProductSortComparator comparator = new ProductSortComparator(SortField.PRICE, SortOrder.ASC);

        assertEquals(0, comparator.compare(testProduct1, testProduct2));
        assertEquals(0, comparator.compare(testProduct2, testProduct1));
    }
}
