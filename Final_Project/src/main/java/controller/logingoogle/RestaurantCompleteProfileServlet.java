package controller.logingoogle;

import dao.RestaurantDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.Restaurants;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.Instant;

@WebServlet(name = "RestaurantCompleteProfileServlet", urlPatterns = {"/restaurant-complete-profile"})
@MultipartConfig(maxFileSize = 5 * 1024 * 1024)
public class RestaurantCompleteProfileServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/Google/restaurant-complete-profile.jsp").forward(request, response);
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
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirm_password");

        if (phone == null || address == null || password == null || confirmPassword == null) {
            request.setAttribute("error", "Please fill in all required fields.");
            request.getRequestDispatcher("/WEB-INF/Google/restaurant-complete-profile.jsp").forward(request, response);
            return;
        }

        if (!password.equals(confirmPassword)) {
            request.setAttribute("error", "Passwords do not match.");
            request.getRequestDispatcher("/WEB-INF/Google/restaurant-complete-profile.jsp").forward(request, response);
            return;
        }

        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt(12));

        Restaurants restaurant = new Restaurants();
        restaurant.setName(name);
        restaurant.setEmail(email);
        restaurant.setPhone(phone);
        restaurant.setAddress(address);
        restaurant.setPassword(hashedPassword);
        restaurant.setStatusId(2); // Pending Approval (đúng logic restaurant mới)
        restaurant.setCreatedAt(Timestamp.from(Instant.now()));

        RestaurantDAO restaurantDAO = new RestaurantDAO();
        int restaurantId = restaurantDAO.insertRestaurantAndReturnId(restaurant);

        if (restaurantId > 0) {
            session.setAttribute("role", "restaurant");
            session.setAttribute("userId", restaurantId);

            session.removeAttribute("oauth_email");
            session.removeAttribute("oauth_name");
            session.removeAttribute("oauth_role");

            response.sendRedirect("home");
        } else {
            request.setAttribute("error", "Failed to create restaurant account. Please try again.");
            request.getRequestDispatcher("/WEB-INF/Google/restaurant-complete-profile.jsp").forward(request, response);
        }
    }

    @Override
    public String getServletInfo() {
        return "Handles restaurant profile completion after Google login";
    }
}
