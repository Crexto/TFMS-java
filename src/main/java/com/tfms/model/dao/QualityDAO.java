package com.tfms.model.dao;

import com.tfms.db.DatabaseConnection;
import com.tfms.model.entity.QualityInspection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class QualityDAO {

    public boolean addInspection(QualityInspection qi) {
        String sql = "INSERT INTO quality_inspection (batch_number, inspector_id, moisture_content, grade, status, remarks) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, qi.getBatchNumber());
            stmt.setInt(2, qi.getInspectorId());
            stmt.setDouble(3, qi.getMoistureContent());
            stmt.setString(4, qi.getGrade());
            stmt.setString(5, qi.getStatus());
            stmt.setString(6, qi.getRemarks());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<QualityInspection> getAllInspections() {
        List<QualityInspection> list = new ArrayList<>();
        String sql = "SELECT * FROM quality_inspection ORDER BY inspection_date DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                QualityInspection qi = new QualityInspection();
                qi.setInspectionId(rs.getInt("inspection_id"));
                qi.setBatchNumber(rs.getString("batch_number"));
                qi.setInspectorId(rs.getInt("inspector_id"));
                qi.setInspectionDate(rs.getTimestamp("inspection_date"));
                qi.setMoistureContent(rs.getDouble("moisture_content"));
                qi.setGrade(rs.getString("grade"));
                qi.setStatus(rs.getString("status"));
                qi.setRemarks(rs.getString("remarks"));
                list.add(qi);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}