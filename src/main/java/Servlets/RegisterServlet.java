package Servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.*;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1. Get data from form
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String mobile = request.getParameter("mobile");
        String password = request.getParameter("password");

        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            // 2. Load JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // 3. Connect to MySQL
            conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/campus_connect", // DB name
                "root",    // username
                "123456" // password
            );

            // 4. Prepare SQL INSERT query
            String sql = "INSERT INTO users (username, email, mobile, password) VALUES (?, ?, ?, ?)";

            stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, email);
            stmt.setString(3, mobile);
            stmt.setString(4, password); // (You should hash this in real apps)

            // 5. Execute update
            int rowsInserted = stmt.executeUpdate();

            if (rowsInserted > 0) {
                // 6. Redirect or show success message
                response.sendRedirect("index.jsp");
            } else {
                response.getWriter().println("Registration failed. Please try again.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().println("Database Error: " + e.getMessage());
        } finally {
            try { if (stmt != null) stmt.close(); } catch (Exception e) {}
            try { if (conn != null) conn.close(); } catch (Exception e) {}
        }
    }
}
