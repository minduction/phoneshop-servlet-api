package com.es.phoneshop.web;

import com.es.phoneshop.dao.ArrayListProductDao;
import com.es.phoneshop.model.PriceHistory;
import com.es.phoneshop.model.Product;
import com.es.phoneshop.dao.ProductDao;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Currency;
import java.util.List;

public class DemoDataServletContextListener implements ServletContextListener {

    private static final String USD = "USD";
    private static final Currency USD_CURRENCY = Currency.getInstance(USD);
    private ProductDao productDao;

    public DemoDataServletContextListener() {
        productDao = ArrayListProductDao.getInstance();
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContextListener.super.contextInitialized(sce);
        if (Boolean.parseBoolean(sce.getServletContext().getInitParameter("insertDemoData")))
            saveSampleProducts();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        ServletContextListener.super.contextDestroyed(sce);
    }

    private List<PriceHistory> generateRandomPriceHistory(LocalDate lastDate, BigDecimal lastPrice) {
        return List.of(new PriceHistory(lastPrice, lastDate), new PriceHistory(new BigDecimal(250), LocalDate.of(2018, 11, 10)), new PriceHistory(new BigDecimal(390), LocalDate.of(2017, 10, 8)));
    }

    private void saveSampleProducts() {
        LocalDate lastDate = LocalDate.of(2019, 12, 13);
        productDao.save(new Product("sgs", "Samsung Galaxy S", new BigDecimal(100), USD_CURRENCY, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg", generateRandomPriceHistory(lastDate, new BigDecimal(100))));
        productDao.save(new Product("sgs2", "Samsung Galaxy S II", new BigDecimal(200), USD_CURRENCY, 0, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S%20II.jpg",generateRandomPriceHistory(lastDate, new BigDecimal(200))));
        productDao.save(new Product("sgs3", "Samsung Galaxy S III", new BigDecimal(300), USD_CURRENCY, 5, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S%20III.jpg", generateRandomPriceHistory(lastDate, new BigDecimal(300))));
        productDao.save(new Product("iphone", "Apple iPhone", new BigDecimal(200), USD_CURRENCY, 10, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Apple/Apple%20iPhone.jpg", generateRandomPriceHistory(lastDate, new BigDecimal(200))));
        productDao.save(new Product("iphone6", "Apple iPhone 6", new BigDecimal(1000), USD_CURRENCY, 30, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Apple/Apple%20iPhone%206.jpg", generateRandomPriceHistory(lastDate, new BigDecimal(1000))));
        productDao.save(new Product("htces4g", "HTC EVO Shift 4G", new BigDecimal(320), USD_CURRENCY, 3, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/HTC/HTC%20EVO%20Shift%204G.jpg", generateRandomPriceHistory(lastDate, new BigDecimal(320))));
        productDao.save(new Product("sec901", "Sony Ericsson C901", new BigDecimal(420), USD_CURRENCY, 30, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Sony/Sony%20Ericsson%20C901.jpg", generateRandomPriceHistory(lastDate, new BigDecimal(420))));
        productDao.save(new Product("xperiaxz", "Sony Xperia XZ", new BigDecimal(120), USD_CURRENCY, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Sony/Sony%20Xperia%20XZ.jpg", generateRandomPriceHistory(lastDate, new BigDecimal(120))));
        productDao.save(new Product("nokia3310", "Nokia 3310", new BigDecimal(70), USD_CURRENCY, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Nokia/Nokia%203310.jpg", generateRandomPriceHistory(lastDate, new BigDecimal(70))));
        productDao.save(new Product("palmp", "Palm Pixi", new BigDecimal(170), USD_CURRENCY, 30, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Palm/Palm%20Pixi.jpg", generateRandomPriceHistory(lastDate, new BigDecimal(170))));
        productDao.save(new Product("simc56", "Siemens C56", new BigDecimal(70), USD_CURRENCY, 20, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Siemens/Siemens%20C56.jpg", generateRandomPriceHistory(lastDate, new BigDecimal(70))));
        productDao.save(new Product("simc61", "Siemens C61", new BigDecimal(80), USD_CURRENCY, 30, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Siemens/Siemens%20C61.jpg", generateRandomPriceHistory(lastDate, new BigDecimal(80))));
        productDao.save(new Product("simsxg75", "Siemens SXG75", new BigDecimal(150), USD_CURRENCY, 40, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Siemens/Siemens%20SXG75.jpg", generateRandomPriceHistory(lastDate, new BigDecimal(150))));
    }
}
