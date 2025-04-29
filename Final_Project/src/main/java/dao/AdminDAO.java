/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import model.Admins;
import utils.DBContext;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdminDAO extends DBContext {

    public boolean insertAdmin(Admins admin) {
        String sql = "INSERT INTO SystemAdmins (name, email, password, created_at) VALUES (?, ?, ?, GETDATE())";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, admin.getName());
            stmt.setString(2, admin.getEmail());
            stmt.setString(3, admin.getPassword());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateAdmin(Admins admin) {
        String sql = "UPDATE SystemAdmins SET name = ?, password = ? WHERE email = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, admin.getName());
            stmt.setString(2, admin.getPassword());
            stmt.setString(3, admin.getEmail());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteAdminByEmail(String email) {
        String sql = "DELETE FROM SystemAdmins WHERE email = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Admins> getAllAdmins() {
        List<Admins> list = new ArrayList<>();
        String sql = "SELECT * FROM SystemAdmins";
        try (PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Admins admin = new Admins();
                admin.setAdminId(rs.getInt("admin_id"));
                admin.setName(rs.getString("name"));
                admin.setEmail(rs.getString("email"));
                admin.setPassword(rs.getString("password"));
                admin.setCreatedAt(rs.getTimestamp("created_at"));
                list.add(admin);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public Admins getAdminByEmail(String email) {
        String sql = "SELECT * FROM SystemAdmins WHERE email = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Admins admin = new Admins();
                admin.setAdminId(rs.getInt("admin_id"));
                admin.setName(rs.getString("name"));
                admin.setEmail(rs.getString("email"));
                admin.setPassword(rs.getString("password"));
                admin.setCreatedAt(rs.getTimestamp("created_at"));
                return admin;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
