<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%    
    // Одоогийн хуудасны нэрийг тодорхойлох
    String currentPage = request.getRequestURI();
    String pageName = currentPage.substring(currentPage.lastIndexOf("/") + 1);
    
    // Header title тодорхойлох
    String headerTitle = "";
    if (pageName.equals("main.jsp")) 
    {
        headerTitle = "Home";
    } 
    
    else if (pageName.equals("edit.jsp")) 
    {
        headerTitle = "Edit";
    } 
    
    else if (pageName.equals("create-quiz.jsp")) 
    {
        headerTitle = "New Quiz";
    } 
    
    else if (pageName.equals("login.jsp")) {
        headerTitle = "Сур сур бас дахин сур - В.И. Ленин";
    } 
        
    else if (pageName.startsWith("quiz")) {
        String headerQuizTitle = (String) request.getAttribute("quizTitle");
        headerTitle = headerQuizTitle != null ? headerQuizTitle : "Quiz";
    }
    
    else {
        headerTitle = "Сур сур бас дахин сур - В.И. Ленин";
    }
%>

<style>
	.body {
		font-family: 'Poppins', sans-serif;
	}

    .quiz-header {
        background: linear-gradient(135deg, #0D1B2A 0%, #1B263B 100%);
        padding: 15px 30px;
        box-shadow: 0 4px 15px rgba(0, 0, 0, 0.3);
        position: sticky;
        top: 0;
        z-index: 1000;
        transition: all 0.3s ease;
    }
    
    .header-container {
        display: flex;
        justify-content: space-between;
        align-items: center;
        max-width: 1400px;
        margin: 0 auto;
    }
    
    .header-left {
        display: flex;
        align-items: center;
        gap: 15px;
        min-width: 200px;
    }
    
    .logo {
        height: 40px;
        width: auto;
    }
    
    .username-display {
        color: #FFFFFF;
        font-weight: 500;
        font-size: 20px;
        padding: 8px 16px;
        background: rgba(255, 255, 255, 0.08);
        border-radius: 20px;
        backdrop-filter: blur(8px);
        box-shadow: 0 2px 6px rgba(0,0,0,0.4);
    }
    
    .header-center {
        flex: 1;
        text-align: center;
    }
    
    .header-title {
        color: #FFFFFF;
        font-size: 30px;
        font-weight: 700;
        margin: 0;
        text-transform: uppercase;
        letter-spacing: 1px;
        text-shadow: 1px 1px 6px rgba(0,0,0,0.5);
    }
    
    .header-right {
        min-width: 200px;
        text-align: right;
    }
    
    .logout-btn {
        background: rgba(255, 255, 255, 0.15);
        color: #FFFFFF;
        border: none;
        padding: 10px 24px;
        border-radius: 25px;
        font-weight: 600;
        cursor: pointer;
        transition: all 0.3s ease;
        text-decoration: none;
        display: inline-block;
    }
    
    .logout-btn:hover {
        background: rgba(255, 255, 255, 0.25);
        transform: translateY(-2px);
        box-shadow: 0 4px 12px rgba(0, 0, 0, 0.4);
    }
    
</style>

<header class="quiz-header">
    <div class="header-container">
        <div class="header-left">
            <% if (session.getAttribute("username") != null && (!pageName.equals("login.jsp") && !pageName.equals("reset-password.jsp"))) { %>
                <div class="username-display">
                   Hello <%= session.getAttribute("username") %>
                </div>
            <% } %>
        </div>
        
        <div class="header-center">
            <h1 class="header-title"><%= headerTitle %></h1>
        </div>
        
        <div class="header-right">
            <% if (session.getAttribute("username") != null && (!pageName.equals("login.jsp") && !pageName.equals("reset-password.jsp"))) { %>
                <a href="${pageContext.request.contextPath}/" class="logout-btn">
                    Log Out 
                </a>
            <% } %>
            
            <%if(pageName.equals("reset-password.jsp")) { %>
            	<a href="${pageContext.request.contextPath}/" class="logout-btn">
            		Back to Home
            	</a>
            <% } %>
        </div>
    </div>
</header>