<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="com.quizapp.DAO.*, com.quizapp.Model.*, java.util.*, java.net.*" %>
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
	
	QuizDAO quizDAO = new QuizDAO();
	Quiz quiz = new Quiz();
	quizDAO.fetchQuizDataByTitle(quiz, quizTitle);
	quizDAO.fetchQuestions(quiz, quiz.getId());
	
	if (!username.equals(quiz.getUser())) {
	    response.sendRedirect("main.jsp");
	    return;
	}
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Quiz | Edit Quiz</title>
<style>
body { margin: 0; font-family: "Segoe UI", Arial, sans-serif; background: #f8fafc; color: #1e293b; }
.back-button { padding: 20px 60px; }
.back-button a { color: #1b263b; text-decoration: none; font-weight: 600; font-size: 16px; }
.back-button a:hover { text-decoration: underline; }
.container { padding: 20px 60px; max-width: 1200px; margin: auto; }
h1 { text-align: center; color: #0f172a; margin-bottom: 10px; }
.quiz-info { text-align: center; margin-bottom: 30px; color: #475569; }
.action-buttons { display: flex; justify-content: center; gap: 20px; margin: 30px 0; }
.delete-quiz-button { background: #dc2626; color: white; padding: 12px 40px; border: none; border-radius: 8px; cursor: pointer; font-size: 16px; font-weight: 600; }
.delete-quiz-button:hover { background: #b91c1c; }
.add-question-button { background: #059669; color: white; padding: 12px 40px; border: none; border-radius: 8px; cursor: pointer; font-size: 16px; font-weight: 600; }
.add-question-button:hover { background: #047857; }
.questions-section { margin-top: 40px; }
.question-card { background: white; border-radius: 12px; border: 1px solid #e2e8f0; padding: 25px; margin-bottom: 25px; box-shadow: 0 2px 6px rgba(0, 0, 0, 0.08); position: relative; }
.question-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 15px; padding-bottom: 15px; border-bottom: 2px solid #e2e8f0; }
.question-type { background: #1b263b; color: white; padding: 6px 16px; border-radius: 20px; font-size: 14px; font-weight: 600; }
.question-order { color: #64748b; font-weight: 600; font-size: 18px; }
.delete-question-btn { position: absolute; top: 20px; right: 20px; background: #dc2626; color: white; border: none; padding: 8px 16px; border-radius: 6px; cursor: pointer; font-size: 14px; font-weight: 600; }
.delete-question-btn:hover { background: #b91c1c; }
.form-group { margin-bottom: 20px; }
.form-group label { display: block; margin-bottom: 8px; font-weight: 600; color: #334155; }
.form-group input[type="text"], .form-group input[type="number"], .form-group textarea, .form-group select { width: 100%; padding: 12px; border: 1px solid #cbd5e1; border-radius: 8px; font-size: 15px; box-sizing: border-box; }
.form-group textarea { min-height: 80px; resize: vertical; }
.choices-grid { display: grid; grid-template-columns: 1fr 1fr; gap: 15px; margin-top: 15px; }
.save-all-button { background: #1b263b; color: white; padding: 16px 60px; border: none; border-radius: 8px; cursor: pointer; font-size: 18px; font-weight: 600; display: block; margin: 40px auto; }
.save-all-button:hover { background: #0d1b2a; }
.no-questions { text-align: center; padding: 40px; color: #64748b; font-style: italic; }
.new-question { border: 2px dashed #059669; }
</style>
</head>
<body>
<%@include file="header.jsp" %>	
<div class="back-button"><a href="main.jsp">← Back to Home</a></div>
<div class="container">
    <h1><%=quiz.getTitle()%></h1>
    <div class="quiz-info"><p>Created by: <%=quiz.getUser()%> | Time: <%=quiz.getTime()%> минут</p></div>
    
    <div class="action-buttons">
        <button type="button" class="add-question-button" onclick="addNewQuestion()">+ ШИНЭ АСУУЛТ НЭМЭХ</button>
        <form method="post" action="quiz" style="display: inline;" onsubmit="return confirm('Та энэ quiz-ийг устгахдаа итгэлтэй байна уу?');">
            <input type="hidden" name="action" value="delete-quiz"/>
            <input type="hidden" name="title" value="<%=quiz.getTitle()%>"/>
            <button type="submit" class="delete-quiz-button">QUIZ УСТГАХ</button> 
        </form>
    </div>
    
    <form method="post" action="quiz" id="editQuizForm">
        <input type="hidden" name="action" value="update-quiz"/>
        <input type="hidden" name="quizId" value="<%=quiz.getId()%>"/>
        <input type="hidden" name="quizTitle" value="<%=quiz.getTitle()%>"/>
        
        <div class="questions-section" id="questionsContainer">
            <h2>Асуултууд</h2>
            <%
                List<Question> questions = quiz.getQuestions();
                int questionCount = 0;
                if (questions != null && !questions.isEmpty()) {
                    for (Question q : questions) {
                        questionCount++;
                        if (q instanceof MCQ) {
                            MCQ mcq = (MCQ) q;
            %>
                            <div class="question-card" id="question_<%=questionCount%>" data-question-num="<%=questionCount%>">
                                <button type="button" class="delete-question-btn" onclick="deleteQuestion(<%=questionCount%>)">Устгах</button>
                                <div class="question-header">
                                    <span class="question-type">Сонгох асуулт</span>
                                    <span class="question-order">Асуулт #<%=questionCount%></span>
                                </div>
                                <input type="hidden" name="questionType_<%=questionCount%>" value="mcq"/>
                                <input type="hidden" name="questionId_<%=questionCount%>" value="<%=mcq.getID()%>"/>
                                <input type="hidden" name="questionOrder_<%=questionCount%>" value="<%=mcq.getOrder()%>"/>
                                <input type="hidden" name="isExisting_<%=questionCount%>" value="true"/>
                                <div class="form-group"><label>Асуулт:</label><textarea name="question_<%=questionCount%>" required><%=mcq.getQuestion()%></textarea></div>
                                <div class="choices-grid">
                                    <div class="form-group"><label>Сонголт 1:</label><input type="text" name="choice1_<%=questionCount%>" value="<%=mcq.getChoice_1()%>" required/></div>
                                    <div class="form-group"><label>Сонголт 2:</label><input type="text" name="choice2_<%=questionCount%>" value="<%=mcq.getChoice_2()%>" required/></div>
                                    <div class="form-group"><label>Сонголт 3:</label><input type="text" name="choice3_<%=questionCount%>" value="<%=mcq.getChoice_3()%>" required/></div>
                                    <div class="form-group"><label>Сонголт 4:</label><input type="text" name="choice4_<%=questionCount%>" value="<%=mcq.getChoice_4()%>" required/></div>
                                </div>
                                <div class="form-group"><label>Зөв хариулт:</label><input type="text" name="answer_<%=questionCount%>" value="<%=mcq.getAnswer()%>" required/></div>
                                <div class="form-group"><label>Оноо:</label><input type="number" name="point_<%=questionCount%>" value="<%=mcq.getPoint()%>" min="1" required/></div>
                            </div>
            <%
                        } else if (q instanceof SAQ) {
                            SAQ saq = (SAQ) q;
            %>
                            <div class="question-card" id="question_<%=questionCount%>" data-question-num="<%=questionCount%>">
                                <button type="button" class="delete-question-btn" onclick="deleteQuestion(<%=questionCount%>)">Устгах</button>
                                <div class="question-header">
                                    <span class="question-type">Богино хариулт</span>
                                    <span class="question-order">Асуулт #<%=questionCount%></span>
                                </div>
                                <input type="hidden" name="questionType_<%=questionCount%>" value="saq"/>
                                <input type="hidden" name="questionId_<%=questionCount%>" value="<%=saq.getId()%>"/>
                                <input type="hidden" name="questionOrder_<%=questionCount%>" value="<%=saq.getOrder()%>"/>
                                <input type="hidden" name="isExisting_<%=questionCount%>" value="true"/>
                                <div class="form-group"><label>Асуулт:</label><textarea name="question_<%=questionCount%>" required><%=saq.getQuestion()%></textarea></div>
                                <div class="form-group"><label>Зөв хариулт:</label><input type="text" name="answer_<%=questionCount%>" value="<%=saq.getAnswer()%>" required/></div>
                                <div class="form-group"><label>Оноо:</label><input type="number" name="point_<%=questionCount%>" value="<%=saq.getPoint()%>" min="1" required/></div>
                            </div>
            <%      }
                    }
                }
            %>
            <% if (questions == null || questions.isEmpty()) { %>
                <div class="no-questions" id="noQuestionsMsg">Энэ quiz-д асуулт байхгүй байна. Шинэ асуулт нэмнэ үү.</div>
            <% } %>
        </div>
        <input type="hidden" name="questionCount" id="questionCount" value="<%=questionCount%>"/>
        <button type="submit" class="save-all-button">БҮХИЙ ӨӨРЧЛӨЛТИЙГ ХАДГАЛАХ</button>
    </form>
</div>
<%@include file="footer.jsp" %>
<script>
var questionCounter = parseInt(document.getElementById('questionCount').value);
var maxExistingOrder = 0;

window.addEventListener('DOMContentLoaded', function() {
    var existingCards = document.querySelectorAll('.question-card[data-question-num]');
    for (var i = 0; i < existingCards.length; i++) {
        var orderInput = existingCards[i].querySelector('input[name^="questionOrder_"]');
        if (orderInput) {
            var orderValue = parseInt(orderInput.value);
            if (orderValue > maxExistingOrder) maxExistingOrder = orderValue;
        }
    }
    console.log('Loaded. questionCounter=' + questionCounter + ', maxExistingOrder=' + maxExistingOrder);
});

function addNewQuestion() {
    var noMsg = document.getElementById('noQuestionsMsg');
    if (noMsg) noMsg.style.display = 'none';
    
    questionCounter++;
    var newOrder = maxExistingOrder + questionCounter;
    console.log('Adding question #' + questionCounter + ' with order=' + newOrder);
    
    var container = document.getElementById('questionsContainer');
    var div = document.createElement('div');
    div.className = 'question-card new-question';
    div.id = 'question_' + questionCounter;
    div.setAttribute('data-question-num', questionCounter);
    div.innerHTML = '<button type="button" class="delete-question-btn" onclick="deleteQuestion(' + questionCounter + ')">Устгах</button>' +
        '<div class="question-header"><span class="question-type">Шинэ асуулт</span><span class="question-order">Асуулт #' + questionCounter + ' (Order: ' + newOrder + ')</span></div>' +
        '<input type="hidden" name="isExisting_' + questionCounter + '" value="false"/>' +
        '<input type="hidden" name="newQuestionOrder_' + questionCounter + '" value="' + newOrder + '"/>' +
        '<div class="form-group"><label>Асуултын төрөл:</label><select name="questionType_' + questionCounter + '" onchange="toggleChoices(' + questionCounter + ')" required>' +
        '<option value="">-- Сонгоно уу --</option><option value="mcq">Сонгох асуулт (MCQ)</option><option value="saq">Богино хариулт (SAQ)</option></select></div>' +
        '<div class="form-group"><label>Асуулт:</label><textarea name="question_' + questionCounter + '" required></textarea></div>' +
        '<div id="choices_' + questionCounter + '" style="display:none;"><div class="choices-grid">' +
        '<div class="form-group"><label>Сонголт 1:</label><input type="text" name="choice1_' + questionCounter + '"/></div>' +
        '<div class="form-group"><label>Сонголт 2:</label><input type="text" name="choice2_' + questionCounter + '"/></div>' +
        '<div class="form-group"><label>Сонголт 3:</label><input type="text" name="choice3_' + questionCounter + '"/></div>' +
        '<div class="form-group"><label>Сонголт 4:</label><input type="text" name="choice4_' + questionCounter + '"/></div></div></div>' +
        '<div class="form-group"><label>Зөв хариулт:</label><input type="text" name="answer_' + questionCounter + '" required/></div>' +
        '<div class="form-group"><label>Оноо:</label><input type="number" name="point_' + questionCounter + '" min="1" value="1" required/></div>';
    
    container.appendChild(div);
    document.getElementById('questionCount').value = questionCounter;
    console.log('questionCount updated to: ' + questionCounter);
    div.scrollIntoView({ behavior: 'smooth', block: 'center' });
}

function toggleChoices(num) {
    var sel = document.querySelector('select[name="questionType_' + num + '"]');
    var choicesDiv = document.getElementById('choices_' + num);
    if (sel && choicesDiv) {
        if (sel.value === 'mcq') {
            choicesDiv.style.display = 'block';
            var inputs = choicesDiv.querySelectorAll('input');
            for (var i = 0; i < inputs.length; i++) inputs[i].required = true;
        } else {
            choicesDiv.style.display = 'none';
            var inputs = choicesDiv.querySelectorAll('input');
            for (var i = 0; i < inputs.length; i++) { inputs[i].required = false; inputs[i].value = ''; }
        }
    }
}

function deleteQuestion(num) {
    if (confirm('Та энэ асуултыг устгахдаа итгэлтэй байна уу?')) {
        var div = document.getElementById('question_' + num);
        if (div) {
            var hidden = document.createElement('input');
            hidden.type = 'hidden';
            hidden.name = 'deletedQuestion_' + num;
            hidden.value = 'true';
            div.appendChild(hidden);
            div.style.display = 'none';
        }
        var remaining = document.querySelectorAll('.question-card:not([style*="display: none"])');
        if (remaining.length === 0) {
            var noMsg = document.getElementById('noQuestionsMsg');
            if (!noMsg) {
                noMsg = document.createElement('div');
                noMsg.className = 'no-questions';
                noMsg.id = 'noQuestionsMsg';
                noMsg.textContent = 'Энэ quiz-д асуулт байхгүй байна. Шинэ асуулт нэмнэ үү.';
                document.getElementById('questionsContainer').appendChild(noMsg);
            } else {
                noMsg.style.display = 'block';
            }
        }
    }
}

document.getElementById('editQuizForm').addEventListener('submit', function(e) {
    console.log('Form submitting...');
    var visible = document.querySelectorAll('.question-card:not([style*="display: none"])');
    console.log('Visible questions: ' + visible.length);
    
    if (visible.length === 0) {
        alert('Хамгийн багадаа 1 асуулт байх ёстой!');
        e.preventDefault();
        return false;
    }
    
    var maxNum = 0;
    for (var i = 0; i < visible.length; i++) {
        var num = parseInt(visible[i].getAttribute('data-question-num'));
        if (num > maxNum) maxNum = num;
    }
    document.getElementById('questionCount').value = maxNum;
    console.log('Final questionCount: ' + maxNum);
    
    var hasError = false;
    for (var i = 0; i < visible.length; i++) {
        var card = visible[i];
        var num = card.getAttribute('data-question-num');
        var isEx = card.querySelector('input[name="isExisting_' + num + '"]');
        
        if (isEx && isEx.value === 'false') {
            var typeSelect = card.querySelector('select[name="questionType_' + num + '"]');
            console.log('Question ' + num + ': questionType=' + (typeSelect ? typeSelect.value : 'null'));
            
            if (!typeSelect || !typeSelect.value) {
                alert('Асуулт #' + num + ' - Асуултын төрлөө ЗААВАЛ сонгоно уу!');
                hasError = true;
                if (typeSelect) {
                    typeSelect.scrollIntoView({ behavior: 'smooth', block: 'center' });
                    typeSelect.focus();
                }
                break;
            }
        }
    }
    
    if (hasError) {
        e.preventDefault();
        return false;
    }
    
    console.log('Validation passed, submitting...');
    return true;
});
</script>
</body>
</html>