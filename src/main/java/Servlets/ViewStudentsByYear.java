package Servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

@WebServlet("/ViewStudentsByYear")
public class ViewStudentsByYear extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private static final String DB_URL = "jdbc:mysql://localhost:3306/campus_connect?useSSL=false&serverTimezone=UTC";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "123456";

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String year = request.getParameter("year");

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        out.println("<!DOCTYPE html><html><head><meta charset='UTF-8'>");
        out.println("<title>Students - Batch " + year + "</title>");
        out.println("<style>");
        out.println("table { width: 100%; border-collapse: collapse; }");
        out.println("th, td { border: 1px solid #ccc; padding: 8px; text-align: left; }");
        out.println("th { background-color: #4CAF50; color: white; }");
        out.println(".download-button { padding: 5px 10px; background-color: #007bff; color: white; border: none; border-radius: 4px; cursor: pointer; }");
        out.println(".download-button:hover { background-color: #0056b3; }");
        out.println("</style></head><body>");
        out.println("<h2>Registered Students - Batch " + year + "</h2>");
        out.println("<table><tr>");

        out.println("<th>Name</th><th>Roll No</th><th>Email</th><th>Mobile</th><th>Gender</th><th>Branch</th>");
        out.println("<th>B.Tech CGPA</th><th>Inter %</th><th>SSC %</th><th>Year</th>");
        out.println("<th>Option</th><th>1st Priority</th><th>2nd Priority</th>");
        out.println("<th>Father Name</th><th>Father Mobile</th><th>Mother Name</th><th>Mother Mobile</th>");
        out.println("<th>Payment Proof</th>");
        out.println("<th>Download</th></tr>");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            try (
                Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
                PreparedStatement pst = con.prepareStatement("SELECT * FROM tp_registration WHERE passing_year = ?")
            ) {
                pst.setString(1, year);
                ResultSet rs = pst.executeQuery();

                boolean hasData = false;
                while (rs.next()) {
                    hasData = true;

                    String rollno = rs.getString("rollno");
                    String email = rs.getString("domain_email");
                    String paymentProofFileName = rs.getString("payment_proof"); // Column from DB

                    out.println("<tr>");
                    out.println("<td>" + rs.getString("name") + "</td>");
                    out.println("<td>" + rollno + "</td>");
                    out.println("<td>" + email + "</td>");
                    out.println("<td>" + rs.getString("mobile") + "</td>");
                    out.println("<td>" + rs.getString("gender") + "</td>");
                    out.println("<td>" + rs.getString("btech_branch") + "</td>");
                    out.println("<td>" + rs.getString("btech_cgpa") + "</td>");
                    out.println("<td>" + rs.getString("inter_marks") + "</td>");
                    out.println("<td>" + rs.getString("ssc_marks") + "</td>");
                    out.println("<td>" + rs.getString("passing_year") + "</td>");
                    out.println("<td>" + rs.getString("option_selected") + "</td>");
                    out.println("<td>" + rs.getString("first_priority") + "</td>");
                    out.println("<td>" + rs.getString("second_priority") + "</td>");
                    out.println("<td>" + rs.getString("father_name") + "</td>");
                    out.println("<td>" + rs.getString("father_mobile") + "</td>");
                    out.println("<td>" + rs.getString("mother_name") + "</td>");
                    out.println("<td>" + rs.getString("mother_mobile") + "</td>");

                    // ✅ Payment Proof column
                    if (paymentProofFileName != null && !paymentProofFileName.trim().isEmpty()) {
                        out.println("<td><a href='uploads/payment_proofs/" + paymentProofFileName + "' target='_blank'>View</a></td>");
                    } else {
                        out.println("<td>No file</td>");
                    }

                    // ✅ Individual PDF download by rollno
                    out.println("<td>");
                    out.println("<form action='DownloadSingleStudentPDF' method='post' target='_blank'>");
                    out.println("<input type='hidden' name='rollno' value='" + rollno + "'>");
                    out.println("<button type='submit' class='download-button'>PDF</button>");
                    out.println("</form>");
                    out.println("</td>");

                    out.println("</tr>");
                }

                if (!hasData) {
                    out.println("<tr><td colspan='19' style='text-align:center;'>No records found for batch " + year + "</td></tr>");
                }

            } catch (SQLException e) {
                e.printStackTrace();
                out.println("<tr><td colspan='19' style='color:red;'>Database Error: " + e.getMessage() + "</td></tr>");
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            out.println("<tr><td colspan='19' style='color:red;'>Driver Error: " + e.getMessage() + "</td></tr>");
        }

        out.println("</table>");

        // ✅ Download all as PDF
        out.println("<br><form action='DownloadStudentsPDF' method='post' target='_blank'>");
        out.println("<input type='hidden' name='year' value='" + year + "'>");
        out.println("<button type='submit' class='download-button'>Download All as PDF</button>");
        out.println("</form>");
        
        // ✅ Download all as Excel
        out.println("<br><form action='DownloadAllStudentsExcel' method='post' target='_blank'>");
        out.println("<input type='hidden' name='year' value='" + year + "'>");
        out.println("<button type='submit' class='download-button' style='background-color:#28a745;'>Download All as Excel</button>");
        out.println("</form>");

        out.println("</body></html>");
    }
}
