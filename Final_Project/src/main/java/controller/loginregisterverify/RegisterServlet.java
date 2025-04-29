package controller.loginregisterverify;

import dao.CustomerDAO;
import dao.RestaurantDAO;
import dao.ShipperDAO;
import dao.VerificationDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import model.Customers;
import model.Restaurants;
import model.Shippers;
import org.apache.commons.codec.digest.DigestUtils;
import utils.MailUtil;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.Instant;

@WebServlet(name = "RegisterServlet", urlPatterns = {"/register"})
@MultipartConfig(maxFileSize = 2 * 1024 * 1024)
public class RegisterServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/loginverify/register.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String role = request.getParameter("role");
        if (role == null) {
            request.setAttribute("error", "Role is required.");
            request.getRequestDispatcher("/WEB-INF/views/loginverify/register.jsp").forward(request, response);
            return;
        }

        switch (role) {
            case "customer":
                handleCustomerRegistration(request, response);
                break;
            case "restaurant":
                handleRestaurantRegistration(request, response);
                break;
            case "shipper":
                handleShipperRegistration(request, response);
                break;
            default:
                request.setAttribute("error", "Invalid role");
                request.getRequestDispatcher("/WEB-INF/views/loginverify/register.jsp").forward(request, response);
        }
    }

    private void handleCustomerRegistration(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String address = request.getParameter("address");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirm_password");

        Timestamp now = Timestamp.from(Instant.now());
        Customers customer = new Customers();
        customer.setName(name);
        customer.setEmail(email);
        customer.setPhone(phone);
        customer.setAddress(address);
        customer.setPassword(password);
        customer.setStatusId(0);
        customer.setCreatedAt(now);

        CustomerDAO customerDAO = new CustomerDAO();
        String error = null;
        int userId = -1;

        if (customerDAO.isEmailOrPhoneExists(email, phone)) {
            error = "Email or phone already exists.";
        } else if (!password.equals(confirmPassword)) {
            error = "Passwords do not match.";
        } else {
            userId = customerDAO.insertCustomerAndReturnId(customer);
        }

        handlePostRegistrationResponse(error, userId, email, name, request, response, "customer");
    }

    private void handleRestaurantRegistration(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String address = request.getParameter("address");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirm_password");

        Timestamp now = Timestamp.from(Instant.now());
        Restaurants restaurant = new Restaurants();
        restaurant.setName(name);
        restaurant.setEmail(email);
        restaurant.setPhone(phone);
        restaurant.setAddress(address);
        restaurant.setPassword(password);
        restaurant.setStatusId(0);
        restaurant.setCreatedAt(now);

        RestaurantDAO restaurantDAO = new RestaurantDAO();
        String error = null;
        int userId = -1;

        if (restaurantDAO.isEmailOrPhoneExists(email, phone)) {
            error = "Email or phone already exists.";
        } else if (!password.equals(confirmPassword)) {
            error = "Passwords do not match.";
        } else {
            userId = restaurantDAO.insertRestaurantAndReturnId(restaurant);
        }

        handlePostRegistrationResponse(error, userId, email, name, request, response, "restaurant");
    }

    private void handleShipperRegistration(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String address = request.getParameter("address");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirm_password");
        String cccd = request.getParameter("cccd");
        String driverLicense = request.getParameter("driver_license");
        Part licenseImage = request.getPart("driver_license_image");

        Timestamp now = Timestamp.from(Instant.now());
        Shippers shipper = new Shippers();
        shipper.setName(name);
        shipper.setEmail(email);
        shipper.setPhone(phone);
        shipper.setAddress(address);
        shipper.setPassword(password);
        shipper.setCccd(cccd);
        shipper.setDriverLicense(driverLicense);
        shipper.setStatusId(0); // not active yet
        shipper.setCreatedAt(now);

        ShipperDAO shipperDAO = new ShipperDAO();
        String error = null;
        int userId = -1;

        if (shipperDAO.isEmailOrPhoneExists(email, phone)) {
            error = "Email or phone already exists.";
        } else if (!password.equals(confirmPassword)) {
            error = "Passwords do not match.";
        } else if (licenseImage == null || licenseImage.getSize() == 0) {
            error = "Driver license image is required.";
        } else {
            userId = shipperDAO.insertShipperAndReturnId(shipper, licenseImage);
        }

        handlePostRegistrationResponse(error, userId, email, name, request, response, "shipper");
    }

    private void handlePostRegistrationResponse(String error, int userId, String email, String name,
            HttpServletRequest request, HttpServletResponse response, String role)
            throws ServletException, IOException {
        if (error != null) {
            request.setAttribute("error", error);
            request.getRequestDispatcher("/WEB-INF/views/loginverify/register.jsp").forward(request, response);
            return;
        }

        if (userId == -1) {
            request.setAttribute("error", "Failed to register. Please try again.");
            request.getRequestDispatcher("/WEB-INF/views/loginverify/register.jsp").forward(request, response);
            return;
        }

        String code = String.valueOf(100000 + new java.util.Random().nextInt(900000));
        String hashedCode = DigestUtils.md5Hex(code);

        VerificationDAO verificationDAO = new VerificationDAO();
        verificationDAO.saveVerificationCode(userId, code, hashedCode, role);

        MailUtil.sendVerificationEmail(email, name, code);

        HttpSession session = request.getSession();
        session.setAttribute("role", role);
        session.setAttribute("userId", userId);

        response.sendRedirect("verify");
    }

    @Override
    public String getServletInfo() {
        return "Handles registration view and logic";
    }
}
