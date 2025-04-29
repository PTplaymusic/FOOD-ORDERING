package controller.loginregisterverify;

import dao.CustomerDAO;
import dao.ShipperDAO;
import dao.RestaurantDAO;
import dao.UserDAO;
import dao.VerificationDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.Customers;
import model.Shippers;
import model.Restaurants;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.IOException;

@WebServlet(name = "VerifyServlet", urlPatterns = {"/verify"})
public class VerifyServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/loginverify/verify.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null) {
            response.sendRedirect("login");
            return;
        }

        String inputCode = request.getParameter("code");

        String oauthEmail = (String) session.getAttribute("oauth_email");
        String oauthCode = (String) session.getAttribute("oauth_code");
        String oauthRole = (String) session.getAttribute("oauth_role");

        String role = (String) session.getAttribute("role");
        Integer userId = (Integer) session.getAttribute("userId");

        if (inputCode == null || inputCode.isEmpty()) {
            request.setAttribute("error", "Please enter the verification code.");
            request.getRequestDispatcher("/WEB-INF/views/loginverify/verify.jsp").forward(request, response);
            return;
        }

        if (oauthEmail != null) {
            // ➡️ Xác thực Google OAuth
            if (inputCode.equals(oauthCode)) {
                session.removeAttribute("oauth_code");

                switch (oauthRole) {
                    case "customer":
                        CustomerDAO customerDAO = new CustomerDAO();
                        Customers customer = customerDAO.getCustomerByEmail(oauthEmail);

                        if (customer != null) {
                            // Đã có -> login
                            session.setAttribute("role", "customer");
                            session.setAttribute("userId", customer.getCustomerId());
                            cleanOAuthSession(session);
                            response.sendRedirect("home");
                        } else {
                            // Chưa có -> bổ sung thông tin
                            response.sendRedirect("customer-complete-profile");
                        }
                        break;

                    case "shipper":
                        ShipperDAO shipperDAO = new ShipperDAO();
                        Shippers shipper = shipperDAO.getShipperByEmail(oauthEmail);

                        if (shipper != null) {
                            session.setAttribute("role", "shipper");
                            session.setAttribute("userId", shipper.getShipperId());
                            cleanOAuthSession(session);
                            response.sendRedirect("home");
                        } else {
                            response.sendRedirect("shipper-complete-profile");
                        }
                        break;

                    case "restaurant":
                        RestaurantDAO restaurantDAO = new RestaurantDAO();
                        Restaurants restaurant = restaurantDAO.getRestaurantByEmail(oauthEmail);

                        if (restaurant != null) {
                            session.setAttribute("role", "restaurant");
                            session.setAttribute("userId", restaurant.getRestaurantId());
                            cleanOAuthSession(session);
                            response.sendRedirect("home");
                        } else {
                            response.sendRedirect("restaurant-complete-profile");
                        }
                        break;

                    default:
                        response.sendRedirect("login");
                        break;
                }
            } else {
                request.setAttribute("error", "Incorrect verification code.");
                request.getRequestDispatcher("/WEB-INF/views/loginverify/verify.jsp").forward(request, response);
            }
        } else {
            // ➡️ Xác thực đăng ký bằng form thường
            VerificationDAO verificationDAO = new VerificationDAO();
            String hashedCode = DigestUtils.md5Hex(inputCode);

            if (verificationDAO.verifyCode(role, userId, hashedCode)) {
                UserDAO userDAO = new UserDAO();
                userDAO.updateStatus(role, userId, 1); // 1 = Active

                verificationDAO.markCodeAsUsed(role, userId, hashedCode);

                session.removeAttribute("verify_code");
                response.sendRedirect("home");
            } else {
                request.setAttribute("error", "Invalid or expired verification code.");
                request.getRequestDispatcher("/WEB-INF/views/loginverify/verify.jsp").forward(request, response);
            }
        }
    }

    private void cleanOAuthSession(HttpSession session) {
        session.removeAttribute("oauth_email");
        session.removeAttribute("oauth_name");
        session.removeAttribute("oauth_role");
    }

    @Override
    public String getServletInfo() {
        return "Handles both Registration and Google OAuth email verification for all roles";
    }
}
