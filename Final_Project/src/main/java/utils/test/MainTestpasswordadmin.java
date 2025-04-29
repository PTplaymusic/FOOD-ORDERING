package utils.test;

import java.sql.*;
import org.mindrot.jbcrypt.BCrypt;

public class MainTestpasswordadmin {

    public static void main(String[] args) {
        String dbURL = "jdbc:sqlserver://localhost:1433;databaseName=FoodShip;encrypt=false";
        String username = "sa";  // đổi user SQL Server của em vào
        String password = "123456";  // đổi password SQL Server vào

        Connection conn = null;

        try {
            // Kết nối Database
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            conn = DriverManager.getConnection(dbURL, username, password);
            System.out.println("✅ Database Connected!");

            // Test đọc password từ bảng Admin
            String sql = "SELECT password FROM SystemAdmins WHERE email = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, "admin@foodship.com"); // email em muốn test
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String hashedPassword = rs.getString("password");
                System.out.println("📦 Password Hash trong DB: " + hashedPassword);

                // Bây giờ test kiểm tra nhập password thực
                String plainPassword = "admin1";  // Mật khẩu gốc em nghĩ đúng
                boolean matched = BCrypt.checkpw(plainPassword, hashedPassword);

                if (matched) {
                    System.out.println("✅ Password nhập vào CHÍNH XÁC!");
                } else {
                    System.out.println("❌ Password nhập vào KHÔNG KHỚP!");
                }
            } else {
                System.out.println("❌ Không tìm thấy tài khoản Admin này!");
            }

            rs.close();
            stmt.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
