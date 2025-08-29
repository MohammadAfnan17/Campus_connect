package Servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.*;
import java.sql.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@WebServlet("/downloadPaymentProof")
public class FileDownloadServlet extends HttpServlet {
    
    private static final long serialVersionUID = 1L;
    
    private final String DB_URL = "jdbc:mysql://localhost:3306/campus_connect";
    private final String DB_USER = "root";
    private final String DB_PASS = "123456";
    
    private final int BUFFER_SIZE = 8192;
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String rollno = request.getParameter("rollno");
        String idParam = request.getParameter("id");
        
        if ((rollno == null || rollno.trim().isEmpty()) && (idParam == null || idParam.trim().isEmpty())) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Roll number or ID is required");
            return;
        }
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
            
            String sql;
            if (rollno != null && !rollno.trim().isEmpty()) {
                sql = "SELECT payment_proof_path, name, rollno FROM tp_registration WHERE rollno = ?";
                stmt = conn.prepareStatement(sql);
                stmt.setString(1, rollno.trim());
                System.out.println("Searching for rollno: " + rollno.trim());
            } else {
                sql = "SELECT payment_proof_path, name, rollno FROM tp_registration WHERE id = ?";
                stmt = conn.prepareStatement(sql);
                stmt.setInt(1, Integer.parseInt(idParam.trim()));
                System.out.println("Searching for id: " + idParam.trim());
            }
            
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                String filePath = rs.getString("payment_proof_path");
                String studentName = rs.getString("name");
                String studentRollNo = rs.getString("rollno");
                
                System.out.println("Found student: " + studentName + " (Roll: " + studentRollNo + ")");
                System.out.println("File path from DB: " + filePath);
                
                if (filePath != null && !filePath.trim().isEmpty()) {
                    // Get webapp real path
                    String webappPath = request.getServletContext().getRealPath("");
                    
                    // Construct full file path
                    String fullFilePath;
                    if (filePath.startsWith("uploads")) {
                        // Path already includes uploads folder
                        fullFilePath = webappPath + File.separator + filePath.replace("/", File.separator);
                    } else {
                        // Legacy path - add uploads folder
                        fullFilePath = webappPath + File.separator + "uploads" + File.separator + "payment_proofs" + File.separator + filePath;
                    }
                    
                    System.out.println("Looking for file at: " + fullFilePath);
                    
                    File file = new File(fullFilePath);
                    
                    if (file.exists() && file.isFile()) {
                        System.out.println("File found, size: " + file.length() + " bytes");
                        
                        String contentType = getContentType(filePath);
                        String downloadFileName = generateDownloadFileName(studentName, studentRollNo, filePath);
                        
                        System.out.println("Content-Type: " + contentType);
                        System.out.println("Download filename: " + downloadFileName);
                        
                        // Set response headers
                        response.setContentType(contentType);
                        response.setContentLengthLong(file.length());
                        response.setHeader("Content-Disposition", "attachment; filename=\"" + 
                            URLEncoder.encode(downloadFileName, StandardCharsets.UTF_8.name()) + "\"");
                        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
                        response.setHeader("Pragma", "no-cache");
                        response.setDateHeader("Expires", 0);
                        
                        // Stream file content
                        try (FileInputStream fileIn = new FileInputStream(file);
                             BufferedInputStream bufferedIn = new BufferedInputStream(fileIn);
                             OutputStream out = response.getOutputStream()) {
                            
                            byte[] buffer = new byte[BUFFER_SIZE];
                            int bytesRead;
                            int totalBytes = 0;
                            
                            while ((bytesRead = bufferedIn.read(buffer)) != -1) {
                                out.write(buffer, 0, bytesRead);
                                totalBytes += bytesRead;
                            }
                            
                            out.flush();
                            System.out.println("File download completed. Bytes sent: " + totalBytes);
                        }
                        
                    } else {
                        System.err.println("File not found at path: " + fullFilePath);
                        System.err.println("File exists: " + file.exists());
                        System.err.println("Is file: " + file.isFile());
                        
                        // List directory contents for debugging
                        File parentDir = file.getParentFile();
                        if (parentDir != null && parentDir.exists()) {
                            System.err.println("Parent directory contents:");
                            String[] files = parentDir.list();
                            if (files != null) {
                                for (String f : files) {
                                    System.err.println("  - " + f);
                                }
                            }
                        }
                        
                        response.sendError(HttpServletResponse.SC_NOT_FOUND, "File not found on server");
                    }
                } else {
                    System.err.println("No file path in database for student: " + studentName);
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "No payment proof uploaded");
                }
            } else {
                System.err.println("Student not found in database");
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Student not found");
            }
            
        } catch (ClassNotFoundException e) {
            System.err.println("Database driver error: " + e.getMessage());
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database driver error");
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error");
        } catch (NumberFormatException e) {
            System.err.println("Invalid ID format: " + idParam);
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid ID format");
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Server error");
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    private String getContentType(String fileName) {
        if (fileName == null) return "application/octet-stream";
        
        String lower = fileName.toLowerCase();
        if (lower.endsWith(".pdf")) return "application/pdf";
        if (lower.endsWith(".jpg") || lower.endsWith(".jpeg")) return "image/jpeg";
        if (lower.endsWith(".png")) return "image/png";
        
        return "application/octet-stream";
    }
    
    private String generateDownloadFileName(String studentName, String rollno, String originalPath) {
        // Clean student name (remove special characters and limit length)
        String cleanName = studentName.replaceAll("[^a-zA-Z0-9\\s]", "")
                                    .replaceAll("\\s+", "_")
                                    .substring(0, Math.min(studentName.length(), 20));
        
        String extension = getFileExtension(originalPath);
        return cleanName + "_" + rollno + "_payment_proof" + extension;
    }
    
    private String getFileExtension(String fileName) {
        if (fileName == null || fileName.lastIndexOf('.') == -1) return "";
        return fileName.substring(fileName.lastIndexOf('.'));
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        doGet(request, response);
    }
}