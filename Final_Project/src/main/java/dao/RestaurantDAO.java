package dao;

import model.Restaurants;
import utils.DBContext;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RestaurantDAO extends DBContext {

    private Connection conn;

    public RestaurantDAO() {
        super();
        conn = this.conn; // Kế thừa connection từ DBContext
    }

    public boolean insertRestaurant(Restaurants restaurant) {
        String sql = "INSERT INTO Restaurants (name, email, phone, password, address, opening_hours, cuisine_type, status_id, created_at) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, GETDATE())";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, restaurant.getName());
            stmt.setString(2, restaurant.getEmail());
            stmt.setString(3, restaurant.getPhone());
            stmt.setString(4, restaurant.getPassword());
            stmt.setString(5, restaurant.getAddress());
            stmt.setString(6, restaurant.getOpeningHours() != null ? restaurant.getOpeningHours() : "Unknown");
            stmt.setString(7, restaurant.getCuisineType() != null ? restaurant.getCuisineType() : "Unknown");
            stmt.setInt(8, restaurant.getStatusId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public int insertRestaurantAndReturnId(Restaurants restaurant) {
        String sql = "INSERT INTO Restaurants (name, email, phone, password, address, status_id, created_at) "
                + "VALUES (?, ?, ?, ?, ?, ?, GETDATE())";

        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, restaurant.getName());
            stmt.setString(2, restaurant.getEmail());
            stmt.setString(3, restaurant.getPhone());
            stmt.setString(4, restaurant.getPassword());
            stmt.setString(5, restaurant.getAddress());
            stmt.setInt(6, restaurant.getStatusId());

            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public boolean updateRestaurant(Restaurants restaurant) {
        String sql = "UPDATE Restaurants SET name = ?, phone = ?, address = ?, opening_hours = ?, cuisine_type = ?, status_id = ? WHERE email = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, restaurant.getName());
            stmt.setString(2, restaurant.getPhone());
            stmt.setString(3, restaurant.getAddress());
            stmt.setString(4, restaurant.getOpeningHours());
            stmt.setString(5, restaurant.getCuisineType());
            stmt.setInt(6, restaurant.getStatusId());
            stmt.setString(7, restaurant.getEmail());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean deleteRestaurant(String email) {
        String sql = "DELETE FROM Restaurants WHERE email = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public List<Restaurants> getAllRestaurants() {
        List<Restaurants> list = new ArrayList<>();
        String sql = "SELECT * FROM Restaurants";

        try (PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Restaurants r = new Restaurants();
                r.setRestaurantId(rs.getInt("restaurant_id"));
                r.setName(rs.getString("name"));
                r.setEmail(rs.getString("email"));
                r.setPhone(rs.getString("phone"));
                r.setPassword(rs.getString("password"));
                r.setAddress(rs.getString("address"));
                r.setOpeningHours(rs.getString("opening_hours"));
                r.setCuisineType(rs.getString("cuisine_type"));
                r.setStatusId(rs.getInt("status_id"));
                r.setCreatedAt(rs.getTimestamp("created_at"));
                list.add(r);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public boolean isEmailOrPhoneExists(String email, String phone) {
        String sql = "SELECT 1 FROM Restaurants WHERE email = ? OR phone = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            stmt.setString(2, phone);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Restaurants getRestaurantByEmail(String email) {
        String sql = "SELECT * FROM Restaurants WHERE email = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Restaurants restaurant = new Restaurants();
                restaurant.setRestaurantId(rs.getInt("restaurant_id"));
                restaurant.setName(rs.getString("name"));
                restaurant.setEmail(rs.getString("email"));
                restaurant.setPhone(rs.getString("phone"));
                restaurant.setPassword(rs.getString("password"));
                restaurant.setAddress(rs.getString("address"));
                restaurant.setStatusId(rs.getInt("status_id"));
                restaurant.setCreatedAt(rs.getTimestamp("created_at"));
                return restaurant;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
