package com.es.phoneshop.model.product;

import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.NoSuchElementException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertEquals;

public class ArrayListProductDaoTest
{
    private ProductDao productDao;
    public static final String USD = "USD";
    private static final Currency USD_CURRENCY = Currency.getInstance(USD);

    @Before
    public void setup() {
        productDao = new ArrayListProductDao();
    }

    @Test
    public void testFindProducts() {
        assertFalse(productDao.findProducts().isEmpty());
    }

    @Test
    public void testSaveAndGetProduct() {
        Product testProduct = new Product("sgs", "Samsung Galaxy S", new BigDecimal(100), USD_CURRENCY, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");
        productDao.save(testProduct);

        assertEquals(testProduct, productDao.getProduct(testProduct.getId()));
    }

    @Test(expected = NoSuchElementException.class)
    public void testDeleteProduct() {
        Product testProduct = new Product("sgs", "Samsung Galaxy S", new BigDecimal(100), USD_CURRENCY, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");
        productDao.save(testProduct);

        productDao.delete(testProduct.getId());
        productDao.getProduct(testProduct.getId());
    }

    @Test
    public void testFindProductsPriceNotNull(){
        int prevFindLength = productDao.findProducts().size();
        Product testProduct = new Product("sgs", "Samsung Galaxy S", null, USD_CURRENCY, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");
        productDao.save(testProduct);

        assertEquals(testProduct, productDao.getProduct(testProduct.getId()));
        assertEquals(productDao.findProducts().size(), prevFindLength);
    }

    @Test
    public void testFindProductsStockLevelNotZero(){
        int prevFindLength = productDao.findProducts().size();
        Product testProduct = new Product("sgs", "Samsung Galaxy S", new BigDecimal(100), USD_CURRENCY, 0, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");
        productDao.save(testProduct);

        assertEquals(testProduct, productDao.getProduct(testProduct.getId()));
        assertEquals(productDao.findProducts().size(), prevFindLength);
    }
}
