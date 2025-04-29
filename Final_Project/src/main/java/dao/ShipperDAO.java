package dao;

import jakarta.servlet.http.Part;
import model.Shippers;
import utils.DBContext;

import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import utils.service.ImageUtil;

public class ShipperDAO extends DBContext {

    private Connection conn;

    public ShipperDAO() {
        super();
        this.conn = super.conn;
    }
    // Trong ShipperDAO.java

    public List<Shippers> getAllShippers() {
        List<Shippers> list = new ArrayList<>();
        String sql = "SELECT * FROM Shippers";
        try (PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Shippers shipper = new Shippers();
                shipper.setShipperId(rs.getInt("shipper_id"));
                shipper.setName(rs.getString("name"));
                shipper.setEmail(rs.getString("email"));
                shipper.setPhone(rs.getString("phone"));
                shipper.setPassword(rs.getString("password"));
                shipper.setCccd(rs.getString("cccd"));
                shipper.setDriverLicense(rs.getString("driver_license"));
                shipper.setAddress(rs.getString("address"));
                shipper.setVehicleInfo(rs.getString("vehicle_info"));
                shipper.setStatusId(rs.getInt("status_id"));
                shipper.setCreatedAt(rs.getTimestamp("created_at"));
                list.add(shipper);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public int insertShipperAndReturnId(Shippers shipper, Part licenseImagePart) {
        String sql = "INSERT INTO Shippers (name, email, phone, address, cccd, driver_license, driver_license_image, vehicle_info, password, status_id, created_at) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, GETDATE())";
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, shipper.getName());
            stmt.setString(2, shipper.getEmail());
            stmt.setString(3, shipper.getPhone());
            stmt.setString(4, shipper.getAddress());
            stmt.setString(5, shipper.getCccd());
            stmt.setString(6, shipper.getDriverLicense());

            // ✅ Convert file ảnh thành byte[] rồi lưu
            byte[] imageBytes = ImageUtil.partToBytes(licenseImagePart);
            stmt.setBytes(7, imageBytes);

            stmt.setString(8, shipper.getVehicleInfo() != null ? shipper.getVehicleInfo() : "Unknown");
            stmt.setString(9, shipper.getPassword());
            stmt.setInt(10, shipper.getStatusId());

            int affected = stmt.executeUpdate();
            if (affected > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public boolean isEmailOrPhoneExists(String email, String phone) {
        String sql = "SELECT 1 FROM Shippers WHERE email = ? OR phone = ?";
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

    public Shippers getShipperByEmail(String email) {
        String sql = "SELECT * FROM Shippers WHERE email = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapShipper(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Shippers mapShipper(ResultSet rs) throws SQLException {
        Shippers shipper = new Shippers();
        shipper.setShipperId(rs.getInt("shipper_id"));
        shipper.setName(rs.getString("name"));
        shipper.setEmail(rs.getString("email"));
        shipper.setPhone(rs.getString("phone"));
        shipper.setPassword(rs.getString("password"));
        shipper.setAddress(rs.getString("address"));
        shipper.setCccd(rs.getString("cccd"));
        shipper.setDriverLicense(rs.getString("driver_license"));
        shipper.setVehicleInfo(rs.getString("vehicle_info"));
        shipper.setStatusId(rs.getInt("status_id"));
        shipper.setCreatedAt(rs.getTimestamp("created_at"));
        return shipper;
    }

    // Trong ShipperDAO.java
    public boolean updateStatus(int shipperId, int statusId) {
        String sql = "UPDATE Shippers SET status_id = ? WHERE shipper_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, statusId);
            stmt.setInt(2, shipperId);
            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

}
