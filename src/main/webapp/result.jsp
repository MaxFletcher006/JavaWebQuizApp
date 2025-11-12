<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.quizapp.DAO.*, com.quizapp.Model.*, java.util.*" %>
<%
    String username = (String) session.getAttribute("username");
    if (username == null) {
        response.sendRedirect("login.jsp");
        return;
    }

    String quizTitle = request.getParameter("title");
    if (quizTitle == null || quizTitle.trim().isEmpty()) {
        response.sendRedirect("main.jsp");
        return;
    }

    Quiz quiz = new Quiz();
    QuizDAO quizDAO = new QuizDAO();
    quizDAO.fetchQuizDataByTitle(quiz, quizTitle);
    quizDAO.fetchQuestions(quiz, quiz.getId());

    ResultDAO resultDAO = new ResultDAO();
    AnswerDAO answerDAO = new AnswerDAO();

    boolean hasResult = resultDAO.isResultExist(quiz.getId(), username) != 0;
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Quiz App | Result</title>
<style>
    body {
        font-family: Arial, sans-serif;
        background-color: #f8fafc;
        margin: 0;
        padding: 0;
        color: #1e293b;
    }

    .container {
        max-width: 950px;
        margin: 40px auto;
        padding: 20px;
    }

    .back-button {
        text-align: center;
        margin-bottom: 25px;
    }

    .back-button a {
        display: inline-block;
        padding: 12px 28px;
        background-color: #6c757d;
        color: white;
        text-decoration: none;
        border-radius: 10px;
        font-size: 16px;
        font-weight: 600;
        transition: background 0.3s, transform 0.2s;
    }

    .back-button a:hover {
        background-color: #5a6268;
        transform: scale(1.05);
    }

    .quiz-title {
        text-align: center;
        font-size: 28px;
        font-weight: 700;
        margin-bottom: 25px;
        margin-top: 25px;
    }

    .point-section {
        text-align: center;
        background-color: white;
        padding: 25px;
        border-radius: 12px;
        box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
        margin-bottom: 30px;
    }

    .point-section h1 {
        font-size: 40px;
        color: #10b981;
    }

    .question-block {
        background-color: white;
        padding: 20px;
        border-radius: 12px;
        box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
        margin-bottom: 20px;
    }

    .question-text {
        font-weight: 600;
        font-size: 18px;
        margin-bottom: 10px;
    }

    .answer-row {
        padding: 15px 20px;
        border-radius: 10px;
        margin-top: 12px;
        display: flex;
        flex-direction: column;
        gap: 8px;
        font-size: 16px;
        line-height: 1.6;
        box-shadow: 0 1px 4px rgba(0, 0, 0, 0.05);
    }

    .correct {
        background-color: #d1fae5;
        border-left: 5px solid #10b981;
    }

    .wrong {
        background-color: #fee2e2;
        border-left: 5px solid #ef4444;
    }

    .label {
        font-weight: 600;
        color: #475569;
    }

    .no-result {
        text-align: center;
        padding: 50px;
        background-color: white;
        border-radius: 12px;
        box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
    }

    .no-result h1 {
        color: #ef4444;
    }					
</style>
</head>
<body>
<%@ include file="header.jsp" %>

<div class="container">
    <div class="back-button">
        <a href="main.jsp">Back to Home</a>
    </div>
    <% if (hasResult) { 
        Result result = resultDAO.getResult(username, quiz.getId());
        List<Question> questions = quiz.getQuestions();
        List<String> userAnswers = new ArrayList<>() ;
        userAnswers = answerDAO.getUserAnswers(username, quiz.getId()) ; 
        
        for(String answer: userAnswers) {
        	System.out.println(answer) ;
        }
    %>
        <div class="point-section">
        	<div class="quiz-title"><%= quiz.getTitle() %></div>
            <h1>You got <%= ((double) result.getPoint() / quiz.getMaxScore()) * 100 %>%</h1>
            <h2>Your point: <%= result.getPoint() %> </h2>
            <h2>Total point: <%= quiz.getMaxScore() %> </h2>
        </div>

        <% for (int i = 0; i < questions.size(); i++) {
            Question q = questions.get(i); 
            String userAnswer = (i < userAnswers.size()) ? userAnswers.get(i) : "";
            boolean isCorrect = q.getAnswer().trim().equalsIgnoreCase(userAnswer.trim());
        %>
            <div class="question-block">
                <div class="question-text"><%= (i + 1) %>. <%= q.getQuestion() %></div>
                <div class="answer-row <%= isCorrect ? "correct" : "wrong" %>">
                    <div><span class="label">Your answer:</span> <%= (userAnswer.isEmpty() ? "(empty)" : userAnswer) %></div>
                    <div><span class="label">Correct answer:</span> <%= q.getAnswer() %></div>
                    <div><span class="label">
                       <% if (isCorrect) { %>
                        Points: <%= q.getPoint() %> 
	                    <% } else { %>
	                       	Points: 0 
	                    <% } %>
                    </span></div>
                </div>
            </div>
        <% } %>

    <% } else { %>

        <div class="no-result">
            <h1>You haven't taken this quiz 🫤</h1>
            <p>First submit this quiz.</p>
        </div>

    <% } %>

</div>

<%@ include file="footer.jsp" %>
</body>
</html>
