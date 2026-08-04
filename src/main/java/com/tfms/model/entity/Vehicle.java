package com.tfms.model.entity;

public class Vehicle {

    private int vehicleId;
    private String registrationNo;
    private String driverName;
    private String capacity;

    public Vehicle() {}

    public Vehicle(int vehicleId, String registrationNo,
                   String driverName, String capacity) {
        this.vehicleId = vehicleId;
        this.registrationNo = registrationNo;
        this.driverName = driverName;
        this.capacity = capacity;
    }

    public int getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(int vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getRegistrationNo() {
        return registrationNo;
    }

    public void setRegistrationNo(String registrationNo) {
        this.registrationNo = registrationNo;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getCapacity() {
        return capacity;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }
}