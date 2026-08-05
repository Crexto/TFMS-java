package com.tfms.model.entity;

import java.sql.Date;

public class LeafPrice {

    private int priceId;
    private double price;
    private Date startDate;
    private Date endDate;

    public LeafPrice() {
    }

    public LeafPrice(int priceId, double price, Date startDate, Date endDate) {
        this.priceId = priceId;
        this.price = price;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public int getPriceId() {
        return priceId;
    }

    public void setPriceId(int priceId) {
        this.priceId = priceId;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}