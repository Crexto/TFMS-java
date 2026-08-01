package com.tfms.model.dao;

import com.tfms.db.DatabaseConnection;
import com.tfms.model.entity.Inventory;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InventoryDAO {

    public List<Inventory> getInventory() {
        List<Inventory> inventory = new ArrayList<>();
        String sql = "SELECT id, name, quantity FROM inventory";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Inventory item = new Inventory(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getInt("quantity")
                );
                inventory.add(item);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return inventory;
    }
    
    public int getTotal() {
        String sql = "SELECT SUM(quantity) FROM inventory";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }
    
    public Inventory getTea(String name) {
        String sql = "SELECT id, name, quantity FROM inventory WHERE name = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, name);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Inventory(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("quantity")
                    );
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
    
    public boolean updateQuantity(int id, int quantity) {
    String sql = "UPDATE inventory SET quantity = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, quantity);
            stmt.setInt(2, id);

            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
}
}