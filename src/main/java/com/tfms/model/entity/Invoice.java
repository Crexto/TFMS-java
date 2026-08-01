package com.tfms.model.entity;
import java.time.LocalDate;


public class Invoice {
    private String buyer_name;
    private int quantity; 
    private Inventory tea;

    public Invoice(String buyer_name, Inventory tea, int quantity) {
        this.buyer_name = buyer_name;
        this.tea = tea;
        this.quantity = quantity;
    }
    
    public Inventory getTea() { return tea; }
    public String getBuyer() { return buyer_name; }
    public int getQuantity() { return quantity; }
}