package com.tfms.model.dao;

import com.tfms.model.entity.Route;
import com.tfms.db.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RouteDAO {

    private Connection conn;

    public RouteDAO() {
        try {
            conn = DatabaseConnection.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
            
        }
    }


    public List<Route> getAll() {

        List<Route> routes = new ArrayList<>();

        String sql = "SELECT * FROM routes ORDER BY route_id";

        try (
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()
        ) {

            while (rs.next()) {

                Route route = new Route();

                route.setRouteId(rs.getInt("route_id"));
                route.setRouteName(rs.getString("route_name"));

                routes.add(route);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return routes;
    }

    // ==========================
    // Insert Route
    // ==========================
    public boolean insert(Route route) {

        String sql = "INSERT INTO routes(route_name) VALUES(?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, route.getRouteName());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // ==========================
    // Update Route
    // ==========================
    public boolean update(Route route) {

        String sql = "UPDATE routes SET route_name=? WHERE route_id=?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, route.getRouteName());
            ps.setInt(2, route.getRouteId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // ==========================
    // Delete Route
    // ==========================
    public boolean delete(int id) {

        String sql = "DELETE FROM routes WHERE route_id=?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // ==========================
    // Count Routes
    // ==========================
    public int count() {

        String sql = "SELECT COUNT(*) FROM routes";

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

    // ==========================
    // Get Route By ID
    // ==========================
    public Route getById(int id) {

        String sql = "SELECT * FROM routes WHERE route_id=?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                Route route = new Route();

                route.setRouteId(rs.getInt("route_id"));
                route.setRouteName(rs.getString("route_name"));

                return route;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

}