package com.tfms.model.entity;
import java.time.LocalDate;

public class Inventory {
    private int id;
    private int quantity; 
    private String name;

    public Inventory(int id, String name, int quantity) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
    }
    
    public int getId() { return id; }
    public String getName() { return name; }
    public int getQuantity() { return quantity; }
}