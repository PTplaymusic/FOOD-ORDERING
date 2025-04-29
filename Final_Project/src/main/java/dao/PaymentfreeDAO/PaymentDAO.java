/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao.PaymentfreeDAO;

import model.paymentfree.ShipperFee;
import model.paymentfree.RestaurantFee;
import model.paymentfree.OrderNotification;
import utils.DBContext;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PaymentDAO extends DBContext {

    // SHIPPER FEES
    public boolean insertShipperFee(ShipperFee fee) {
        String sql = "INSERT INTO ShipperFees (shipper_id, order_id, fee_amount, fee_type, created_at) VALUES (?, ?, ?, ?, GETDATE())";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, fee.getShipperId());
            stmt.setInt(2, fee.getOrderId());
            stmt.setDouble(3, fee.getFeeAmount());
            stmt.setString(4, fee.getFeeType());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<ShipperFee> getAllShipperFees() {
        List<ShipperFee> list = new ArrayList<>();
        String sql = "SELECT * FROM ShipperFees ORDER BY created_at DESC";
        try (PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                ShipperFee fee = new ShipperFee();
                fee.setFeeId(rs.getInt("fee_id"));
                fee.setShipperId(rs.getInt("shipper_id"));
                fee.setOrderId(rs.getInt("order_id"));
                fee.setFeeAmount(rs.getDouble("fee_amount"));
                fee.setFeeType(rs.getString("fee_type"));
                fee.setCreatedAt(rs.getTimestamp("created_at"));
                list.add(fee);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // RESTAURANT FEES
    public boolean insertRestaurantFee(RestaurantFee fee) {
        String sql = "INSERT INTO RestaurantFees (restaurant_id, order_id, fee_amount, fee_type, created_at) VALUES (?, ?, ?, ?, GETDATE())";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, fee.getRestaurantId());
            stmt.setInt(2, fee.getOrderId());
            stmt.setDouble(3, fee.getFeeAmount());
            stmt.setString(4, fee.getFeeType());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<RestaurantFee> getAllRestaurantFees() {
        List<RestaurantFee> list = new ArrayList<>();
        String sql = "SELECT * FROM RestaurantFees ORDER BY created_at DESC";
        try (PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                RestaurantFee fee = new RestaurantFee();
                fee.setFeeId(rs.getInt("fee_id"));
                fee.setRestaurantId(rs.getInt("restaurant_id"));
                fee.setOrderId(rs.getInt("order_id"));
                fee.setFeeAmount(rs.getDouble("fee_amount"));
                fee.setFeeType(rs.getString("fee_type"));
                fee.setCreatedAt(rs.getTimestamp("created_at"));
                list.add(fee);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // ORDER NOTIFICATIONS
    public boolean insertOrderNotification(OrderNotification notification) {
        String sql = "INSERT INTO OrderNotifications (order_id, user_id, message, notification_type, is_read, created_at) VALUES (?, ?, ?, ?, 0, GETDATE())";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, notification.getOrderId());
            stmt.setInt(2, notification.getCustomerId()); // Có thể là customerId hoặc restaurantId tùy đối tượng
            stmt.setString(3, notification.getMessage());
            stmt.setString(4, notification.getNotificationType());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<OrderNotification> getNotificationsByUserId(int userId) {
        List<OrderNotification> list = new ArrayList<>();
        String sql = "SELECT * FROM OrderNotifications WHERE user_id = ? ORDER BY created_at DESC";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                OrderNotification noti = new OrderNotification();
                noti.setNotificationId(rs.getInt("notification_id"));
                noti.setOrderId(rs.getInt("order_id"));
                noti.setCustomerId(rs.getInt("user_id"));
                noti.setMessage(rs.getString("message"));
                noti.setNotificationType(rs.getString("notification_type"));
                noti.setRead(rs.getBoolean("is_read"));
                noti.setCreatedAt(rs.getTimestamp("created_at"));
                list.add(noti);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean markNotificationAsRead(int notificationId) {
        String sql = "UPDATE OrderNotifications SET is_read = 1 WHERE notification_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, notificationId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Double getTotalPaymentForShipper(int shipperId) {
        String sql = "SELECT SUM(fee_amount) AS total FROM ShipperFees WHERE shipper_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, shipperId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    public Double getTotalPaymentForRestaurant(int restaurantId) {
        String sql = "SELECT SUM(fee_amount) AS total FROM RestaurantFees WHERE restaurant_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, restaurantId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }

}
