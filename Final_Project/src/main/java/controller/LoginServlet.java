package controller;

import dao.CustomerDAO;
import dao.ShipperDAO;
import dao.RestaurantDAO;
import dao.AdminDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.Customers;
import model.Shippers;
import model.Restaurants;
import model.Admins;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;

@WebServlet(name = "LoginServlet", urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String role = request.getParameter("role");

        HttpSession session = request.getSession();

        if (email == null || password == null || role == null) {
            request.setAttribute("error", "Missing login information.");
            request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
            return;
        }

        switch (role) {
            case "customer":
                CustomerDAO customerDAO = new CustomerDAO();
                Customers customer = customerDAO.getCustomerByEmail(email);

                if (customer == null) {
                    request.setAttribute("error", "Invalid email or password.");
                    request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
                    return;
                }

                if (!BCrypt.checkpw(password, customer.getPassword())) {
                    request.setAttribute("error", "Incorrect password.");
                    request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
                    return;
                }

                if (customer.getStatusId() != 1) { // 1 = Active
                    request.setAttribute("error", "Your account is not activated or has been banned.");
                    request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
                    return;
                }

                session.setAttribute("userId", customer.getCustomerId());
                session.setAttribute("role", "customer");
                response.sendRedirect("home");
                break;

            case "shipper":
                ShipperDAO shipperDAO = new ShipperDAO();
                Shippers shipper = shipperDAO.getShipperByEmail(email);

                if (shipper == null) {
                    request.setAttribute("error", "Invalid email or password.");
                    request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
                    return;
                }

                if (!BCrypt.checkpw(password, shipper.getPassword())) {
                    request.setAttribute("error", "Incorrect password.");
                    request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
                    return;
                }

                if (shipper.getStatusId() != 1) {
                    request.setAttribute("error", "Your shipper account is not activated or has been banned.");
                    request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
                    return;
                }

                session.setAttribute("userId", shipper.getShipperId());
                session.setAttribute("role", "shipper");
                response.sendRedirect("home");
                break;

            case "restaurant":
                RestaurantDAO restaurantDAO = new RestaurantDAO();
                Restaurants restaurant = restaurantDAO.getRestaurantByEmail(email);

                if (restaurant == null) {
                    request.setAttribute("error", "Invalid email or password.");
                    request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
                    return;
                }

                if (!BCrypt.checkpw(password, restaurant.getPassword())) {
                    request.setAttribute("error", "Incorrect password.");
                    request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
                    return;
                }

                if (restaurant.getStatusId() != 1) {
                    request.setAttribute("error", "Your restaurant account is not activated or has been banned.");
                    request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
                    return;
                }

                session.setAttribute("userId", restaurant.getRestaurantId());
                session.setAttribute("role", "restaurant");
                response.sendRedirect("home");
                break;

            case "admin":
                AdminDAO adminDAO = new AdminDAO();
                Admins admin = adminDAO.getAdminByEmail(email);

                if (admin == null) {
                    request.setAttribute("error", "Invalid admin credentials.");
                    request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
                    return;
                }

                if (!BCrypt.checkpw(password, admin.getPassword())) {
                    request.setAttribute("error", "Incorrect admin password.");
                    request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
                    return;
                }

                session.setAttribute("userId", admin.getAdminId());
                session.setAttribute("role", "admin");
                response.sendRedirect("home");
                break;

            default:
                request.setAttribute("error", "Invalid role selected.");
                request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
                break;
        }
    }

    @Override
    public String getServletInfo() {
        return "Handles user login for all roles (customer, shipper, restaurant, admin)";
    }
}
