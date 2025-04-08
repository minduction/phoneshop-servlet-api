package com.es.phoneshop.model;

import com.es.phoneshop.dao.ArrayListProductDao;
import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.enums.SortField;
import com.es.phoneshop.enums.SortOrder;
import com.es.phoneshop.exceptions.ProductNotFoundException;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Currency;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ArrayListProductDaoTest
{
    private ProductDao productDao;
    public static final String USD = "USD";
    private static final Currency USD_CURRENCY = Currency.getInstance(USD);
    private static final String MORE_RESULT_QUERY = "Samsung";
    private static final String LESS_RESULT_QUERY = "III";
    private static final String NO_RESULT_QUERY = "NO_RESULT_QUERY";

    @Before
    public void setup() {
        productDao = ArrayListProductDao.getInstance();
        productDao.save(new Product("sgs", "Samsung Galaxy S", new BigDecimal(100), USD_CURRENCY, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg"));
        productDao.save(new Product("sgs2", "Samsung Galaxy S II", new BigDecimal(200), USD_CURRENCY, 0, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S%20II.jpg"));
        productDao.save(new Product("sgs3", "Samsung Galaxy S III", new BigDecimal(300), USD_CURRENCY, 5, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S%20III.jpg"));
    }

    @Test
    public void testFindProductsResultNotNull() {
        assertFalse(productDao.findProducts(null, null, null).isEmpty());
    }

    @Test
    public void testSaveAndGetProduct() {
        Product testProduct = new Product("sgs", "Samsung Galaxy S", new BigDecimal(100), USD_CURRENCY, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");
        productDao.save(testProduct);

        assertEquals(testProduct, productDao.getProduct(testProduct.getId()));
    }

    @Test(expected = ProductNotFoundException.class)
    public void testDeleteProduct() {
        Product testProduct = new Product("sgs", "Samsung Galaxy S", new BigDecimal(100), USD_CURRENCY, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");
        productDao.save(testProduct);

        productDao.delete(testProduct.getId());
        productDao.getProduct(testProduct.getId());
    }

    @Test
    public void testFindProductsPriceNotNull(){
        int prevFindLength = productDao.findProducts(null, null, null).size();
        Product testProduct = new Product("sgs", "Samsung Galaxy S", null, USD_CURRENCY, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");
        productDao.save(testProduct);

        assertEquals(testProduct, productDao.getProduct(testProduct.getId()));
        assertEquals(productDao.findProducts(null, null, null).size(), prevFindLength);
    }

    @Test
    public void testFindProductsStockLevelNotZero(){
        int prevFindLength = productDao.findProducts(null, null, null).size();
        Product testProduct = new Product("sgs", "Samsung Galaxy S", new BigDecimal(100), USD_CURRENCY, 0, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");
        productDao.save(testProduct);

        assertEquals(testProduct, productDao.getProduct(testProduct.getId()));
        assertEquals(productDao.findProducts(null, null, null).size(), prevFindLength);
    }

    @Test
    public void testFindProductsWithQuery(){
        assertTrue(productDao.findProducts(LESS_RESULT_QUERY, null, null).size() <
                productDao.findProducts(MORE_RESULT_QUERY, null, null).size());
    }

    @Test
    public void testFindProductsWithBadQuery(){
        assertEquals(0, productDao.findProducts(NO_RESULT_QUERY, null, null).size());
    }

    @Test
    public void testFindProductsAscendingDescriptionSort(){
        List<Product> result = productDao.findProducts(null, SortField.DESCRIPTION, SortOrder.ASC);
        boolean isSorted = result.stream()
                .map(Product::getDescription)
                .toList()
                .equals(result.stream()
                .map(Product::getDescription)
                .sorted()
                .toList()
                );

        assertTrue(isSorted);
    }

    @Test
    public void testFindProductsDescendingDescriptionSort(){
        List<Product> result = productDao.findProducts(null, SortField.DESCRIPTION, SortOrder.DESC);
        boolean isSorted = result.stream()
                .map(Product::getDescription)
                .toList()
                .equals(result.stream()
                        .map(Product::getDescription)
                        .sorted(Comparator.reverseOrder())
                        .toList()
                );

        assertTrue(isSorted);
    }

    @Test
    public void testFindProductsDescendingPriceSort(){
        List<Product> result = productDao.findProducts(null, SortField.PRICE, SortOrder.DESC);
        boolean isSorted = result.stream()
                .map(Product::getPrice)
                .toList()
                .equals(result.stream()
                        .map(Product::getPrice)
                        .sorted(Comparator.reverseOrder())
                        .toList()
                );

        assertTrue(isSorted);
    }

    @Test
    public void testFindProductsAscendingPriceSort(){
        List<Product> result = productDao.findProducts(null, SortField.PRICE, SortOrder.ASC);
        boolean isSorted = result.stream()
                .map(Product::getPrice)
                .toList()
                .equals(result.stream()
                        .map(Product::getPrice)
                        .sorted()
                        .toList()
                );

        assertTrue(isSorted);
    }

}
