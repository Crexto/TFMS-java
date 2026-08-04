package com.tfms.model.entity;

import java.sql.Timestamp;

public class QualityInspection {
    private int inspectionId;
    private String batchNumber;
    private int inspectorId;
    private Timestamp inspectionDate;
    private double moistureContent;
    private String grade;
    private String status;
    private String remarks;

    public QualityInspection() {}

    public QualityInspection(String batchNumber, int inspectorId, double moistureContent, String grade, String status, String remarks) {
        this.batchNumber = batchNumber;
        this.inspectorId = inspectorId;
        this.moistureContent = moistureContent;
        this.grade = grade;
        this.status = status;
        this.remarks = remarks;
    }

    // Getters and Setters
    public int getInspectionId() { return inspectionId; }
    public void setInspectionId(int inspectionId) { this.inspectionId = inspectionId; }

    public String getBatchNumber() { return batchNumber; }
    public void setBatchNumber(String batchNumber) { this.batchNumber = batchNumber; }

    public int getInspectorId() { return inspectorId; }
    public void setInspectorId(int inspectorId) { this.inspectorId = inspectorId; }

    public Timestamp getInspectionDate() { return inspectionDate; }
    public void setInspectionDate(Timestamp inspectionDate) { this.inspectionDate = inspectionDate; }

    public double getMoistureContent() { return moistureContent; }
    public void setMoistureContent(double moistureContent) { this.moistureContent = moistureContent; }

    public String getGrade() { return grade; }
    public void setGrade(String grade) { this.grade = grade; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }
}