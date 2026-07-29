package com.tfms.model.entity;
import java.util.Date;

public class LeafCollection {
    private int id;
    private int supplier;
    private int weight;
    private Date collectionDate;
    private int receipt;
    private int recorded_by;
    
    public LeafCollection(int supplier, int gross_weight, int receipt_no, int recorded_by) {
        this.supplier = supplier;
        this.weight = gross_weight;
        this.receipt = receipt_no;
        this.recorded_by = recorded_by;
    }

    public LeafCollection(int id, int supplier, Date collection_date, int gross_weight, int receipt_no, int recorded_by) {
        this(supplier,gross_weight,receipt_no,recorded_by);
        this.id = id;
        this.collectionDate = collection_date;

    }

    public int getId() { return id; }
    public int getSupplier() { return supplier; }
    public int getWeight() { return weight; }
    
}