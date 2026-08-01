package com.tfms.model.dao;

import com.tfms.db.DatabaseConnection;
import com.tfms.model.entity.Invoice;
import com.tfms.model.entity.Inventory;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.sql.*;

public class InvoiceDAO {

    public boolean invoiceInsert(Invoice invoice) {
        String sql = "INSERT INTO dispatch_history (buyer_name, inv_id, quantity) VALUES (?, ?, ?)";
        Inventory tea = invoice.getTea();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, invoice.getBuyer());
            stmt.setInt(2, tea.getId()); 
            stmt.setInt(3, invoice.getQuantity()); 

            int rs = stmt.executeUpdate();
            return rs > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public List<Object[]> getAllInvoices() {
        List<Object[]> rows = new ArrayList<>();
        String sql = "SELECT d.id, d.buyer_name, i.name, d.quantity, d.date FROM dispatch_history d LEFT JOIN inventory i ON d.inv_id = i.id";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                rows.add(new Object[]{
                    rs.getInt("id"),
                    rs.getString("buyer_name"),
                    rs.getString("name"),
                    rs.getInt("quantity"),
                    rs.getTimestamp("date"),
                });
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rows;
    }
}