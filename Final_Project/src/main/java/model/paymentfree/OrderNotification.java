/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.paymentfree;

import java.sql.Timestamp;

public class OrderNotification {

    private int notificationId;
    private int orderId;
    private Integer customerId;
    private Integer shipperId;
    private Integer restaurantId;
    private String message;
    private String notificationType;
    private boolean isRead;
    private Timestamp createdAt;

    public OrderNotification(int notificationId, int orderId, Integer customerId, Integer shipperId, Integer restaurantId, String message, String notificationType, boolean isRead, Timestamp createdAt) {
        this.notificationId = notificationId;
        this.orderId = orderId;
        this.customerId = customerId;
        this.shipperId = shipperId;
        this.restaurantId = restaurantId;
        this.message = message;
        this.notificationType = notificationType;
        this.isRead = isRead;
        this.createdAt = createdAt;
    }

    public OrderNotification() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    
    // Getters and Setters
    public int getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(int notificationId) {
        this.notificationId = notificationId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public Integer getShipperId() {
        return shipperId;
    }

    public void setShipperId(Integer shipperId) {
        this.shipperId = shipperId;
    }

    public Integer getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(Integer restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(String notificationType) {
        this.notificationType = notificationType;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean isRead) {
        this.isRead = isRead;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "OrderNotification{" + "notificationId=" + notificationId + ", orderId=" + orderId + ", customerId=" + customerId + ", shipperId=" + shipperId + ", restaurantId=" + restaurantId + ", message=" + message + ", notificationType=" + notificationType + ", isRead=" + isRead + ", createdAt=" + createdAt + '}';
    }
    
}
