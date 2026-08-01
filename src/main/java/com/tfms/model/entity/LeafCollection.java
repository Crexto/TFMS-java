package com.tfms.model.entity;
import java.util.Date;

public class LeafCollection {
    private int id;
    private int supplier;
    private int weight;
    private Date collectionDate;
    private int receipt;
    private int recorded_by;
    private String supplier_name;
    
    public LeafCollection(int supplier, int gross_weight, int recorded_by) {
        this.supplier = supplier;
        this.weight = gross_weight;
        this.recorded_by = recorded_by;
    }

    public LeafCollection(int id, int supplier, String supplier_name, Date collection_date, int gross_weight, int recorded_by) {
        this(supplier,gross_weight,recorded_by);
        this.id = id;
        this.supplier_name = supplier_name;
        this.collectionDate = collection_date;

    }

    public int getRecordedBy() { return recorded_by; }
    public int getSupplier() { return supplier; }
    public int getWeight() { return weight; }
    
}