package com.tfms.model.entity;

public class Supplier {

    private int supplierId;
    private String name;
    private String phone;
    private String address;
    private String route;

    public Supplier() {
    }

    public Supplier(int supplierId, String name, String phone,
                    String address, String route) {
        this.supplierId = supplierId;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.route = route;
    }

    public int getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(int supplierId) {
        this.supplierId = supplierId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }
}