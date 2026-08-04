//package com.tfms.controller;
//
//import com.tfms.model.dao.QualityDAO;
//import com.tfms.model.entity.QualityInspection;
//import com.tfms.util.UserSession;
//
//import java.util.List;
//
//public class QualityControlController {
//    private final QualityDAO qualityDAO;
//
//    public QualityControlController() {
//        this.qualityDAO = new QualityDAO();
//    }
//
//    public List<QualityInspection> fetchAllInspections() {
//        return qualityDAO.getAllInspections();
//    }
//
//    public boolean recordInspection(String batchNumber, String moistureStr, String grade, String status, String remarks) {
//        if (batchNumber == null || batchNumber.trim().isEmpty()) {
//            throw new IllegalArgumentException("Batch number cannot be empty.");
//        }
//
//        double moisture;
//        try {
//            moisture = Double.parseDouble(moistureStr.trim());
//            if (moisture < 0 || moisture > 100) {
//                throw new IllegalArgumentException("Moisture content must be between 0 and 100%.");
//            }
//        } catch (NumberFormatException e) {
//            throw new IllegalArgumentException("Invalid numerical value for moisture content.");
//        }
//
//        int currentUserId = UserSession.getInstance().getCurrentUser().getUserId();
//        QualityInspection inspection = new QualityInspection(
//            batchNumber.trim(),
//            currentUserId,
//            moisture,
//            grade,
//            status,
//            remarks != null ? remarks.trim() : ""
//        );
//
//        return qualityDAO.addInspection(inspection);
//    }
//}