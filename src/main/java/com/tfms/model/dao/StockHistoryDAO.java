package com.tfms.model.dao;

import com.tfms.db.DatabaseConnection;
import java.time.LocalDateTime;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StockHistoryDAO {

    public List<Object[]> getStockHistory() {
        List<Object[]> rows = new ArrayList<>();
        String sql = "SELECT date, i.name, s.transaction_type, s.quantity, s.balance FROM inventory i JOIN stock_history s ON s.inv_id = i.id;";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                rows.add(new Object[]{
                    rs.getDate("date"),
                    rs.getString("name"),
                    rs.getString("transaction_type"),
                    rs.getInt("quantity"),
                    rs.getInt("balance")
                });
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rows;
    }
    
    public boolean insertRecord(int invId, String transactionType, int quantity, int balance, LocalDateTime date) {
        String sql = "INSERT INTO stock_history (inv_id, transaction_type, quantity, balance, date) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, invId);
            stmt.setString(2, transactionType);
            stmt.setInt(3, quantity);
            stmt.setInt(4, balance);
            stmt.setTimestamp(5, Timestamp.valueOf(date));

            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}