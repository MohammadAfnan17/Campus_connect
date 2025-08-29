package Servlets;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.*;
import java.sql.*;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

@WebServlet("/DownloadStudentsPDF")
public class DownloadStudentsPDF extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private static final String DB_URL = "jdbc:mysql://localhost:3306/campus_connect";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "123456";

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String year = request.getParameter("year");

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=Students_" + year + ".pdf");

        try (
            Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
            PreparedStatement pst = con.prepareStatement(
                "SELECT name, domain_email, mobile, gender, btech_branch, btech_cgpa, inter_marks, ssc_marks, passing_year, " +
                "first_priority, second_priority, option_selected, father_name, father_mobile, mother_name, mother_mobile " +
                "FROM tp_registration WHERE passing_year = ?"
            );
            OutputStream out = response.getOutputStream()
        ) {
            pst.setString(1, year);
            ResultSet rs = pst.executeQuery();

            Document document = new Document(PageSize.A3.rotate()); // Landscape layout
            PdfWriter.getInstance(document, out);
            document.open();

            Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
            Paragraph title = new Paragraph("Registered Students - Batch " + year, titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(Chunk.NEWLINE);

            PdfPTable table = new PdfPTable(16); // now 16 columns
            table.setWidthPercentage(100);

            String[] headers = {
                "Name", "Email", "Mobile", "Gender", "Branch", "B.Tech %", "Inter %", "SSC %",
                "Year", "Option","1st Priority", "2nd Priority",  "Father Name", "Father Mobile", "Mother Name", "Mother Mobile"
            };

            for (String header : headers) {
                PdfPCell cell = new PdfPCell(new Phrase(header));
                cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                table.addCell(cell);
            }

            while (rs.next()) {
                table.addCell(rs.getString("name"));
                table.addCell(rs.getString("domain_email"));
                table.addCell(rs.getString("mobile"));
                table.addCell(rs.getString("gender"));
                table.addCell(rs.getString("btech_branch"));
                table.addCell(rs.getString("btech_cgpa"));
                table.addCell(rs.getString("inter_marks"));
                table.addCell(rs.getString("ssc_marks"));
                table.addCell(rs.getString("passing_year"));
                table.addCell(rs.getString("option_selected"));
                table.addCell(rs.getString("first_priority"));
                table.addCell(rs.getString("second_priority"));
                table.addCell(rs.getString("father_name"));
                table.addCell(rs.getString("father_mobile"));
                table.addCell(rs.getString("mother_name"));
                table.addCell(rs.getString("mother_mobile"));
            }

            document.add(table);
            document.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
