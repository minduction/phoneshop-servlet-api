package com.es.phoneshop.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class PriceHistory {

    private BigDecimal price;
    private LocalDate date;

    public PriceHistory(BigDecimal price, LocalDate date) {
        this.price = price;
        this.date = date;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
