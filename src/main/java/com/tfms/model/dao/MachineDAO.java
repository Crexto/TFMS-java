package com.tfms.model.dao;

import com.tfms.db.DatabaseConnection;
import com.tfms.model.dao.EmployeeDAO;
import com.tfms.model.entity.Machine;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MachineDAO {

    public List<Object[]> getAllMachines() {
        List<Object[]> rows = new ArrayList<>();
        String sql = "SELECT m.id, m.name AS machine_name, m.type, m.assigned_emp_id, e.name AS emp_name, m.status FROM machines m JOIN employees e ON m.assigned_emp_id = e.id";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                rows.add(new Object[]{
                    rs.getInt("id"),
                    rs.getString("machine_name"),
                    rs.getString("type"),
                    new EmployeeDAO().getEmployeeId(rs.getInt("assigned_emp_id")),
                    rs.getString("status")
                });

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rows;
    }
    
    public List<Machine> getAllClassMachines() {
        List<Machine> rows = new ArrayList<>();
        String sql = "SELECT id, name, type, assigned_emp_id, status FROM machines";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                rows.add(new Machine(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("type"),
                    rs.getInt("assigned_emp_id"),
                    rs.getString("status")
                ));

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rows;
    }
    
    public boolean updateMachine(int id, int emp_id, String status) {
        String sql = "UPDATE machines SET assigned_emp_id = ?, status = ? WHERE (id = ?);";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, emp_id);
            stmt.setString(2, status); 
            stmt.setInt(3, id); 

            int rs = stmt.executeUpdate();
            return rs > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean setMachineDown(int id) {
        String sql = "UPDATE machines SET status = ? WHERE (id = ?);";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "Under Maintenance"); 
            stmt.setInt(2, id); 

            int rs = stmt.executeUpdate();
            return rs > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public int getRunningMachines() {
        String sql = "SELECT COUNT(*) FROM machines WHERE status = 'Running'";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }
    
    public int getDownMachines() {
        String sql = "SELECT COUNT(*) FROM machines WHERE status = 'Under Maintenance'";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }    
}