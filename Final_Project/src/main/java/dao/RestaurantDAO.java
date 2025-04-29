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
        this.conn = super.conn;
    }

    // Thêm restaurant và trả về ID
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

    // Cập nhật thông tin nhà hàng (thường dùng khi họ muốn update profile)
    public boolean updateRestaurant(Restaurants restaurant) {
        String sql = "UPDATE Restaurants SET name = ?, phone = ?, address = ?, opening_hours = ?, cuisine_type = ?, status_id = ? WHERE restaurant_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, restaurant.getName());
            stmt.setString(2, restaurant.getPhone());
            stmt.setString(3, restaurant.getAddress());
            stmt.setString(4, restaurant.getOpeningHours());
            stmt.setString(5, restaurant.getCuisineType());
            stmt.setInt(6, restaurant.getStatusId());
            stmt.setInt(7, restaurant.getRestaurantId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Kiểm tra email hoặc phone đã tồn tại chưa
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

    // Lấy restaurant theo email
    public Restaurants getRestaurantByEmail(String email) {
        String sql = "SELECT * FROM Restaurants WHERE email = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapRestaurant(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Lấy toàn bộ danh sách restaurant
    public List<Restaurants> getAllRestaurants() {
        List<Restaurants> list = new ArrayList<>();
        String sql = "SELECT * FROM Restaurants";
        try (PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                list.add(mapRestaurant(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Xóa restaurant theo email
    public boolean deleteRestaurantByEmail(String email) {
        String sql = "DELETE FROM Restaurants WHERE email = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Map từ ResultSet sang Restaurant object
    private Restaurants mapRestaurant(ResultSet rs) throws SQLException {
        Restaurants restaurant = new Restaurants();
        restaurant.setRestaurantId(rs.getInt("restaurant_id"));
        restaurant.setName(rs.getString("name"));
        restaurant.setEmail(rs.getString("email"));
        restaurant.setPhone(rs.getString("phone"));
        restaurant.setPassword(rs.getString("password"));
        restaurant.setAddress(rs.getString("address"));
        restaurant.setOpeningHours(rs.getString("opening_hours"));
        restaurant.setCuisineType(rs.getString("cuisine_type"));
        restaurant.setStatusId(rs.getInt("status_id"));
        restaurant.setCreatedAt(rs.getTimestamp("created_at"));
        return restaurant;
    }

    // Trong RestaurantDAO.java
    public boolean updateStatus(int restaurantId, int statusId) {
        String sql = "UPDATE Restaurants SET status_id = ? WHERE restaurant_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, statusId);
            stmt.setInt(2, restaurantId);
            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

}
