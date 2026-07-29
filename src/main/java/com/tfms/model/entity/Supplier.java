package com.tfms.model.entity;

public class Supplier {
    private int id;
    private String username;
    private String contact;

    public Supplier(int id, String username, String contact) {
        this.id = id;
        this.username = username;
        this.contact = contact;
    }

    public int getId() { return id; }
    public String getUsername() { return username; }
    public String getContact() { return contact; }
    
    @Override
    public String toString() {
        return username; 
    }
}