package com.tfms.model.dao;

import com.tfms.db.DatabaseConnection;
import com.tfms.model.entity.LeafCollection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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
}