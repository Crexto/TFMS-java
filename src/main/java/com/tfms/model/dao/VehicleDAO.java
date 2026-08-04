package com.tfms.model.dao;

import com.tfms.model.entity.Vehicle;
import com.tfms.db.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VehicleDAO {

    private Connection conn;

     public VehicleDAO() {
        try {
            conn = DatabaseConnection.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
            
        }
    }
    public List<Vehicle> getAll() {

        List<Vehicle> vehicles = new ArrayList<>();

        String sql = "SELECT * FROM vehicles ORDER BY vehicle_id";

        try (
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {

            while (rs.next()) {

                Vehicle vehicle = new Vehicle();

                vehicle.setVehicleId(rs.getInt("vehicle_id"));
                vehicle.setRegistrationNo(rs.getString("registration_no"));
                vehicle.setDriverName(rs.getString("driver_name"));
                vehicle.setCapacity(rs.getString("capacity"));

                vehicles.add(vehicle);

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return vehicles;
    }

    // =========================
    // Insert Vehicle
    // =========================
    public boolean insert(Vehicle vehicle) {

        String sql = "INSERT INTO vehicles (registration_no, driver_name, capacity) VALUES (?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, vehicle.getRegistrationNo());
            ps.setString(2, vehicle.getDriverName());
            ps.setString(3, vehicle.getCapacity());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // =========================
    // Update Vehicle
    // =========================
    public boolean update(Vehicle vehicle) {

        String sql = "UPDATE vehicles SET registration_no=?, driver_name=?, capacity=? WHERE vehicle_id=?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, vehicle.getRegistrationNo());
            ps.setString(2, vehicle.getDriverName());
            ps.setString(3, vehicle.getCapacity());
            ps.setInt(4, vehicle.getVehicleId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // =========================
    // Delete Vehicle
    // =========================
    public boolean delete(int id) {

        String sql = "DELETE FROM vehicles WHERE vehicle_id=?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // =========================
    // Count Vehicles
    // =========================
    public int count() {

        String sql = "SELECT COUNT(*) FROM vehicles";

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

    // =========================
    // Get Vehicle By ID
    // =========================
    public Vehicle getById(int id) {

        String sql = "SELECT * FROM vehicles WHERE vehicle_id=?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                Vehicle vehicle = new Vehicle();

                vehicle.setVehicleId(rs.getInt("vehicle_id"));
                vehicle.setRegistrationNo(rs.getString("registration_no"));
                vehicle.setDriverName(rs.getString("driver_name"));
                vehicle.setCapacity(rs.getString("capacity"));

                return vehicle;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}