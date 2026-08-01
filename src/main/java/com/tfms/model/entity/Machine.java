package com.tfms.model.entity;

public class Machine {
    private int id;
    private String machine_name;
    private String type;
    private int assigned_id;
    private String assigned_name;
    private String status;

    public Machine(int id, String mName, String type, int assigned_emp_id, String status) {
        this.id = id;
        this.machine_name = mName;
        this.type = type;
        this.assigned_id = assigned_emp_id;
        this.status = status;
    }

    public int getId() { return id; }
    public String getName() { return machine_name; }
    public String getType() { return type; }
    
    @Override
    public String toString() {
        return machine_name; 
    }
}