<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    if (session == null || session.getAttribute("admin") == null) {
        response.sendRedirect("AdminLogin.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html>
<head>
    <title>Select Year and Branch</title>
    <style>
        @import url('https://fonts.googleapis.com/css2?family=Roboto:wght@300;400;700&display=swap');

        body {
            font-family: 'Roboto', sans-serif;
            background-color: #f0f2f5; 
            display: flex;
            justify-content: center;
            align-items: center;
            min-height: 100vh;
            margin: 0;
        }

        .container {
            background-color: #ffffff;
            padding: 40px;
            border-radius: 10px;
            box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
            text-align: center;
            width: 100%;
            max-width: 500px;
        }

        h2 {
            color: #333;
            margin-bottom: 30px;
            font-weight: 700;
            font-size: 28px;
        }

        .form-group {
            margin-bottom: 25px;
            text-align: left;
        }

        label {
            display: block;
            margin-bottom: 8px;
            color: #555;
            font-weight: 500;
            font-size: 16px;
        }

        select {
            width: 100%;
            padding: 12px 15px;
            font-size: 16px;
            border: 1px solid #ddd;
            border-radius: 5px;
            background-color: #fff;
            appearance: none;
            -webkit-appearance: none;
            -moz-appearance: none;
            cursor: pointer;
            box-sizing: border-box; 
        }

        select:focus {
            border-color: #007bff;
            outline: none;
            box-shadow: 0 0 0 0.2rem rgba(0, 123, 255, 0.25);
        }

        button {
            background-color: #007bff;
            color: white;
            padding: 12px 25px;
            border: none;
            border-radius: 5px;
            font-size: 18px;
            cursor: pointer;
            transition: background-color 0.3s ease, transform 0.2s ease;
            width: 100%;
            font-weight: 600;
        }

        button:hover {
            background-color: #0056b3; 
            transform: translateY(-2px);
        }

        button:active {
            transform: translateY(0);
        }
    </style>
    <script>
        function goToSelection() {
            const year = document.getElementById("year").value;
            const branch = document.getElementById("branch").value;

            if (year && branch) {
                window.location.href = "ViewStudentsByYear?year=" + year + "&branch=" + branch;
            } else {
                alert("Please select both Year and Branch.");
            }
        }
    </script>
</head>
<body>
    <div class="container">
        <h2>Select Passing Out Year and Branch</h2>

        <div class="form-group">
            <label for="year">Year:</label>
            <select id="year">
                <option value="">-- Select Year --</option>
                <option value="2025">2025</option>
                <option value="2026">2026</option>
                <option value="2027">2027</option>
            </select>
        </div>

        <div class="form-group">
            <label for="branch">Branch:</label>
            <select id="branch">
                <option value="">-- Select Branch --</option>
                <option value="CSN">CSN</option>
        
            </select>
        </div>

        <button onclick="goToSelection()">View Students</button>
    </div>
</body>
</html>