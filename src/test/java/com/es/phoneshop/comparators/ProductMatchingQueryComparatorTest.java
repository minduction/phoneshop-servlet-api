package com.es.phoneshop.comparators;

import com.es.phoneshop.model.Product;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Currency;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ProductMatchingQueryComparatorTest {

    public static final String USD = "USD";
    private static final Currency USD_CURRENCY = Currency.getInstance(USD);

    @Test
    public void testCompareDifferentProducts(){
        Product testProduct1 = new Product("sgs", "Samsung Galaxy S", new BigDecimal(100), USD_CURRENCY, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");
        Product testProduct2 = new Product("sgs2", "Samsung Galaxy S II", new BigDecimal(200), USD_CURRENCY, 0, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S%20II.jpg");
        ProductMatchingQueryComparator comparator = new ProductMatchingQueryComparator("Samsung II");

        assertTrue(comparator.compare(testProduct1, testProduct2) > 0);
        assertTrue(comparator.compare(testProduct2, testProduct1) < 0);
    }

    @Test
    public void testCompareEqualProducts(){
        Product testProduct1 = new Product("sgs", "Samsung Galaxy S", new BigDecimal(100), USD_CURRENCY, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");
        Product testProduct2 = new Product("sgs2", "Samsung Galaxy II", new BigDecimal(200), USD_CURRENCY, 0, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S%20II.jpg");
        ProductMatchingQueryComparator comparator = new ProductMatchingQueryComparator("Samsung");

        assertEquals(0, comparator.compare(testProduct1, testProduct2));
    }
}
