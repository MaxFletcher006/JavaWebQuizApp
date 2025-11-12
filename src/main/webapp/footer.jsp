<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<style>
    .quiz-footer {
        background: linear-gradient(135deg, #0D1B2A 0%, #1B263B 100%); /* Хар хөх gradient */
        padding: 15px 30px;
        box-shadow: 0 -4px 15px rgba(0, 0, 0, 0.3);
        position: fixed;
        bottom: 0;
        left: 0;
        right: 0;
        z-index: 999;
        height: 100px;
        display: flex;
        align-items: center;
        justify-content: center;
        backdrop-filter: blur(10px); /* subtle glass effect */
    }
    
    .footer-container {
        display: flex;
        flex-direction: column;
        justify-content: center;
        align-items: center;
        max-width: 1400px;
        margin: 0 auto;
        width: 100%;
        gap: 3px;
    }
    
    .project-info {
        color: #E0E0E0; 
        font-size: 14px;
        margin: 0;
        font-weight: 400;
        letter-spacing: 0.3px;
        text-shadow: 1px 1px 3px rgba(0,0,0,0.4);
    }
    
    body {
        margin: 0;
        padding-bottom: 110px; 
        font-family: 'Poppins', sans-serif;
    }
</style>


<footer class="quiz-footer">
    <div class="footer-container">
        <p class="project-info"><strong>ICSI402 - Программ Хангамжийн Хөгжүүлэлт</strong></p>
        <p class="project-info">Төсөл №2: <strong>Web Quiz App</strong></p>
        <p class="project-info">Гүйцэтгэсэн оюутан: <strong>Бямбадоржийн Баяржавхлан</strong></p>
        <p class="project-info">2025 оны Намрын Улирал</p>
    </div>
</footer>