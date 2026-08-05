package com.tfms.model.dao;

import com.tfms.model.entity.LeafPrice;
import com.tfms.db.DatabaseConnection;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LeafPriceDAO {

    private Connection conn;

    public LeafPriceDAO() {
        try {
            conn = DatabaseConnection.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,
                    "Database connection failed.");
        }
    }

    //=========================================
    // Get All Prices
    //=========================================

    public List<LeafPrice> getAll() {

        List<LeafPrice> list = new ArrayList<>();

        String sql = "SELECT * FROM leaf_prices ORDER BY start_date DESC";

        try (
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {

            while (rs.next()) {

                LeafPrice price = new LeafPrice();

                price.setPriceId(rs.getInt("id"));
                price.setPrice(rs.getDouble("price"));
                price.setStartDate(rs.getDate("start_date"));
                price.setEndDate(rs.getDate("end_date"));

                list.add(price);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    //=========================================
    // Insert New Price
    //=========================================

    public boolean insert(LeafPrice price) {

        String sql = """
                INSERT INTO leaf_prices(price,start_date,end_date)
                VALUES(?,?,?)
                """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDouble(1, price.getPrice());
            ps.setDate(2, price.getStartDate());
            ps.setDate(3, price.getEndDate());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    //=========================================
    // Update End Date
    //=========================================

    public boolean updateEndDate(Date endDate) {

        String sql = """
                UPDATE leaf_prices
                SET end_date=?
                WHERE end_date IS NULL
                """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDate(1, endDate);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    //=========================================
    // Current Price
    //=========================================
//
//    public LeafPrice getCurrentPrice() {
//
//        String sql = """
//                SELECT *
//                FROM leaf_prices
//                WHERE end_date IS NULL
//                LIMIT 1
//                """;
//
//        try (PreparedStatement ps = conn.prepareStatement(sql)) {
//
//            ResultSet rs = ps.executeQuery();
//
//            if (rs.next()) {
//
//                LeafPrice price = new LeafPrice();
//
//                price.setPriceId(rs.getInt("id"));
//                price.setPrice(rs.getDouble("price"));
//                price.setStartDate(rs.getDate("start_date"));
//                price.setEndDate(rs.getDate("end_date"));
//
//                return price;
//            }
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//        return null;
//    }

}