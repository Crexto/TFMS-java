package com.tfms.model.dao;

import com.tfms.db.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Date;
import java.sql.Time;


public class DowntimeDAO {

    public boolean downtimeLog(int machineId, Date date, Time startTime, Time endTime, String reason, String remarks){
        String sql = "INSERT INTO downtime (machine_id, date, start_time, end_time, reason, remarks) VALUES (?, ?, ?, ?, ?, ?);";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, machineId);
            stmt.setDate(2, date);
            stmt.setTime(3, startTime);
            stmt.setTime(4, endTime);
            stmt.setString(5, reason);
            stmt.setString(6, remarks);

            int rs = stmt.executeUpdate();
            return rs > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}