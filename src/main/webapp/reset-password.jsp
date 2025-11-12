<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Quiz App | Reset password</title>
<style>
    body {
        font-family: Arial, sans-serif;
        background-color: #f4f4f4;
        margin: 0;
        padding: 0;
    }

    .container {
        max-width: 400px;
        margin: 50px auto;
        padding: 20px;
        background-color: #fff;
        border-radius: 8px;
        box-shadow: 0 4px 6px rgba(0,0,0,0.1);
    }

    .text-top h1 {
        text-align: center;
        margin-bottom: 20px;
        color: #333;
    }

    label {
        display: block;
        margin-bottom: 5px;
        color: #555;
        font-weight: bold;
    }

    input[type="text"], input[type="password"], input[type="email"] {
        width: 100%;
        padding: 10px;
        margin-bottom: 15px;
        border: 1px solid #ccc;
        border-radius: 4px;
        box-sizing: border-box;
    }

    .submit-btn {
        width: 100%;
        padding: 10px;
        background-color: #007BFF;
        border: none;
        border-radius: 4px;
        color: #fff;
        font-size: 16px;
        cursor: pointer;
        transition: background-color 0.3s ease;
    }

    .submit-btn:hover {
        background-color: #0056b3;
    }

    .error-msg {
        color: red;
        margin-top: 10px;
        text-align: center;
    }

</style>
</head>
<body>
    <%@include file="header.jsp" %>
    
    <div class="container">
        <div class="text-top">
            <h1>Reset Password</h1>
        </div>
        
        <div class="reset-section">
            <form action="${pageContext.request.contextPath}/user" method="post">
                <input type="hidden" name="action" value="reset">
                
                <label>Username:</label>
                <input type="text" name="username" required> 
                
                <label>Email:</label>
                <input type="text" name="email" required>
                
                <label>Phone Number:</label>
                <input type="text" name="number" required>
                
                <label>New Password:</label>
                <input type="text" name="new-password" required>
                
                <button type="submit" class="submit-btn">Reset</button>
            </form>
            
            <% if (request.getAttribute("resetError") != null) { %>
                <div class="error-msg"><%= request.getAttribute("resetError") %></div>
            <% } %>
        </div>
    </div>
    
    <%@include file="footer.jsp" %> 
</body>
</html>
