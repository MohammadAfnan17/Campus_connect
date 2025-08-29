package Servlets;

import jakarta.servlet.ServletException;
import java.nio.file.Paths;

import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.*;
import java.sql.*;
import java.util.Enumeration;
import java.util.UUID;
import java.util.Arrays;
import java.util.List;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@MultipartConfig(
    fileSizeThreshold = 1024 * 1024 * 1, // 1MB
    maxFileSize = 1024 * 1024 * 10,      // 10MB per file
    maxRequestSize = 1024 * 1024 * 50    // 50MB total
)
@WebServlet("/TpRegister")
public class TpRegisterServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final String DB_URL = "jdbc:mysql://localhost:3306/campus_connect";
    private final String DB_USER = "root";
    private final String DB_PASS = "123456";
    
    // Allowed file types
    private final List<String> ALLOWED_EXTENSIONS = Arrays.asList(".jpg", ".jpeg", ".png", ".pdf");
    private final List<String> ALLOWED_MIME_TYPES = Arrays.asList(
        "image/jpeg", "image/jpg", "image/png", "application/pdf"
    );
    
    private final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB

    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");

        // Debug: Print all received parameters
        System.out.println("------ Form Parameters ------");
        Enumeration<String> paramNames = request.getParameterNames();
        while (paramNames.hasMoreElements()) {
            String param = paramNames.nextElement();
            String value = request.getParameter(param);
            System.out.println(param + " = " + (value != null ? value : "NULL"));
        }
        System.out.println("-----------------------------");

        try {
            // Extract all form fields
            String name = getParameter(request, "name");
            String rollno = getParameter(request, "rollno");
            String gender = getParameter(request, "gender");
            String mobile = getParameter(request, "mobile");
            String dob = getParameter(request, "dob");
            String aadhar = getParameter(request, "aadhar");
            String domain_email = getParameter(request, "domain_email");
            String personal_email = getParameter(request, "personal_email");
            String current_address = getParameter(request, "current_address");
            String permanent_address = getParameter(request, "permanent_address");
            String payment_date = getParameter(request, "payment_date");
            String payment_reference = getParameter(request, "payment_reference");
            String amount_paid = getParameter(request, "amount_paid");
            String btech_branch = getParameter(request, "btech_branch");
            String btech_cgpa = getParameter(request, "btech_cgpa");
            String btech_backlogs = getParameter(request, "btech_backlogs");
            String btech_gaps = getParameter(request, "btech_gaps");
            String ssc_marks = getParameter(request, "ssc_marks");
            String inter_marks = getParameter(request, "inter_marks");
            String passing_year = getParameter(request, "passing_year");
            String option_selected = getParameter(request, "option_selected");
            String first_priority = getParameter(request, "first_priority");
            String second_priority = getParameter(request, "second_priority");
            String father_name = getParameter(request, "father_name");
            String father_mobile = getParameter(request, "father_mobile");
            String mother_name = getParameter(request, "mother_name");
            String mother_mobile = getParameter(request, "mother_mobile");

            // Validate required fields
            if (name.isEmpty() || rollno.isEmpty() || passing_year.isEmpty()) {
                response.getWriter().println("<h3 style='color:red;'>Required fields are missing.</h3>");
                return;
            }

            // Handle file upload
            Part paymentProofPart = request.getPart("payment_proof");
            String paymentProofPath = handleFileUpload(paymentProofPart, rollno, request, response);
            
            if (paymentProofPath == null) {
                return; // Error already sent
            }

            // Database insertion
            insertToDatabase(name, rollno, gender, mobile, dob, aadhar, domain_email, personal_email,
                           current_address, permanent_address, payment_date, payment_reference, amount_paid,
                           btech_branch, btech_cgpa, btech_backlogs, btech_gaps, ssc_marks, inter_marks,
                           passing_year, option_selected, first_priority, second_priority,
                           father_name, father_mobile, mother_name, mother_mobile, paymentProofPath, response);

        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().println("<h3 style='color:red;'>Error: " + e.getMessage() + "</h3>");
        }
    }
    
    private String getParameter(HttpServletRequest request, String paramName) {
        String value = request.getParameter(paramName);
        return (value != null) ? value.trim() : "";
    }
    
    private String handleFileUpload(Part filePart, String rollno, HttpServletRequest request, HttpServletResponse response) 
            throws IOException, ServletException {
        
        if (filePart == null || filePart.getSize() == 0) {
            response.getWriter().println("<h3 style='color:red;'>Payment proof file is required.</h3>");
            return null;
        }
        
        if (filePart.getSize() > MAX_FILE_SIZE) {
            response.getWriter().println("<h3 style='color:red;'>File size exceeds 10MB limit.</h3>");
            return null;
        }
        
        String originalFileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
        String mimeType = filePart.getContentType();
        String fileExtension = getFileExtension(originalFileName).toLowerCase();
        
        System.out.println("Original filename: " + originalFileName);
        System.out.println("MIME type: " + mimeType);
        System.out.println("File extension: " + fileExtension);
        
        // Validate file type
        if (!ALLOWED_EXTENSIONS.contains(fileExtension)) {
            response.getWriter().println("<h3 style='color:red;'>Invalid file type. Only JPG, PNG, and PDF files are allowed.</h3>");
            return null;
        }
        
        if (mimeType == null || !ALLOWED_MIME_TYPES.contains(mimeType.toLowerCase())) {
            response.getWriter().println("<h3 style='color:red;'>Invalid file format detected. MIME type: " + mimeType + "</h3>");
            return null;
        }
        
        // Get the real path to the webapp directory
        String webappPath = request.getServletContext().getRealPath("");
        System.out.println("Webapp path: " + webappPath);
        
        // Create upload directory path
        String uploadDirPath = webappPath + File.separator + "uploads" + File.separator + "payment_proofs";
        File uploadDir = new File(uploadDirPath);
        
        if (!uploadDir.exists()) {
            boolean created = uploadDir.mkdirs();
            System.out.println("Upload directory created: " + created + " at " + uploadDirPath);
        }
        
        // Generate unique filename
        String uniqueFileName = generateUniqueFileName(rollno, fileExtension);
        String fullFilePath = uploadDirPath + File.separator + uniqueFileName;
        
        System.out.println("Saving file to: " + fullFilePath);
        
        try {
            // Write file using the Part.write() method with full path
            filePart.write(fullFilePath);
            
            // Verify file was written
            File savedFile = new File(fullFilePath);
            if (savedFile.exists() && savedFile.length() > 0) {
                System.out.println("File uploaded successfully: " + fullFilePath + " (Size: " + savedFile.length() + " bytes)");
                
                // Return relative path for database (using forward slashes for web compatibility)
                return "uploads/payment_proofs/" + uniqueFileName;
            } else {
                response.getWriter().println("<h3 style='color:red;'>File upload verification failed.</h3>");
                return null;
            }
            
        } catch (IOException e) {
            e.printStackTrace();
            response.getWriter().println("<h3 style='color:red;'>File upload failed: " + e.getMessage() + "</h3>");
            return null;
        }
    }
    
    private void insertToDatabase(String name, String rollno, String gender, String mobile, String dob, 
                                String aadhar, String domain_email, String personal_email, String current_address, 
                                String permanent_address, String payment_date, String payment_reference, 
                                String amount_paid, String btech_branch, String btech_cgpa, String btech_backlogs, 
                                String btech_gaps, String ssc_marks, String inter_marks, String passing_year, 
                                String option_selected, String first_priority, String second_priority, 
                                String father_name, String father_mobile, String mother_name, String mother_mobile, 
                                String paymentProofPath, HttpServletResponse response) throws IOException {
        
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);

            String sql = "INSERT INTO tp_registration (name, rollno, gender, mobile, dob, aadhar, domain_email, personal_email," +
                    "current_address, permanent_address, payment_date, payment_reference, amount_paid, btech_branch, btech_cgpa," +
                    "btech_backlogs, btech_gaps, ssc_marks, inter_marks, option_selected, first_priority, second_priority," +
                    "father_name, father_mobile, mother_name, mother_mobile, passing_year, payment_proof_path) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            stmt = conn.prepareStatement(sql);
            
            stmt.setString(1, name);
            stmt.setString(2, rollno);
            stmt.setString(3, gender);
            stmt.setString(4, mobile);
            stmt.setString(5, dob);
            stmt.setString(6, aadhar);
            stmt.setString(7, domain_email);
            stmt.setString(8, personal_email);
            stmt.setString(9, current_address);
            stmt.setString(10, permanent_address);
            stmt.setString(11, payment_date);
            stmt.setString(12, payment_reference);
            stmt.setString(13, amount_paid);
            stmt.setString(14, btech_branch);
            stmt.setString(15, btech_cgpa);
            stmt.setString(16, btech_backlogs);
            stmt.setString(17, btech_gaps);
            stmt.setString(18, ssc_marks);
            stmt.setString(19, inter_marks);
            stmt.setString(20, option_selected);
            stmt.setString(21, first_priority);
            stmt.setString(22, second_priority);
            stmt.setString(23, father_name);
            stmt.setString(24, father_mobile);
            stmt.setString(25, mother_name);
            stmt.setString(26, mother_mobile);
            stmt.setString(27, passing_year);
            stmt.setString(28, paymentProofPath);

            int result = stmt.executeUpdate();
            
            if (result > 0) {
                System.out.println("Registration successful for: " + rollno);
                response.sendRedirect("success.jsp");
            } else {
                response.getWriter().println("<h3 style='color:red;'>Registration failed.</h3>");
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            response.getWriter().println("<h3 style='color:red;'>Database Driver Error: " + e.getMessage() + "</h3>");
        } catch (SQLException e) {
            e.printStackTrace();
            String errorMsg = "Database Error: " + e.getMessage();
            if (e.getMessage().contains("Duplicate entry")) {
                errorMsg = "Registration failed: Roll number already exists.";
            }
            response.getWriter().println("<h3 style='color:red;'>" + errorMsg + "</h3>");
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    private String getFileExtension(String fileName) {
        if (fileName == null || fileName.lastIndexOf('.') == -1) {
            return "";
        }
        return fileName.substring(fileName.lastIndexOf('.'));
    }
    
    private String generateUniqueFileName(String rollno, String extension) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String uuid = UUID.randomUUID().toString().substring(0, 8);
        return rollno + "_" + timestamp + "_" + uuid + extension;
    }
}