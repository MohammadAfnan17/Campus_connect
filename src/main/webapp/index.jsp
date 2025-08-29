<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Campus Connect</title>
<style>
*
{
  padding:0px;
  margin:0px;
}
body
{
  height:100vh;
  width:100vw;
  display:flex;
  justify-content:center;
  align-items:center;
 background-color:#CCDDEE
   /* background-image: url("backgrnd4.jpg");
  background-size: cover;
  background-repeat: no-repeat;
  position: relative;*/
}
.container
{
   width: 90%;
  max-width: 400px;
  padding: 20px;
  border:2px solid white;
  border-radius: 20px;
  box-shadow:2px 5px 10px white;
  backdrop-filter: blur(4px);
}
.container .Header
{
  height: 20%;
  width: 100%;
  padding: 10px;
  text-align: center;
}
.image
{	 
    background-image: url("kitsw2.jpeg");
    background-size: contain;
    background-repeat: no-repeat;
    background-position:center;
    position: relative;
    height: 100px;
    width: 100px;
    border-radius: 50%;
}
form
{
  height:50%;
  width:100%;
  display:flex;
  justify-content:center;
  align-items:center;
  flex-direction:column;
}
input[type="email"],
input[type="password"],
input[type="submit"]
{
  width:250px;
  height:40px;
  margin:10px 0;
  font-size:16px;
  padding:5px;
  border-radius:5px;
  border:1px solid #ccc;
}
.container .Footer
{
 text-align:center;
  margin-top:20px;
  font-size:14px;
}
input[type="submit"]
{
  color:black;
}
input[type="submit"]:hover
{
    text-decoration: underline;
    cursor: pointer;
    background-color:rgb(59, 238, 59);
    transform:scale(1.1);
}
a{
    color:black;
}
p{
    color:red;
    cursor: pointer;
}
form a:hover{
    color:red;
}
.Footer a:hover{
    color:WHITE;
}
h3{
    font-size: 25px;
    color:black;
    font-weight: 700;
    text-shadow: 2px 5px 10px white;
}
h3:hover{
  cursor: pointer;
}
@media (max-width: 480px) {
  .container {
    padding: 15px;
  }
 
  body {
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
<body >
<% if (request.getAttribute("error") != null) { %>
  <div style="color: red; text-align: center; margin-bottom: 10px;">
    <%= request.getAttribute("error") %>
  </div>
<% } %>

  <div class="container"> 
    <div class="Header">
      <div class="image"></div>
      <h3>WELCOME TO KITSW</h3>
    </div>
    <form action="IndexServlet" method="post" class="Middle">
      <div class="inp"> 
        <input type="email" id="mail" name="email" placeholder="Enter domain mail">
      </div>
      <div class="inp">
        <input type="password" id="pass" name="password" placeholder="Enter password">
      </div>
      <a href="#">forgot password?</a>
      <div class="inp">
        <input type="submit" id="sub" value="Login">
      </div>
    </form>
    <div class="Footer">
      <p>not a member? <a href="register.jsp">signup now</a></p>
      <p>Admin Login---><a href="AdminLogin.jsp">Click here</a>
    </div>
  </div>
  

  <script>
  document.addEventListener("DOMContentLoaded", function () {
    const form = document.querySelector("form");
    const email = document.getElementById("mail");
    const password = document.getElementById("pass");

    form.addEventListener("submit", function (e) {
      if (!email.value || !password.value) {
        e.preventDefault();
        alert("Please fill in both email and password.");
        return;
      }

      const otp = prompt("Enter OTP sent to your email:");

      if (otp !== "1234") {
        e.preventDefault();
        alert("Invalid OTP. Please try again.");
      } else {
        alert("Login successful!");
        
      }
    });
  });
  </script>
</body>
</html>
