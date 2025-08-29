<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>Register | Campus Connect</title>
  <style>
    * {
      padding: 0px;
      margin: 0px;
    }
    body {
      height: 100vh;
      width: 100vw;
      display: flex;
      justify-content: center;
      align-items: center;
      color: white;
      background-color:#505C84;
     /* background-image: url("backgrnd02.jpg");
      background-position: center;
      background-size: cover;
      background-repeat: no-repeat;
      position: relative;*/
    }
    .container {
      width: 90%;
      max-width: 400px;
      padding: 20px;
      border: 2px solid white;
      border-radius: 20px;
      box-shadow: 2px 5px 10px white;
      backdrop-filter: blur(4px);
    }
    .image {
      background-image: url("kitsw2.jpeg");
      background-size: contain;
      background-repeat: no-repeat;
      background-position: center;
      position: relative;
      height: 100px;
      width: 100px;
      border-radius: 50%;
      margin: auto;
    }
    .container .Header {
      height: 20%;
      width: 100%;
      padding: 10px;
      text-align: center;
    }
    form {
      height: 50%;
      width: 100%;
      display: flex;
      justify-content: center;
      align-items: center;
      flex-direction: column;
    }
    input[type="email"],
    input[type="password"],
    input[type="submit"],
    input[type="text"],
    input[type="name"] {
      width: 250px;
      height: 40px;
      margin: 10px 0;
      font-size: 16px;
      padding: 5px;
      border-radius: 5px;
      border: 1px solid #ccc;
    }
    .container .Footer {
      text-align: center;
      margin-top: 20px;
      font-size: 14px;
    }
    input[type="submit"] {
      color: black;
    }
    input[type="submit"]:hover {
      text-decoration: underline;
      cursor: pointer;
      background-color: rgb(59, 238, 59);
      transform: scale(1.1);
    }
    a {
      font-size: 16px;
      color: rgb(59, 238, 59);
    }
    p {
      font-size: 16px;
      color: red;
      cursor: pointer;
    }
    form a:hover {
      color: red;
    }
    .Footer a:hover {
      color: rgb(59, 238, 59);
    }
    h1 {
      font-size: 25px;
      color: white;
      font-weight: 700;
      
    }
    h1:hover {
      cursor: pointer;
    }
    @media (max-width: 480px) {
      .container {
        padding: 15px;
      }
      body background-image {
        background-position: center;
        background-size: cover;
        background-repeat: no-repeat;
      }
      input {
        max-width: 100%;
      }
      .container .Header h3 {
        font-size: 1.2rem;
      }
    }
  </style>
</head>
<body>
  <div class="container">
    <div class="Header">
      <div class="image"></div>
      <h1>Register in Campus Connect</h1>
    </div>

    <!-- Change action to your servlet endpoint -->
    <form action="register" method="post">
      <div class="name">
        <input type="text" name="username" placeholder="Enter username" required>
      </div>
      <div class="em common">
        <input type="email" name="email" placeholder="Enter domain mail" required>
      </div>
      <div class="num common">
        <input type="text" name="mobile" placeholder="Enter mobile number" required>
      </div>
      <div class="oldpass common">
        <input type="password" name="password" placeholder="Enter password" required>
      </div>
      <div class="newpass common">
        <input type="password" name="confirmPassword" placeholder="Confirm password" required>
      </div>
      <div class="sub common">
        <input type="submit" value="Sign In">
      </div>
    </form>

    <div class="Footer">
      <p>Already have an account? <a href="index.jsp">Login</a></p>
    </div>
  </div>

  <script>
    document.addEventListener("DOMContentLoaded", function () {
      const form = document.querySelector("form");
      const email = form.querySelector('input[name="email"]');
      const mobile = form.querySelector('input[name="mobile"]');
      const password = form.querySelector('input[name="password"]');
      const confirmPassword = form.querySelector('input[name="confirmPassword"]');

      form.addEventListener("submit", function (e) {
        // Email domain check
        if (!email.value.endsWith("@kitsw.ac.in")) {
          e.preventDefault();
          alert("Use only domain email (e.g., yourname@kitsw.ac.in)");
          return;
        }

        // Mobile number validation
        if (!/^\d{10}$/.test(mobile.value)) {
          e.preventDefault();
          alert("Enter a valid 10-digit mobile number.");
          return;
        }

        // Password match check
        if (password.value !== confirmPassword.value) {
          e.preventDefault();
          alert("Passwords do not match.");
          return;
        }

        alert("Registration successful!");
        // Allow form to submit to backend servlet
      });
    });
  </script>
</body>
</html>