package com.tfms.model.dao;

import com.tfms.model.entity.QualityInspection;
import com.tfms.db.DatabaseConnection;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class QualityDAO {

    private Connection conn;

    public QualityDAO() {
        try {
            conn = DatabaseConnection.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,
                    "Database connection failed.");
        }
    }

    //=========================================
    // Insert Inspection
    //=========================================

    public boolean insert(QualityInspection q) {

        String sql = """
                INSERT INTO quality_inspections
                (collection_id, inspector_id, grade,
                 moisture, coarse_leaf, remarks,
                 status, inspection_date)
                VALUES (?,?,?,?,?,?,?,?)
                """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, q.getCollectionId());
            ps.setInt(2, q.getInspectorId());
            ps.setString(3, q.getGrade());
            ps.setDouble(4, q.getMoisture());
            ps.setDouble(5, q.getCoarseLeaf());
            ps.setString(6, q.getRemarks());
            ps.setString(7, q.getStatus());
            ps.setDate(8, q.getInspectionDate());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    //=========================================
    // All Inspections
    //=========================================

    public List<QualityInspection> getAll() {

        List<QualityInspection> list = new ArrayList<>();

        String sql = """
                SELECT qi.*,
                       s.name,
                       lc.gross_weight,
                       u.full_name
                FROM quality_inspections qi
                JOIN leaf_collection lc
                    ON qi.collection_id = lc.id
                JOIN suppliers s
                    ON lc.supplier_id = s.id
                JOIN users u
                    ON qi.inspector_id = u.id
                ORDER BY inspection_date DESC
                """;

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                QualityInspection q = new QualityInspection();

                q.setInspectionId(rs.getInt("inspection_id"));
                q.setCollectionId(rs.getInt("collection_id"));
                q.setInspectorId(rs.getInt("inspector_id"));
                q.setGrade(rs.getString("grade"));
                q.setMoisture(rs.getDouble("moisture"));
                q.setCoarseLeaf(rs.getDouble("coarse_leaf"));
                q.setRemarks(rs.getString("remarks"));
                q.setStatus(rs.getString("status"));
                q.setInspectionDate(rs.getDate("inspection_date"));

                q.setSupplierName(rs.getString("name"));
                q.setWeight(rs.getDouble("gross_weight"));
                q.setInspectorName(rs.getString("full_name"));

                list.add(q);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    //=========================================
    // Pending Collections
    //=========================================

    public List<QualityInspection> getPending() {

        List<QualityInspection> list = new ArrayList<>();

        String sql = """
                SELECT qi.*,
                       s.name,
                       lc.gross_weight
                FROM quality_inspections qi
                JOIN leaf_collection lc
                    ON qi.collection_id = lc.id
                JOIN suppliers s
                    ON lc.supplier_id = s.id
                WHERE qi.status='APPROVED'
                """;

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                QualityInspection q = new QualityInspection();

                q.setCollectionId(rs.getInt("collection_id"));
                q.setSupplierName(rs.getString("name"));
                q.setWeight(rs.getDouble("gross_weight"));
                q.setGrade(rs.getString("grade"));
                q.setStatus(rs.getString("status"));

                list.add(q);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    //=========================================
    // Reject
    //=========================================

    public boolean reject(int collectionId) {

        String sql = """
                UPDATE quality_inspections
                SET status='REJECTED'
                WHERE collection_id=?
                """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, collectionId);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    //=========================================
    // Dashboard
    //=========================================

    public int getTodayCount() {

        String sql = """
                SELECT COUNT(*)
                FROM quality_inspections
                WHERE inspection_date = CURDATE()
                """;

        return getCount(sql);
    }

    public int getApprovedCount() {

        String sql = """
                SELECT COUNT(*)
                FROM quality_inspections
                WHERE status='APPROVED'
                """;

        return getCount(sql);
    }

    public int getRejectedCount() {

        String sql = """
                SELECT COUNT(*)
                FROM quality_inspections
                WHERE status='REJECTED'
                """;

        return getCount(sql);
    }

    private int getCount(String sql) {

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next())
                return rs.getInt(1);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    //=========================================
    // Average Grade
    //=========================================

    public String getAverageGrade() {

        String sql = """
                SELECT grade,
                       COUNT(*) total
                FROM quality_inspections
                GROUP BY grade
                ORDER BY total DESC
                LIMIT 1
                """;

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next())
                return rs.getString("grade");

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return "-";
    }

}