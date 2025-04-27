package controller.homepage;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet(name = "HomeServlet", urlPatterns = {"/home"})
public class HomeServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("role") == null || session.getAttribute("userId") == null) {
            response.sendRedirect("login");
            return;
        }

        String role = (String) session.getAttribute("role");
        redirectByRole(response, session, role);
    }

    private void redirectByRole(HttpServletResponse response, HttpSession session, String role) throws IOException {
        if ("customer".equals(role)) {
            response.sendRedirect("customer"); // Servlet: CustomerDashboardServlet
        } else if ("shipper".equals(role)) {
            response.sendRedirect("shipper"); // Servlet: ShipperDashboardServlet
        } else if ("restaurant".equals(role)) {
            response.sendRedirect("restaurant"); // Servlet: RestaurantDashboardServlet
        } else if ("admin".equals(role)) {
            response.sendRedirect("admin"); // Servlet: AdminDashboardServlet
        } else {
            session.invalidate();
            response.sendRedirect("login");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response); // Cho phép POST gọi lại GET
    }

    @Override
    public String getServletInfo() {
        return "Redirects authenticated users to the correct dashboard based on role";
    }
}
