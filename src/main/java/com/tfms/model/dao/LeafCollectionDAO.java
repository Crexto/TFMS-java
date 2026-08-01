package com.tfms.model.dao;

import com.tfms.db.DatabaseConnection;
import com.tfms.model.entity.LeafCollection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LeafCollectionDAO {

    public boolean leafCollect(LeafCollection leaf) {
        String sql = "INSERT INTO leaf_collection (supplier_id, gross_weight, recorded_by) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, leaf.getSupplier());
            stmt.setInt(2, leaf.getWeight()); 
            stmt.setInt(3, leaf.getRecordedBy()); 

            int rs = stmt.executeUpdate();
            return rs > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    
    public List<Object[]> getAllReciepts() {
        List<Object[]> rows = new ArrayList<>();
        String sql = "SELECT l.collection_date, l.id, l.supplier_id, s.name AS supplier_name, l.gross_weight FROM leaf_collection l LEFT JOIN suppliers s ON l.supplier_id = s.id";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                rows.add(new Object[]{
                    rs.getDate("collection_date"),
                    rs.getInt("id"),
                    rs.getInt("supplier_id"),
                    rs.getString("supplier_name"),
                    rs.getDouble("gross_weight")
                });
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rows;
    }
    
    
    public int getLeavesToday() {
        
        String sql = "SELECT SUM(gross_weight) AS weight FROM leaf_collection WHERE DATE(collection_date) = CURDATE();";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                return rs.getInt("weight");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }
    

}