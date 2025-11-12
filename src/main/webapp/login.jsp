<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String loginError = (String) request.getAttribute("loginError");
    String registerSuccess = (String) request.getAttribute("registerSuccess");
    String registerError = (String) request.getAttribute("registerError");
%>
<!DOCTYPE html>
<html lang="mn">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Quiz App | Welcome</title>
    <style>
	    body {
		    font-family: Arial, sans-serif;
		    background-color: #f4f4f4;
		    margin: 0;
		    padding: 0;							
		}
		
		.container {
		    max-width: 900px;
		    margin: 40px auto;
		    background: #fff;
		    padding: 20px;
		    border-radius: 8px;
		    box-shadow: 0 0 8px rgba(0,0,0,0.1);
		}
		
		.welcome-title {
		    text-align: center;
		    margin-bottom: 30px;
		}
		
		.form-section {
		    display: flex;
		    gap: 20px;
		}
		
		.register {
		    flex: 1;
		    border: 1px solid #ddd;
		    padding: 20px;
		    border-radius: 6px;
		    background-color: #fafafa;
		}
		
		.login{
		 	flex: 1;
		    border: 1px solid #ddd;
		    padding: 20px;
		    border-radius: 6px;
		    background-color: #fafafa;
			align-items: center;
			display: flex;
			flex-direction: column;
			justify-content: center; 
			align-items: center;
		}
		
		.form-group {
		    margin-bottom: 15px;
		}
		
		.form-label {
		    display: block;
		    font-weight: bold;
		    margin-bottom: 5px;
		}
		
		.form-input {
		    width: 100%;
		    padding: 8px;
		    border: 1px solid #ccc;
		    border-radius: 4px;
		    box-sizing: border-box;
		}
		
		.submit-btn {
		    width: 100%;
		    padding: 10px;
		    background-color: #007bff;
		    color: white;
		    border: none;
		    border-radius: 4px;
		    cursor: pointer;
		}
		
		.submit-btn:hover {
		    background-color: #0056b3;
		}
		
		.forgot-password {
		    text-align: center;
		    margin-top: 10px;
		}
		
		.forgot-link {
		    font-size: 0.9em;
		    color: #007bff;
		    text-decoration: none;
		}
		
		.forgot-link:hover {
		    text-decoration: underline;
		}
		
		.login-error {
			color: red ; 
			margin-top: 25px; 
		}
		
		.reset-success {
			color: green ;
			margin-bottom: 20px ;
			text-align: center ; 
		}
		
    </style>
</head>
<body>    
    <body>
    <%@ include file="header.jsp" %>
    
    <div class="container">
    	<div class="welcome-title">
    		<h1>Welcome to Web Quiz App</h1>
    	</div>
    	
    	<%if(request.getAttribute("resetSuccess") != null) { %>
    		<div class="reset-success">
    			<%=request.getAttribute("resetSuccess") %>
    		</div>
    	<% } %>
    	
    	<div class="form-section">
    		<div class="login">
    			<form action="${pageContext.request.contextPath}/user" method="post">
			    <input type="hidden" name="action" value="login">
			    
			    <div class="form-group">
			        <label class="form-label">Username</label>
			        <input type="text" name="username" class="form-input" required>
			    </div>
			    
			    <div class="form-group">
			        <label class="form-label">Password</label>
			        <input type="password" name="password" class="form-input" required>
			    </div>
			    
			    <button type="submit" class="submit-btn">Login</button>
			    </form>
			   
			     <div class="forgot-password">
			        <a href="${pageContext.request.contextPath}/reset-password.jsp" class="forgot-link">
			            Forget your password?
			        </a>
			    </div>
			    
			    <%if(request.getAttribute("loginError") != null) { %>
			    	<div class="login-error"> <%=request.getAttribute("loginError") %> </div>
			    <% } %>
			    
    		</div>
    		
    		<div class="register">
    				<form action="${pageContext.request.contextPath}/user" method="post">
				    <input type="hidden" name="action" value="register">
				
				    <div class="form-group">
				        <label class="form-label">Username</label>
				        <input type="text" name="username" class="form-input"
				               placeholder="Your username" required>
				    </div>
				
				    <div class="form-group">
				        <label class="form-label">Email</label>
				        <input type="email" name="email" class="form-input"
				               placeholder="example@email.com" required>
				    </div>
				
				    <div class="form-group">
				        <label class="form-label">Phone Number</label>
				        <input type="tel" name="phone" class="form-input"
				               placeholder="99001234" pattern="[0-9]{8}" 
				               title="8 оронтой тоо оруулна уу" required>
				    </div>
				
				    <div class="form-group">
				        <label class="form-label">Password</label>
				        <input type="password" name="password" class="form-input"
				               placeholder="CutiepieXD" minlength="6" required>
				    </div>
				
				    <div class="form-group">
				        <label class="form-label">Confirm Password</label>
				        <input type="password" name="confirmPassword" class="form-input"
				               placeholder="Re-enter your password" required>
				    </div>
				
				    <button type="submit" class="submit-btn">Register</button>
				</form>
    		</div>
    	</div>
    </div>
    
    <%@ include file="footer.jsp" %>
	</body>
</body>
</html>