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

    public LeafCollection() {
    }

    public LeafCollection(int supplier, int gross_weight, int recorded_by) {
        this.supplier = supplier;
        this.weight = gross_weight;
        this.recorded_by = recorded_by;
    }

    public LeafCollection(int id, int supplier, String supplier_name,
                          Date collection_date, int gross_weight, int recorded_by) {

        this(supplier, gross_weight, recorded_by);

        this.id = id;
        this.supplier_name = supplier_name;
        this.collectionDate = collection_date;
    }


    public int getId() {
        return id;
    }

    public int getCollectionId() {
        return id;
    }

    public int getSupplier() {
        return supplier;
    }

    public int getSupplierId() {
        return supplier;
    }

    public String getSupplierName() {
        return supplier_name;
    }

    public int getWeight() {
        return weight;
    }

    public double getGrossWeight() {
        return weight;
    }

    public Date getCollectionDate() {
        return collectionDate;
    }

    public int getReceipt() {
        return receipt;
    }

    public int getRecordedBy() {
        return recorded_by;
    }


    public void setId(int id) {
        this.id = id;
    }

    public void setSupplier(int supplier) {
        this.supplier = supplier;
    }

    public void setSupplierName(String supplier_name) {
        this.supplier_name = supplier_name;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public void setCollectionDate(Date collectionDate) {
        this.collectionDate = collectionDate;
    }

    public void setReceipt(int receipt) {
        this.receipt = receipt;
    }

    public void setRecordedBy(int recorded_by) {
        this.recorded_by = recorded_by;
    }
}