package controller.userservlet;

import dao.CustomerDAO;
import dao.RestaurantDAO;
import dao.ShipperDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;
import model.Customers;
import model.Restaurants;
import model.Shippers;

@WebServlet(name = "AdminServlet", urlPatterns = {"/admin"})
public class AdminServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || !"admin".equals(session.getAttribute("role"))) {
            response.sendRedirect("login");
            return;
        }

        // Load data để show trong dashboard
        CustomerDAO customerDAO = new CustomerDAO();
        ShipperDAO shipperDAO = new ShipperDAO();
        RestaurantDAO restaurantDAO = new RestaurantDAO();

        List<Customers> customers = customerDAO.getAllCustomers();
        List<Shippers> shippers = shipperDAO.getAllShippers();
        List<Restaurants> restaurants = restaurantDAO.getAllRestaurants();

        request.setAttribute("customers", customers);
        request.setAttribute("shippers", shippers);
        request.setAttribute("restaurants", restaurants);

        request.getRequestDispatcher("/WEB-INF/views/homepage/admin.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Admin Dashboard Servlet";
    }
}
