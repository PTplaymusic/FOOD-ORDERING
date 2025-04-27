package controller.logingooglel;

import dao.ShipperDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.Shippers;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.Instant;

@WebServlet(name = "ShipperCompleteProfileServlet", urlPatterns = {"/shipper-complete-profile"})
@MultipartConfig(maxFileSize = 5 * 1024 * 1024) // 5MB
public class ShipperCompleteProfileServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/Google/shipper-complete-profile.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null) {
            response.sendRedirect("login");
            return;
        }

        String email = (String) session.getAttribute("oauth_email");
        String name = (String) session.getAttribute("oauth_name");

        if (email == null || name == null) {
            response.sendRedirect("login");
            return;
        }

        String phone = request.getParameter("phone");
        String address = request.getParameter("address");
        String cccd = request.getParameter("cccd");
        String driverLicense = request.getParameter("driver_license");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirm_password");
        Part driverLicenseImage = request.getPart("driver_license_image");

        if (phone == null || address == null || cccd == null || driverLicense == null
                || password == null || confirmPassword == null || driverLicenseImage == null) {
            request.setAttribute("error", "Please fill in all fields.");
            request.getRequestDispatcher("/WEB-INF/Google/shipper-complete-profile.jsp").forward(request, response);
            return;
        }

        if (!password.equals(confirmPassword)) {
            request.setAttribute("error", "Passwords do not match.");
            request.getRequestDispatcher("/WEB-INF/Google/shipper-complete-profile.jsp").forward(request, response);
            return;
        }

        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt(12));

        Shippers shipper = new Shippers();
        shipper.setName(name);
        shipper.setEmail(email);
        shipper.setPhone(phone);
        shipper.setAddress(address);
        shipper.setCccd(cccd);
        shipper.setDriverLicense(driverLicense);
        shipper.setPassword(hashedPassword);
        shipper.setStatusId(0); // Pending Approval
        shipper.setCreatedAt(Timestamp.from(Instant.now()));

        ShipperDAO shipperDAO = new ShipperDAO();
        int shipperId = shipperDAO.insertShipperAndReturnId(shipper, driverLicenseImage);

        if (shipperId > 0) {
            session.setAttribute("role", "shipper");
            session.setAttribute("userId", shipperId);

            session.removeAttribute("oauth_email");
            session.removeAttribute("oauth_name");
            session.removeAttribute("oauth_role");

            response.sendRedirect("home");
        } else {
            request.setAttribute("error", "Failed to create account. Please try again.");
            request.getRequestDispatcher("/WEB-INF/Google/shipper-complete-profile.jsp").forward(request, response);
        }
    }
}
