<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.quizapp.DAO.*, com.quizapp.Model.*, java.util.*, java.net.*" %>
<%
    String username = (String) session.getAttribute("username");
    if (username == null) {
        response.sendRedirect("login.jsp");
        return;
    }

    String ourQuizTitle = request.getParameter("title");
    if (ourQuizTitle == null || ourQuizTitle.trim().isEmpty()) {
        response.sendRedirect("main.jsp");
        return;
    }

    QuizDAO quizDAO = new QuizDAO();
    Quiz quiz = new Quiz();
    quizDAO.fetchQuizDataByTitle(quiz, ourQuizTitle);
    quizDAO.fetchQuestions(quiz, quiz.getId());
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Quiz App | <%= quiz.getTitle() %></title>
<style>
	body { 
	    font-family: Arial, sans-serif; 
	    background: #f8f9fa; 
	    margin: 0; 
	    padding: 0;
	}
	
	.container { 
	    max-width: 800px; 
	    margin: 30px auto; 
	    background: #fff; 
	    border-radius: 12px; 
	    box-shadow: 0 2px 8px rgba(0,0,0,0.2); 
	    padding: 20px 40px;
	}
	
	.timer { 
	    text-align: right; 
	    font-weight: bold; 
	    color: #e63946; 
	    font-size: 18px; 
	    margin-bottom: 20px;
	}
	
	.question { 
	    margin-bottom: 25px; 
	    padding-bottom: 15px; 
	    border-bottom: 1px solid #e0e0e0;
	}
	
	.question h3 { 
	    color: #333; 
	    margin-bottom: 12px;
	}
	
	.question label { 
	    display: block; 
	    margin: 8px 0; 
	    padding: 8px; 
	    cursor: pointer; 
	    border-radius: 4px; 
	    transition: background 0.2s;
	}
	
	.question label:hover { 
	    background: #f0f0f0;
	}
	
	.question input[type="radio"] { 
	    margin-right: 8px; 
	    cursor: pointer;
	}
	
	.question input[type="text"] { 
	    width: 100%; 
	    padding: 10px; 
	    border-radius: 6px; 
	    border: 1px solid #ccc; 
	    font-size: 14px; 
	    box-sizing: border-box;
	}
	
	.submit-btn { 
	    display: block; 
	    width: 100%; 
	    padding: 14px; 
	    font-size: 16px; 
	    font-weight: bold; 
	    background: #007bff; 
	    color: white; 
	    border: none; 
	    border-radius: 8px; 
	    cursor: pointer; 
	    transition: background 0.3s;
	}
	
	.submit-btn:hover { 
	    background: #0056b3;
	}
	
	.back-button { 
	    max-width: 800px; 
	    margin: 20px auto 0;
	    text-align: center;
	}
	
	.back-button a { 
	    display: inline-block; 
	    padding: 10px 18px; 
	    background: #6c757d; 
	    color: white; 
	    border-radius: 6px; 
	    text-decoration: none; 
	    transition: background 0.3s;
	}
	
	.back-button a:hover { 
	    background: #5a6268;
	}
	
	.exist-message {
	    max-width: 700px;
	    margin: 50px auto; 
	    background: #fff3cd; 
	    border-left: 6px solid #ffeeba; 
	    border-radius: 12px;
	    padding: 30px 40px;
	    text-align: center;
	    box-shadow: 0 4px 20px rgba(0,0,0,0.1);
	}
	
	.exist-message h1 {
	    font-size: 20px;
	    color: #856404; 
	    margin-bottom: 25px;
	    line-height: 1.5;
	}
	
	.exist-message form {
	    margin-top: 20px;
	}
	
	.exist-message .delete-button {
	    background: #dc3545; 
	    color: white; 
	    border: none; 
	    border-radius: 8px; 
	    padding: 12px 28px; 
	    font-size: 16px; 
	    font-weight: bold; 
	    cursor: pointer; 
	    transition: all 0.3s ease;
	}
	
	.exist-message .delete-button:hover {
	    background: #b02a37;
	}
	
	#successMessage { 
	    display: none; 
	    padding: 12px; 
	    background: #28a745; 
	    color: white; 
	    border-radius: 6px; 
	    margin-bottom: 15px;
	}
</style>
<script>
    const button = document.querySelector(".submit-btn");

    window.onbeforeunload = function() {
        return "You haven't finished the quiz yet! Are you sure you want to leave this page?";
    };

    var totalSeconds = <%= quiz.getTime() * 60 %>; 
    var timerElement = null;

    function startTimer() {
        timerElement = document.getElementById("timer");
        updateTimer();
        var countdown = setInterval(function() {
            totalSeconds--;
            updateTimer();
            if (totalSeconds <= 0) {
                clearInterval(countdown);
                alert("Time is up! Your answers will be automatically submitted.");
                submitQuizForm();
            }
        }, 1000);
    }

    function updateTimer() {
        var minutes = Math.floor(totalSeconds / 60);
        var seconds = totalSeconds % 60;
        timerElement.textContent = "Time: " +
            (minutes < 10 ? "0" + minutes : minutes) + ":" +
            (seconds < 10 ? "0" + seconds : seconds);
    }

    function submitQuizForm() {
        window.onbeforeunload = null; 
        var form = document.getElementById("quizForm");
        if (form) form.submit();
        var success = document.getElementById("successMessage");
        if (success) success.style.display = "block";
    }

    function handleSubmit(event) {
        event.preventDefault(); 
        window.onbeforeunload = null; 
        submitQuizForm();
    }

    window.onload = function() {
        startTimer();
        document.getElementById("quizForm").addEventListener("submit", handleSubmit);
    };
</script>
</head>
<body>
<%@ include file="header.jsp" %>

<div class="back-button">
    <a href="main.jsp">Back to Home</a>
</div>

<%
    ResultDAO resultDAO = new ResultDAO();
    if(resultDAO.isResultExist(quiz.getId(), username) != 0) {
%>
   <div class="exist-message">
	    <h1>
	        You already submitted this quiz. <br>
	        If you want to take it again, you must delete previous result
	    </h1>
	    <form id="removeResult" method="post" action="quiz">
	        <input type="hidden" name="action" value="delete-result"/>
	        <input type="hidden" name="quizId" value="<%= quiz.getId() %>"/>
	        <input type="hidden" name="username" value="<%= quiz.getUser() %>"/>
	        <button type="submit" class="delete-button">REMOVE RESULT</button>
	    </form>
	</div>
<%
    }
    else {
%>

<div class="container">
    <div id="successMessage" class="success-message">
       	Submitted successfully 
    </div>

    <h2><%= quiz.getTitle() %></h2>
    <div class="timer" id="timer"></div>

    <form id="quizForm" method="post" action="quiz">
        <input type="hidden" name="action" value="submit">
        <input type="hidden" name="title" value="<%= URLEncoder.encode(quiz.getTitle(), "UTF-8") %>">

        <%
            List<Question> questions = quiz.getQuestions();
            int index = 1;
            for (Question q : questions) {
        %>
        <div class="question">
            <h3><%= index++ %>. <%= q.getQuestion() %> (<%= q.getPoint() %> оноо)</h3>

            <% if (q instanceof MCQ) { 
                MCQ mcq = (MCQ) q; %>
                <label>
                    <input type="radio" name="answer_<%= q.getOrder() %>" 
                           value="<%= URLEncoder.encode(mcq.getChoice_1(), "UTF-8") %>">
                    <%= mcq.getChoice_1() %>
                </label>
                <label>
                    <input type="radio" name="answer_<%= q.getOrder() %>" 
                           value="<%= URLEncoder.encode(mcq.getChoice_2(), "UTF-8") %>">
                    <%= mcq.getChoice_2() %>
                </label>
                <label>
                    <input type="radio" name="answer_<%= q.getOrder() %>" 
                           value="<%= URLEncoder.encode(mcq.getChoice_3(), "UTF-8") %>">
                    <%= mcq.getChoice_3() %>
                </label>
                <label>
                    <input type="radio" name="answer_<%= q.getOrder() %>" 
                           value="<%= URLEncoder.encode(mcq.getChoice_4(), "UTF-8") %>">
                    <%= mcq.getChoice_4() %>
                </label>
            <% } else if (q instanceof SAQ) { %>
                <input type="text" name="answer_<%= q.getOrder() %>" placeholder="Таны хариулт..." autocomplete="off">
            <% } %>
        </div>
        <% } %>

        <button type="submit" class="submit-btn" id="submitBtn">Submit</button>
    </form>
</div>
<% } %>

<%@ include file="footer.jsp" %>
</body>

</html>
