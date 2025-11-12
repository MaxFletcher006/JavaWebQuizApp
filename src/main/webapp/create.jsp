<%@ page language="java" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Quiz | Create New Quiz</title>
<style>
    body {
        margin: 0;
        font-family: "Segoe UI", Arial, sans-serif;
        background: #f8fafc;
        color: #1e293b;
    }

    .container {
        max-width: 900px;
        margin: 0 auto;
        padding: 40px 20px;
    }

    .back-button {
        text-align: center;
        margin-bottom: 30px;
    }

    .back-button a {
        display: inline-block;
        padding: 12px 28px;
        background: linear-gradient(135deg, #6c757d, #5a6268);
        color: white;
        text-decoration: none;
        border-radius: 10px;
        font-size: 16px;
        font-weight: 600;
        transition: all 0.3s ease;
        box-shadow: 0 4px 10px rgba(0, 0, 0, 0.15);
    }

    .back-button a:hover {
        transform: scale(1.05);
        background: linear-gradient(135deg, #5a6268, #495057);
    }

    .create-form {
        background: linear-gradient(145deg, #ffffff, #f1f5f9);
        padding: 35px;
        border-radius: 16px;
        box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
        margin-bottom: 30px;
        border: 1px solid #e2e8f0;
    }

    .create-form h2 {
        color: #0f172a;
        margin-bottom: 25px;
        text-align: center;
        font-size: 26px;
        font-weight: 700;
    }

    .form-section {
        background: #fff;
        padding: 25px;
        border-radius: 12px;
        margin-bottom: 25px;
        border: 2px solid #e2e8f0;
    }

    .section-title {
        font-size: 18px;
        font-weight: 700;
        color: #1b263b;
        margin-bottom: 20px;
        padding-bottom: 10px;
        border-bottom: 2px solid #415a77;
    }

    .form-group {
        margin-bottom: 20px;
    }

    .form-group label {
        display: block;
        margin-bottom: 8px;
        color: #475569;
        font-weight: 600;
        font-size: 14px;
    }

    .form-group input[type="text"],
    .form-group input[type="number"],
    .form-group textarea {
        width: 100%;
        padding: 12px;
        border: 2px solid #e0e0e0;
        border-radius: 8px;
        font-size: 14px;
        transition: border-color 0.3s;
        box-sizing: border-box;
        font-family: inherit;
    }

    .form-group input:focus,
    .form-group textarea:focus {
        outline: none;
        border-color: #415a77;
    }

    .form-group textarea {
        resize: vertical;
        min-height: 80px;
    }

    .question-item {
        background: #f8fafc;
        padding: 20px;
        border-radius: 10px;
        margin-bottom: 15px;
        border: 1px solid #cbd5e1;
        position: relative;
    }

    .question-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 15px;
    }

    .question-number {
        font-weight: 700;
        color: #1b263b;
        font-size: 16px;
    }

    .remove-btn {
        background: #ef4444;
        color: white;
        border: none;
        padding: 6px 14px;
        border-radius: 6px;
        cursor: pointer;
        font-size: 13px;
        font-weight: 600;
        transition: all 0.3s;
    }

    .remove-btn:hover {
        background: #dc2626;
        transform: scale(1.05);
    }

    .add-question-btns {
        display: flex;
        gap: 15px;
        margin-top: 15px;
    }

    .add-btn {
        flex: 1;
        padding: 12px;
        background: linear-gradient(135deg, #1b263b, #415a77);
        color: white;
        border: none;
        border-radius: 8px;
        cursor: pointer;
        font-size: 14px;
        font-weight: 600;
        transition: all 0.3s;
    }

    .add-btn:hover {
        background: linear-gradient(135deg, #0d1b2a, #1b263b);
        transform: scale(1.02);
    }

    .submit-btn {
        width: 100%;
        padding: 16px;
        background: linear-gradient(135deg, #1b263b, #415a77);
        color: white;
        border: none;
        border-radius: 12px;
        font-size: 18px;
        font-weight: 700;
        cursor: pointer;
        transition: all 0.3s;
        margin-top: 20px;
        box-shadow: 0 5px 15px rgba(0, 0, 0, 0.2);
    }

    .submit-btn:hover {
        background: linear-gradient(135deg, #0d1b2a, #1b263b);
        transform: scale(1.02);
    }

    .error-message {
        background-color: #fee2e2;
        color: #991b1b;
        padding: 12px;
        border-radius: 8px;
        margin-bottom: 20px;
        border: 1px solid #fecaca;
        text-align: center;
    }

    .info-text {
        color: #64748b;
        font-size: 13px;
        margin-top: 5px;
    }

    .choice-inputs {
        display: grid;
        grid-template-columns: 1fr 1fr;
        gap: 10px;
        margin-bottom: 10px;
    }
</style>
</head>
<body>
<%@include file="header.jsp" %>
<div class="container">
    <div class="back-button">
        <a href="main.jsp">← Нүүр хуудас руу буцах</a>
    </div>

    <div class="create-form">
        <h2>Шинэ Quiz Үүсгэх</h2>

        <% if (request.getAttribute("error") != null) { %>
            <div class="error-message">
                <%= request.getAttribute("error") %>
            </div>
        <% } %>

        <form action="quiz" method="post" id="quizForm" accept-charset="UTF-8">
            <input type="hidden" name="action" value="create">

            <!-- Quiz мэдээлэл -->
            <div class="form-section">
                <div class="section-title">Quiz-ийн үндсэн мэдээлэл</div>

                <div class="form-group">
                    <label for="title">Quiz-ийн нэр: *</label>
                    <input type="text" id="title" name="title"
                           placeholder="Жишээ: Монгол улсын түүх"
                           required>
                </div>

                <div class="form-group">
                    <label for="user">Үүсгэгч: *</label>
                    <input type="text" id="user" name="user"
                           value="<%= session.getAttribute("username") != null ? session.getAttribute("username") : "" %>"
                           readonly>
                </div>

                <div class="form-group">
                    <label for="time">Хугацаа (минут): *</label>
                    <input type="number" id="time" name="time"
                           placeholder="Жишээ: 30"
                           min="1"
                           required>
                    <p class="info-text">Quiz бөглөх хугацааг минутаар оруулна уу</p>
                </div>
            </div>

            <!-- Асуултууд -->
            <div class="form-section">
                <div class="section-title">Асуултууд</div>
                <div id="questionsContainer"></div>

                <div class="add-question-btns">
                    <button type="button" class="add-btn" onclick="addMCQ()">+ Сонгох асуулт (MCQ) нэмэх</button>
                    <button type="button" class="add-btn" onclick="addSAQ()">+ Богино хариулт (SAQ) нэмэх</button>
                </div>
            </div>

            <button type="submit" class="submit-btn">Quiz үүсгэх →</button>
        </form>
    </div>
</div>

<script>
let questionCount = 0;

function addQuestion(type) {
    questionCount++;
    const container = document.getElementById('questionsContainer');
    const questionDiv = document.createElement('div');
    questionDiv.className = 'question-item';
    questionDiv.id = 'question-' + questionCount;
    questionDiv.setAttribute('data-index', questionCount);
    questionDiv.setAttribute('data-type', type);

    let html = '';
    html += '<div class="question-header">';
    html += '<span class="question-number">Асуулт ' + questionCount + ' (' + type.toUpperCase() + ')</span>';
    html += '<button type="button" class="remove-btn" onclick="removeQuestion(' + questionCount + ')">Устгах</button>';
    html += '</div>';
    
    // Hidden input for type
    html += '<input type="hidden" name="questionType_' + questionCount + '" value="' + type + '">';
    
    html += '<div class="form-group">';
    html += '<label>Асуулт: *</label>';
    html += '<textarea name="question_' + questionCount + '" required placeholder="Асуултаа энд бичнэ үү"></textarea>';
    html += '</div>';

    if(type === 'mcq'){
        html += '<div class="form-group">';
        html += '<label>Сонголтууд:</label>';
        html += '<div class="choice-inputs">';
        html += '<input type="text" name="choice1_' + questionCount + '" placeholder="Сонголт 1" required>';
        html += '<input type="text" name="choice2_' + questionCount + '" placeholder="Сонголт 2" required>';
        html += '<input type="text" name="choice3_' + questionCount + '" placeholder="Сонголт 3" required>';
        html += '<input type="text" name="choice4_' + questionCount + '" placeholder="Сонголт 4" required>';
        html += '</div>';
        html += '</div>';
    }

    html += '<div class="form-group">';
    html += '<label>Зөв хариулт: *</label>';
    html += '<input type="text" name="answer_' + questionCount + '" required placeholder="Зөв хариултыг бичнэ үү">';
    html += '</div>';
    
    html += '<div class="form-group">';
    html += '<label>Оноо: *</label>';
    html += '<input type="number" name="point_' + questionCount + '" min="1" value="1" required>';
    html += '</div>';

    questionDiv.innerHTML = html;
    container.appendChild(questionDiv);
    
    console.log('✅ Асуулт нэмэгдлээ: ' + questionCount + ', Type: ' + type);
}

function removeQuestion(index) {
    if(confirm('Энэ асуултыг устгах уу?')) {
        const element = document.getElementById('question-' + index);
        if(element) {
            element.remove();
            console.log('❌ Асуулт устгагдлаа: ' + index);
        }
    }
}

function addMCQ() { 
    addQuestion('mcq'); 
}

function addSAQ() { 
    addQuestion('saq'); 
}

// Form submit
document.getElementById('quizForm').addEventListener('submit', function(e) {
    e.preventDefault();
    
    console.log('');
    console.log('========================================');
    console.log('📝 FORM SUBMIT ЭХЭЛЖ БАЙНА');
    console.log('========================================');
    
    const container = document.getElementById('questionsContainer');
    const allQuestions = Array.from(container.children);
    const actualCount = allQuestions.length;
    
    console.log('📊 Container дахь асуултын тоо: ' + actualCount);
    
    if(actualCount === 0) {
        alert('⚠️ Хамгийн багадаа 1 асуулт нэмнэ үү!');
        return false;
    }
    
    // Асуултуудыг дахин дугаарлах
    console.log('🔄 Асуултуудыг дахин дугаарлаж байна...');
    
    allQuestions.forEach((questionDiv, index) => {
        const newIndex = index + 1;
        const type = questionDiv.getAttribute('data-type');
        
        console.log('');
        console.log('  Асуулт #' + newIndex + ' (Төрөл: ' + type + ')');
        
        // Бүх input болон textarea олох
        const allInputs = questionDiv.querySelectorAll('input, textarea');
        
        allInputs.forEach(input => {
            const oldName = input.getAttribute('name');
            if(oldName && oldName.includes('_')) {
                const parts = oldName.split('_');
                const prefix = parts[0];
                const newName = prefix + '_' + newIndex;
                
                input.setAttribute('name', newName);
                console.log('    ✏️  ' + oldName + ' → ' + newName + ' (value: "' + input.value + '")');
            }
        });
    });
    
    // questionCount нэмэх
    let countInput = this.querySelector('input[name="questionCount"]');
    if(!countInput) {
        countInput = document.createElement('input');
        countInput.type = 'hidden';
        countInput.name = 'questionCount';
        this.appendChild(countInput);
    }
    countInput.value = actualCount;
    
    console.log('');
    console.log('✅ questionCount = ' + actualCount);
    console.log('');
    console.log('📤 Form-ыг server лүү илгээж байна...');
    console.log('========================================');
    console.log('');
    
    // Debug: Form data-г харуулах
    const formData = new FormData(this);
    console.log('📋 Form Data:');
    for(let pair of formData.entries()) {
        console.log('   ' + pair[0] + ' = ' + pair[1]);
    }
    console.log('');
    
    // Submit
    this.submit();
});
</script>

<%@include file="footer.jsp" %>
</body>
</html>