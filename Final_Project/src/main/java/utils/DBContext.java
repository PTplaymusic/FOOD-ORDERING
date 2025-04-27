package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBContext {

    protected Connection conn;

    public DBContext() {
        try {
            // Tên driver kết nối (Microsoft SQL Server)
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

            // URL kết nối database
            String url = "jdbc:sqlserver://localhost:1433;databaseName=FoodShip;encrypt=false";
            String username = "sa"; // tài khoản database của bạn
            String password = "123456"; // mật khẩu database của bạn

            // Mở connection
            conn = DriverManager.getConnection(url, username, password);
            System.out.println("✅ Database connected successfully.");
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("❌ Database connection error: " + e.getMessage());
        }
    }
}
