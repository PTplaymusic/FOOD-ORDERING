// PendingAccountsServlet.java (Update)
package controller.userservlet.pendingaccounts;

import dao.RestaurantDAO;
import dao.ShipperDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.Restaurants;
import model.Shippers;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "PendingAccountsServlet", urlPatterns = {"/pending-accounts"})
public class PendingAccountsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("role") == null || !"admin".equals(session.getAttribute("role"))) {
            response.sendRedirect("login");
            return;
        }

        ShipperDAO shipperDAO = new ShipperDAO();
        RestaurantDAO restaurantDAO = new RestaurantDAO();

        List<Shippers> pendingShippers = shipperDAO.getAllShippers();
        List<Restaurants> pendingRestaurants = restaurantDAO.getAllRestaurants();

        request.setAttribute("pendingShippers", pendingShippers);
        request.setAttribute("pendingRestaurants", pendingRestaurants);

        request.getRequestDispatcher("/WEB-INF/views/pendingaccount/adminpending.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String type = request.getParameter("type"); // shipper or restaurant
        int id = Integer.parseInt(request.getParameter("id"));
        int newStatus = Integer.parseInt(request.getParameter("status"));

        if ("shipper".equals(type)) {
            ShipperDAO shipperDAO = new ShipperDAO();
            shipperDAO.updateStatus(id, newStatus);
        } else if ("restaurant".equals(type)) {
            RestaurantDAO restaurantDAO = new RestaurantDAO();
            restaurantDAO.updateStatus(id, newStatus);
        }

        response.sendRedirect("pending-accounts");
    }
}
