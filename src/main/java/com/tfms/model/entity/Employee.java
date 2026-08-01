package com.tfms.model.entity;

public class Employee {
    private int id;
    private String name;
    private String pos;

    public Employee(int id, String name, String position) {
        this.id = id;
        this.name = name;
        this.pos = position;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getPosition() { return pos; }
    
    @Override
    public String toString() {
        return name; 
    }
}