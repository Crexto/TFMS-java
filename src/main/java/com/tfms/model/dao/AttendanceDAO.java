package com.tfms.model.dao;

import com.tfms.db.DatabaseConnection;
import com.tfms.model.entity.Attendance;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.sql.*;

public class AttendanceDAO {

    public boolean attendanceInsert(Attendance att) {
        String sql = "INSERT INTO attendance (date, employee_id, status, recorded_by) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, Date.valueOf(att.getDate()));
            stmt.setInt(2, att.getEmpId()); 
            stmt.setString(3, att.getStatus());
            stmt.setInt(4, att.getRecordedBy()); 

            int rs = stmt.executeUpdate();
            return rs > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean validateAttendance(LocalDate date) {
        String sql = "SELECT id, date FROM attendance WHERE date = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, Date.valueOf(date));

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public int getAllPresent() {
        String sql = "SELECT COUNT(*) FROM attendance WHERE date = CURDATE() AND status = 'Present';";

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
    
    public List<Object[]> getAllAttendance() {
        List<Object[]> rows = new ArrayList<>();
        String sql = "SELECT a.date, a.id, e.name, e.position, a.status FROM attendance a JOIN employees e ON a.employee_id = e.id";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    rows.add(new Object[]{
                        rs.getDate("date"),
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("position"),
                        rs.getString("status")
                    });
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rows;
    }
}