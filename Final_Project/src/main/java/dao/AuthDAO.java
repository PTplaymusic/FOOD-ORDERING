package dao;

import utils.DBContext;
import java.sql.*;
import org.mindrot.jbcrypt.BCrypt;

public class AuthDAO extends DBContext {

    private Connection conn;

    public AuthDAO() {
        super();
        this.conn = super.conn;  // ✅ Lấy conn từ DBContext
    }

    // Đăng nhập Admin
    public int authenticateAdmin(String email, String password) {
        String sql = "SELECT admin_id, password FROM SystemAdmins WHERE email = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String hashedPassword = rs.getString("password");
                    if (BCrypt.checkpw(password, hashedPassword)) {
                        return rs.getInt("admin_id");
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Authenticate Admin Error: " + e.getMessage());
        }
        return -1;
    }

    // Đăng nhập Customer/Shipper/Restaurant
    public int authenticate(String email, String password, String role) {
        String table = getTable(role);
        String idColumn = getIdColumn(role);
        if (table.isEmpty() || idColumn.isEmpty()) {
            return -1;
        }

        String sql = "SELECT " + idColumn + ", password FROM " + table + " WHERE email = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String hashedPassword = rs.getString("password");
                    if (BCrypt.checkpw(password, hashedPassword)) {
                        return rs.getInt(idColumn);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Authenticate User Error: " + e.getMessage());
        }
        return -1;
    }

    private String getTable(String role) {
        switch (role) {
            case "customer":
                return "Customers";
            case "shipper":
                return "Shippers";
            case "restaurant":
                return "Restaurants";
            default:
                return "";
        }
    }

    private String getIdColumn(String role) {
        switch (role) {
            case "customer":
                return "customer_id";
            case "shipper":
                return "shipper_id";
            case "restaurant":
                return "restaurant_id";
            default:
                return "";
        }
    }
}
