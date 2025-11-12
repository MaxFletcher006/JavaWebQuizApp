<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.quizapp.Model.Quiz" %>
<%@ page import="com.quizapp.DAO.QuizDAO" %>
<%
    String username = (String) session.getAttribute("username");
    if (username == null) {
        response.sendRedirect("login.jsp");
        return;
    }
    
    // Quiz-уудыг татах
    QuizDAO quizDAO = new QuizDAO();
    List<Quiz> quizzes = quizDAO.fetchQuizData();
%>
<!DOCTYPE html>
<html lang="mn">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Quiz App | Home</title>
    <style>
        body {
		    margin: 0;
		    font-family: "Segoe UI", Arial, sans-serif;
		    background: #f8fafc;
		    color: #1e293b;
		}
		
		.container {
		    padding: 40px 60px;
		    max-width: 1200px;
		    margin: auto;
		}
		
		.menu {
		    display: flex;
		    justify-content: center;
		    margin-bottom: 40px;
		}
		
		.menu button {
		    background: #1b263b;
		    color: white;
		    padding: 16px 36px;
		    border: none;
		    border-radius: 8px;
		    cursor: pointer;
		    font-size: 18px;
		    font-weight: 600;
		}
		
		.menu button:hover {
		    background: #0d1b2a;
		}
		
		.quiz-section {
		    display: grid;
		    grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
		    gap: 30px;
		    justify-items: center;
		    align-items: start;
		}
		
		.quiz {
		    background: #ffffff;
		    border-radius: 12px;
		    border: 1px solid #e2e8f0;
		    box-shadow: 0 2px 6px rgba(0, 0, 0, 0.08);
		    padding: 20px;
		    width: 100%;
		    max-width: 280px;
		    text-align: center;
		    display: flex;
		    flex-direction: column;
		    gap: 15px;
		}
		
		.quiz .title {
		    font-size: 20px;
		    font-weight: 700;
		    color: #0f172a;
		}
		
		.quiz .desc-user,
		.quiz .desc-time {
		    font-size: 14px;
		    color: #475569;
		}
		
		.submit-button {
		    display: flex;
		    flex-direction: column;
		    gap: 10px;
		    width: 100%;
		}
		
		.submit-button form {
		    margin: 0;
		}
		
		.submit-button button {
		    background: #1b263b;
		    color: white;
		    border: none;
		    padding: 10px 0;
		    border-radius: 6px;
		    cursor: pointer;
		    font-weight: 600;
		    font-size: 14px;
		    width: 100%;
		}
		
		.submit-button button:hover {
		    background: #013a78;
		}
		
		.no-quizzes {
		    text-align: center;
		    color: #64748b;
		    font-style: italic;
		    grid-column: 1 / -1;
		}
		
		 .message-box {
	        text-align: center;
	        margin: 20px auto;
	        width: 600px;      
	        height: 60px;      
	        position: relative;
	    }
	
	    #flashMessage {
	        width: 100%;
	        height: 100%;
	        background-color: #d4edda; 
	        color: #155724;
	        padding: 16px;
	        border-radius: 10px;
	        border: 1px solid #c3e6cb;
	        font-weight: 600;
	        font-size: 16px;
	        box-shadow: 0 4px 12px rgba(0,0,0,0.1);
	        box-sizing: border-box;
	        overflow: hidden;       
	        white-space: nowrap;
	        text-overflow: ellipsis; 
	        line-height: 28px;       
	        transition: all 0.5s ease;
	    }
	
	    #flashMessage.error {
	        background-color: #f8d7da;
	        color: #842029;
	        border: 1px solid #f5c2c7;
	    }
	
	    #flashMessage.info {
	        background-color: #cff4fc;
	        color: #055160;
	        border: 1px solid #b6effb;
	    }
	
	    #flashMessage.fade-out {
	        opacity: 0;
	        transform: translateY(-10px);
	    }																					
</style>
</head>
<body>
    <%@ include file="header.jsp" %>

    <div class="container">
        <div class="menu">
            <form action="create.jsp" method="post">
                <button type="submit">CREATE NEW QUIZ</button>
            </form>
        </div>
        
        <div class="message-box">
        <%
		    String message = (String) request.getAttribute("message");
		    if (message != null) {
		%>     
	        <div id="flashMessage">
	        	<%= message %>
	    	</div>
	    	<script>
			    setTimeout(function() {
			        var msg = document.getElementById('flashMessage');
			        if(msg){
			            msg.classList.add('fade-out');
			            setTimeout(function(){ msg.remove(); }, 500);
			        }
			    }, 3000); 
			</script>
			<% } %>						
        </div>
        
        <div class="quiz-section">
            <%
                if (quizzes != null && !quizzes.isEmpty()) {
                    for (Quiz quiz : quizzes) {
            %>
                        <div class="quiz">
                            <div class="title"><%= quiz.getTitle() %></div>
                            <div class="desc-user">Created by: <%= quiz.getUser() %></div>
                            <div class="desc-time">Time: <%= quiz.getTime() %> минут</div>
                            <div class="submit-button">                                
                                
                                <form action="submit.jsp" method="get">
                                    <input type="hidden" name="title" value="<%= quiz.getTitle() %>">
                                    <button type="submit">START</button>
                                </form>
                                
                                <form action="result.jsp" method="get">
                                    <input type="hidden" name="title" value="<%= quiz.getTitle() %>">
                                    <button type="submit">RESULT</button>
                                </form>
                                
                                <% if (username.equals(quiz.getUser())) { %>
                                <form action="edit.jsp" method="get">
                                	<input type="hidden" name="title" value="<%= quiz.getTitle() %>">
                                	<button type="submit">EDIT</button>
                                </form>
                                <% } %>
                            </div>
                        </div>
            <%
                    }
                } 
                else {
            %>
                    <div class="no-quizzes">No quiz available</div>
            <%
                }
            %>
        </div>
    </div>
    <%@ include file="footer.jsp" %>
</body>
</html>