package Servlets;

import java.io.IOException;
import java.sql.*;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

@WebServlet("/DownloadSingleStudentPDF")
public class DownloadSingleStudentPDF extends HttpServlet {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/campus_connect?useSSL=false&serverTimezone=UTC";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "123456";

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String rollno = request.getParameter("rollno");

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=" + rollno + "_Details.pdf");

        try {
            // ✅ Load JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            try (
                Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
                PreparedStatement pst = con.prepareStatement(
                    "SELECT name, rollno, domain_email, mobile, gender, btech_branch, btech_cgpa, inter_marks, ssc_marks, passing_year, " +
                    "option_selected, first_priority, second_priority, father_name, father_mobile, mother_name, mother_mobile " +
                    "FROM tp_registration WHERE rollno = ?"
                );
                ServletOutputStream out = response.getOutputStream()
            ) {
                pst.setString(1, rollno);
                ResultSet rs = pst.executeQuery();

                Document document = new Document(PageSize.A4);
                PdfWriter.getInstance(document, out);
                document.open();

                Font titleFont = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD);
                Font labelFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
                Font valueFont = new Font(Font.FontFamily.HELVETICA, 12);

                Paragraph title = new Paragraph("Training & Placement Registration Details", titleFont);
                title.setAlignment(Element.ALIGN_CENTER);
                document.add(title);
                document.add(Chunk.NEWLINE);

                if (rs.next()) {
                    PdfPTable table = new PdfPTable(2);
                    table.setWidthPercentage(100);
                    table.setSpacingBefore(10f);

                    addRow(table, "Name", rs.getString("name"), labelFont, valueFont);
                    addRow(table, "Roll Number", rs.getString("rollno"), labelFont, valueFont);
                    addRow(table, "Email", rs.getString("domain_email"), labelFont, valueFont);
                    addRow(table, "Mobile", rs.getString("mobile"), labelFont, valueFont);
                    addRow(table, "Gender", rs.getString("gender"), labelFont, valueFont);
                    addRow(table, "Branch", rs.getString("btech_branch"), labelFont, valueFont);
                    addRow(table, "B.Tech CGPA", rs.getString("btech_cgpa"), labelFont, valueFont);
                    addRow(table, "Inter %", rs.getString("inter_marks"), labelFont, valueFont);
                    addRow(table, "SSC %", rs.getString("ssc_marks"), labelFont, valueFont);
                    addRow(table, "Passing Year", rs.getString("passing_year"), labelFont, valueFont);
                    addRow(table, "Option Selected", rs.getString("option_selected"), labelFont, valueFont);
                    addRow(table, "First Priority", rs.getString("first_priority"), labelFont, valueFont);
                    addRow(table, "Second Priority", rs.getString("second_priority"), labelFont, valueFont);
                    addRow(table, "Father Name", rs.getString("father_name"), labelFont, valueFont);
                    addRow(table, "Father Mobile", rs.getString("father_mobile"), labelFont, valueFont);
                    addRow(table, "Mother Name", rs.getString("mother_name"), labelFont, valueFont);
                    addRow(table, "Mother Mobile", rs.getString("mother_mobile"), labelFont, valueFont);

                    document.add(table);
                } else {
                    Paragraph notFound = new Paragraph("❌ No data found for Roll Number: " + rollno, labelFont);
                    notFound.setAlignment(Element.ALIGN_CENTER);
                    document.add(notFound);
                }

                document.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 🔁 Helper to add label-value row to table
    private void addRow(PdfPTable table, String label, String value, Font labelFont, Font valueFont) {
        PdfPCell cell1 = new PdfPCell(new Phrase(label, labelFont));
        PdfPCell cell2 = new PdfPCell(new Phrase(value != null ? value : "-", valueFont));
        cell1.setPadding(5);
        cell2.setPadding(5);
        table.addCell(cell1);
        table.addCell(cell2);
    }
}
