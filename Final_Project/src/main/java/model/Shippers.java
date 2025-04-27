/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
// Shipper.java
package model;

import java.io.InputStream;
import java.sql.Timestamp;

public class Shippers {

    private int shipperId;
    private String name;
    private String email;
    private String phone;
    private String password;
    private String cccd;
    private String driverLicense;
    private InputStream driverLicenseImage; // dùng InputStream để nhận ảnh upload
    private String address;
    private String vehicleInfo;
    private int statusId;
    private Timestamp createdAt;

    public Shippers() {
    }

    public Shippers(int shipperId, String name, String email, String phone, String password, String cccd,
            String driverLicense, InputStream driverLicenseImage, String address,
            String vehicleInfo, int statusId, Timestamp createdAt) {
        this.shipperId = shipperId;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.cccd = cccd;
        this.driverLicense = driverLicense;
        this.driverLicenseImage = driverLicenseImage;
        this.address = address;
        this.vehicleInfo = vehicleInfo;
        this.statusId = statusId;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public int getShipperId() {
        return shipperId;
    }

    public void setShipperId(int shipperId) {
        this.shipperId = shipperId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCccd() {
        return cccd;
    }

    public void setCccd(String cccd) {
        this.cccd = cccd;
    }

    public String getDriverLicense() {
        return driverLicense;
    }

    public void setDriverLicense(String driverLicense) {
        this.driverLicense = driverLicense;
    }

    public InputStream getDriverLicenseImage() {
        return driverLicenseImage;
    }

    public void setDriverLicenseImage(InputStream driverLicenseImage) {
        this.driverLicenseImage = driverLicenseImage;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getVehicleInfo() {
        return vehicleInfo;
    }

    public void setVehicleInfo(String vehicleInfo) {
        this.vehicleInfo = vehicleInfo;
    }

    public int getStatusId() {
        return statusId;
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Shippers{"
                + "shipperId=" + shipperId
                + ", name='" + name + '\''
                + ", email='" + email + '\''
                + ", phone='" + phone + '\''
                + ", address='" + address + '\''
                + ", vehicleInfo='" + vehicleInfo + '\''
                + ", statusId=" + statusId
                + ", createdAt=" + createdAt
                + '}';
    }
}
