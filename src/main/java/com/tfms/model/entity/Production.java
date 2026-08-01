package com.tfms.model.entity;
import java.util.Date;


public class Production {
    private Date date;
    private int quantity; 
    private Inventory tea;
    private String remarks;

    public Production(Date date, Inventory tea, int quantity, String remarks) {
        this.date = date;
        this.tea = tea;
        this.quantity = quantity;
        this.remarks = remarks;
    }
    
    public Inventory getTea() { return tea; }
    public Date getDate() { return date; }
    public int getQuantity() { return quantity; }
    public String getRemarks() { return remarks; }
}