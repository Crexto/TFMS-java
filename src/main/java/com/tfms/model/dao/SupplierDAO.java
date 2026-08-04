package com.tfms.model.dao;

import com.tfms.model.entity.Supplier;
import com.tfms.db.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SupplierDAO {

    private Connection conn;

    public SupplierDAO() {
        try {
            conn = DatabaseConnection.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
            
        }
    }

    public List<Supplier> getAll() {

        List<Supplier> suppliers = new ArrayList<>();

        String sql = "SELECT * FROM suppliers ORDER BY id";

        try (
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {

            while (rs.next()) {

                Supplier supplier = new Supplier();

                supplier.setSupplierId(rs.getInt("id"));
                supplier.setName(rs.getString("name"));
                supplier.setPhone(rs.getString("contact"));
                supplier.setAddress(rs.getString("address"));
                supplier.setRoute(rs.getString("route"));

                suppliers.add(supplier);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return suppliers;
    }


    public boolean insert(Supplier supplier) {

        String sql = """
                INSERT INTO suppliers
                (name, contact, address, route)
                VALUES (?, ?, ?, ?)
                """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, supplier.getName());
            ps.setString(2, supplier.getPhone());
            ps.setString(3, supplier.getAddress());
            ps.setString(4, supplier.getRoute());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }


    public boolean update(Supplier supplier) {

        String sql = """
                UPDATE suppliers
                SET name=?,
                    contact=?,
                    address=?,
                    route=?
                WHERE id=?
                """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, supplier.getName());
            ps.setString(2, supplier.getPhone());
            ps.setString(3, supplier.getAddress());
            ps.setString(4, supplier.getRoute());
            ps.setInt(5, supplier.getSupplierId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }


    public boolean delete(int id) {

        String sql = "DELETE FROM suppliers WHERE id=?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }


    public Supplier getById(int id) {

        String sql = "SELECT * FROM suppliers WHERE id=?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                Supplier supplier = new Supplier();

                supplier.setSupplierId(rs.getInt("id"));
                supplier.setName(rs.getString("name"));
                supplier.setPhone(rs.getString("contact"));
                supplier.setAddress(rs.getString("address"));
                supplier.setRoute(rs.getString("route"));

                return supplier;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }


    public int count() {

        String sql = "SELECT COUNT(*) FROM suppliers";

        try (
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }



    public List<Supplier> search(String keyword) {

        List<Supplier> suppliers = new ArrayList<>();

        String sql = """
                SELECT *
                FROM suppliers
                WHERE name LIKE ?
                   OR contact LIKE ?
                   OR route LIKE ?
                ORDER BY name
                """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            String search = "%" + keyword + "%";

            ps.setString(1, search);
            ps.setString(2, search);
            ps.setString(3, search);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                Supplier supplier = new Supplier();

                supplier.setSupplierId(rs.getInt("supplier_id"));
                supplier.setName(rs.getString("supplier_name"));
                supplier.setPhone(rs.getString("phone"));
                supplier.setAddress(rs.getString("address"));
                supplier.setRoute(rs.getString("route"));

                suppliers.add(supplier);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return suppliers;
    }
}