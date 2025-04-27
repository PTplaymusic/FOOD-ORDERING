package dao;

import model.Customers;
import utils.DBContext;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAO extends DBContext {

    private Connection conn;

    public CustomerDAO() {
        super();
        this.conn = super.conn; // Lấy connection đúng từ DBContext
    }

    public boolean insertCustomer(Customers customer) {
        String sql = "INSERT INTO Customers (name, email, phone, password, address, status_id, created_at) VALUES (?, ?, ?, ?, ?, ?, GETDATE())";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, customer.getName());
            stmt.setString(2, customer.getEmail());
            stmt.setString(3, customer.getPhone());
            stmt.setString(4, customer.getPassword());
            stmt.setString(5, customer.getAddress());
            stmt.setInt(6, customer.getStatusId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace(); // để hiện lỗi rõ
            System.err.println("Insert Customer Error: " + e.getMessage());
        }
        return false;
    }

    public int insertCustomerAndReturnId(Customers customer) {
        String sql = "INSERT INTO Customers (name, email, phone, password, address, status_id, created_at) VALUES (?, ?, ?, ?, ?, ?, GETDATE())";
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, customer.getName());
            stmt.setString(2, customer.getEmail());
            stmt.setString(3, customer.getPhone());
            stmt.setString(4, customer.getPassword());
            stmt.setString(5, customer.getAddress());
            stmt.setInt(6, customer.getStatusId());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt(1);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // in full lỗi cho dễ debug
            System.err.println("Insert Customer and Return ID Error: " + e.getMessage());
        }
        return -1;
    }

    public boolean updateCustomer(Customers customer) {
        String sql = "UPDATE Customers SET name = ?, phone = ?, address = ?, status_id = ? WHERE customer_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, customer.getName());
            stmt.setString(2, customer.getPhone());
            stmt.setString(3, customer.getAddress());
            stmt.setInt(4, customer.getStatusId());
            stmt.setInt(5, customer.getCustomerId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Update Customer Error: " + e.getMessage());
        }
        return false;
    }

    public boolean deleteCustomer(int customerId) {
        String sql = "DELETE FROM Customers WHERE customer_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, customerId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Delete Customer Error: " + e.getMessage());
        }
        return false;
    }

    public Customers getCustomerById(int customerId) {
        String sql = "SELECT * FROM Customers WHERE customer_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, customerId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return extractCustomer(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Get Customer By ID Error: " + e.getMessage());
        }
        return null;
    }

    public Customers getCustomerByEmail(String email) {
        String sql = "SELECT * FROM Customers WHERE email = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return extractCustomer(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Get Customer By Email Error: " + e.getMessage());
        }
        return null;
    }

    public List<Customers> getAllCustomers() {
        List<Customers> list = new ArrayList<>();
        String sql = "SELECT * FROM Customers";
        try (PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                list.add(extractCustomer(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Get All Customers Error: " + e.getMessage());
        }
        return list;
    }

    public boolean isEmailOrPhoneExists(String email, String phone) {
        String sql = "SELECT 1 FROM Customers WHERE email = ? OR phone = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            stmt.setString(2, phone);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Check Email or Phone Exists Error: " + e.getMessage());
        }
        return false;
    }

    private Customers extractCustomer(ResultSet rs) throws SQLException {
        Customers customer = new Customers();
        customer.setCustomerId(rs.getInt("customer_id"));
        customer.setName(rs.getString("name"));
        customer.setEmail(rs.getString("email"));
        customer.setPhone(rs.getString("phone"));
        customer.setPassword(rs.getString("password"));
        customer.setAddress(rs.getString("address"));
        customer.setStatusId(rs.getInt("status_id"));
        customer.setCreatedAt(rs.getTimestamp("created_at"));
        return customer;
    }
}
