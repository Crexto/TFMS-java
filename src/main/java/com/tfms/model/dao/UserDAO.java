package com.tfms.model.dao;

import com.tfms.db.DatabaseConnection;
import com.tfms.model.entity.User;
import java.sql.*;

public class UserDAO {

    public User validate(String username, String password) {
        String sql = "SELECT id, username, role FROM users WHERE username = ? AND password_hash = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, password); 

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new User(
                    rs.getInt("id"),
                    rs.getString("username"),
                    rs.getInt("role")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}