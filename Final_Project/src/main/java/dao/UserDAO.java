package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import utils.DBContext;

public class UserDAO extends DBContext {

    private Connection conn;

    public UserDAO() {
        super();
        this.conn = super.conn; // ✅ lấy connection từ DBContext
    }

    // Lấy status_id dựa trên role và userId
    public int getStatusId(String role, int userId) {
        String table = getTable(role);
        String idColumn = getIdColumn(role);

        if (table == null || idColumn == null) {
            System.err.println("Invalid role provided: " + role);
            return -1;
        }

        String sql = "SELECT status_id FROM " + table + " WHERE " + idColumn + " = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("status_id");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching status_id: " + e.getMessage());
        }
        return -1;
    }

    // Update status_id (ví dụ activate tài khoản)
    public boolean updateStatus(String role, int userId, int statusId) {
        String table = getTable(role);
        String idColumn = getIdColumn(role);

        if (table == null || idColumn == null) {
            System.err.println("Invalid role provided for update: " + role);
            return false;
        }

        String sql = "UPDATE " + table + " SET status_id = ? WHERE " + idColumn + " = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, statusId);
            stmt.setInt(2, userId);
            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            System.err.println("Error updating status: " + e.getMessage());
        }
        return false;
    }

    // Helper: Lấy bảng theo role
    private String getTable(String role) {
        switch (role.toLowerCase()) {
            case "customer":
                return "Customers";
            case "shipper":
                return "Shippers";
            case "restaurant":
                return "Restaurants";
            default:
                return null;
        }
    }

    // Helper: Lấy tên cột ID theo role
    private String getIdColumn(String role) {
        switch (role.toLowerCase()) {
            case "customer":
                return "customer_id";
            case "shipper":
                return "shipper_id";
            case "restaurant":
                return "restaurant_id";
            default:
                return null;
        }
    }
}
