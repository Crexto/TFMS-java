package com.tfms.model.dao;

import com.tfms.db.DatabaseConnection;
import com.tfms.model.entity.Production;
import com.tfms.model.entity.Inventory;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.sql.*;

public class ProductionDAO {

    public boolean productionInsert(Production prod) {
        String sql = "INSERT INTO production (remarks, inv_id, date, quantity) VALUES (?, ?, ?, ?)";
        Inventory tea = prod.getTea();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, prod.getRemarks());
            stmt.setInt(2, tea.getId());             
            stmt.setDate(3, new Date(prod.getDate().getTime())); 
            stmt.setInt(4, prod.getQuantity()); 

            int rs = stmt.executeUpdate();
            return rs > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public int getProductionToday() {
        String sql = "SELECT SUM(quantity) FROM production WHERE date = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDate(1, Date.valueOf(LocalDate.now()));

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }
    
    public List<Object[]> getAllProduction() {
        List<Object[]> rows = new ArrayList<>();
        String sql = "SELECT p.id, p.date, i.name, p.quantity, p.remarks FROM production p LEFT JOIN inventory i ON p.inv_id = i.id";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                rows.add(new Object[]{
                    rs.getInt("id"),
                    rs.getDate("date"),
                    rs.getString("name"),
                    rs.getInt("quantity"),
                    rs.getString("remarks"),
                });
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rows;
    }
}