<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>T and P Registration</title>
  <style>
    body {
      background: #f1f3f6;
      font-family: Arial, sans-serif;
      padding: 20px;
    }
    .container {
      max-width: 700px;
      margin: auto;
      background: #fff;
      padding: 30px;
      border-radius: 10px;
      box-shadow: 0 0 10px rgba(0,0,0,0.1);
    }
    h2, h3 {
      text-align: center;
      color: #003366;
      margin-bottom: 15px;
    }
    form {
      display: flex;
      flex-direction: column;
    }
    input, textarea, select {
      margin-bottom: 15px;
      padding: 10px;
      font-size: 1rem;
      border: 1px solid #ccc;
      border-radius: 6px;
    }
    textarea {
      resize: vertical;
      height: 80px;
    }
    input[type="submit"] {
      background-color: #003366;
      color: white;
      font-weight: bold;
      cursor: pointer;
      transition: background 0.3s ease;
    }
    input[type="submit"]:hover {
      background-color: #0055aa;
    }
  </style>
</head>
<body>
  <div class="container">
    <h2>Training and Placement Registration Form</h2>

    <form action="TpRegister" method="post" id="tpForm" enctype="multipart/form-data">
      
      <h3>Student Info</h3>
      <input type="text" name="name" placeholder="Full Name" required>
      <input type="text" name="rollno" placeholder="Roll Number" required  pattern="[A-Z0-9]{8}" title="Enter 8 characters: uppercase letters and numbers only ">
      <select name="gender" required>
        <option value="">Select Gender</option>
        <option value="Male">Male</option>
        <option value="Female">Female</option>
        <option value="Other">Other</option>
      </select>
      
      <input type="tel" name="mobile" placeholder="Mobile Number" required pattern="\d{10}" title="Enter 10-digit mobile number">
      <input type="date" name="dob" required>
      <input type="text" name="aadhar" placeholder="Aadhar Number" required pattern="\d{12}" title="Enter 12-digit Aadhar number">
      <input type="email" name="domain_email" placeholder="College Email" required>
      <input type="email" name="personal_email" placeholder="Personal Email" required>
      <textarea name="current_address" placeholder="Current Address" required></textarea>
      <textarea name="permanent_address" placeholder="Permanent Address" required></textarea>

      <h3>Payment Details</h3>
      <input type="date" name="payment_date" required>
      <input type="text" name="payment_reference" placeholder="Transaction Ref. No" required>
      <input type="number" name="amount_paid" placeholder="Amount Paid" min="0" required>
      <label>Upload Payment Proof (Image/PDF):</label>
      <input type="file" name="payment_proof" accept=".jpg,.jpeg,.png,.pdf" required>

      <h3>Academic Info</h3>
      <input type="text" name="btech_branch" placeholder="B.Tech Branch" required>
      <input type="number" id="btech_cgpa" name="btech_cgpa" placeholder="B.Tech CGPA" required step="0.01" min="0" max="10">
	  <small style="color: gray; font-size: 0.9em;">Formula: CGPA = Percentage ÷ 9.5</small>
      <input type="number" name="btech_backlogs" placeholder="Number of Backlogs" required min="0">
      <input type="number" name="btech_gaps" placeholder="Study Gaps (if any)" required min="0">
      <input type="number" name="ssc_marks" placeholder="SSC % / CGPA" required step="0.01" min="0" max="100">
      <input type="number" name="inter_marks" placeholder="Inter/Diploma %" required step="0.01" min="0" max="100">
      <input type="text" name="passing_year" placeholder="Passing Year (e.g., 2027)" required pattern="\d{4}" title="Enter 4-digit year">

      <h3>Placement Preferences</h3>
      <select name="option_selected" required>
        <option value="">Select Option</option>
        <option value="Training">Training</option>
        <option value="Placement">Placement</option>
        <option value="Both">Both</option>
      </select>
      <input type="text" name="first_priority" placeholder="1st Priority Domain" required>
      <input type="text" name="second_priority" placeholder="2nd Priority Domain" required>

      <h3>Parent Info</h3>
      <input type="text" name="father_name" placeholder="Father's Name" required>
      <input type="tel" name="father_mobile" placeholder="Father's Mobile No." required pattern="\d{10}" title="Enter 10-digit number">
      <input type="text" name="mother_name" placeholder="Mother's Name" required>
      <input type="tel" name="mother_mobile" placeholder="Mother's Mobile No." required pattern="\d{10}" title="Enter 10-digit number">

      <input type="submit" value="Submit">
    </form>
  </div>

  <script>
  // Alert after user finishes typing domain email
  /*document.querySelector("[name='domain_email']").addEventListener("blur", function () {
      if (this.value.trim() !== "") {
          alert("Please recheck your College Email: " + this.value);
      }
  });*/

  // Alert after user finishes typing personal email
 /* document.querySelector("[name='personal_email']").addEventListener("blur", function () {
      if (this.value.trim() !== "") {
          alert("Please recheck your Personal Email: " + this.value);
      }
  });*/
  /*document.getElementById("btech_cgpa").addEventListener("blur", function () {
      let value = parseFloat(this.value);
      if (!isNaN(value) && value > 10) {
          alert("Please enter CGPA, not percentage.\nFormula: CGPA = Percentage ÷ 9.5");
          this.value = ""; // clear the wrong entry
      }
  });*/
  /*document.getElementsByName("dob")[0].addEventListener("blur", function() {
      showRecheckAlert("Date of Birth");
  });*/

    //document.getElementById("tpForm").addEventListener("submit", function(e) {
 // prevent default submission
      //const enteredOtp = prompt("Enter the OTP:");
      //if (enteredOtp === "1234") {
       // alert("OTP verified. Submitting form...");
       // this.submit(); // allow form to submit
     // } else {
       // alert("Invalid OTP. Please try again.");
      //}
    //});
  </script>
</body>
</html>
