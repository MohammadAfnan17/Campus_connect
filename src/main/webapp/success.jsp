<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
  <title>Registration Successful</title>
  <style>
    body {
      font-family: Arial, sans-serif;
      background-color: #e6f2ff;
      display: flex;
      justify-content: center;
      align-items: center;
      height: 100vh;
    }
    .success-box {
      background: white;
      padding: 40px;
      border-radius: 10px;
      box-shadow: 0 0 10px rgba(0,0,0,0.2);
      text-align: center;
    }
    .success-box h2 {
      color: #006600;
    }
    .success-box a {
      display: inline-block;
      margin-top: 20px;
      text-decoration: none;
      color: white;
      background: #003366;
      padding: 10px 20px;
      border-radius: 6px;
    }
    .success-box a:hover {
      background-color: #0055aa;
    }
  </style>
</head>
<body>
  <div class="success-box">
    <h2>Registration Successful!</h2>
    <p>Thank you for registering for TandP activities.</p>
    <a href="index.jsp">Go to Login</a>
  </div>
</body>
</html>
