package com.quizapp.Servlets;

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.quizapp.DAO.*;
import com.quizapp.Model.*; 

@WebServlet("/quiz") 
public class QuizServlet extends HttpServlet {
    private QuizDAO quizDAO; 

    @Override 
    public void init() throws ServletException {
        quizDAO = new QuizDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {

        String action = request.getParameter("action");

        if (action == null || action.isEmpty()) {
            response.sendRedirect("main.jsp");
            return;
        }

        try {
            switch (action) {
                case "result":
                    getResult(request, response);
                    break;
                case "answer":
                    getAnswer(request, response);
                    break;
                default:
                    response.sendRedirect("main.jsp");
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {

        String action = request.getParameter("action");

        if (action == null || action.isEmpty()) {
            response.sendRedirect("main.jsp");
            return;
        }

        try {
            switch (action) {
                case "create":
                    createQuiz(request, response);
                    break;
                case "submit":
                    submitQuiz(request, response);
                    break;
                case "delete-result":
                    deleteResult(request, response);
                    deleteAnswer(request, response);
                    break;
                case "update-quiz":
                	updateQuiz(request, response);
                	break;
                case "delete-quiz":
                	deleteQuiz(request, response);
                	break;
                default:
                    response.sendRedirect("main.jsp");
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createQuiz(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        String username = (String) session.getAttribute("username");

        // Хэрэглэгч нэвтрээгүй бол login хуудас руу шилжүүлэх
        if (username == null || username.trim().isEmpty()) {
            response.sendRedirect("login.jsp");
            return;
        }

        String title = request.getParameter("title");
        String user = request.getParameter("user");
        String timeStr = request.getParameter("time");
        String questionCountStr = request.getParameter("questionCount");

        // Бүх талбаруудын validation
        if (title == null || title.trim().isEmpty() ||
            user == null || user.trim().isEmpty() ||
            timeStr == null || timeStr.trim().isEmpty()) {
            request.setAttribute("error", "Бүх талбарыг бөглөнө үү!");
            request.getRequestDispatcher("create.jsp").forward(request, response);
            return;
        }

        try {
            int time = Integer.parseInt(timeStr);

            // Цаг сөрөг эсвэл 0 байж болохгүй
            if (time <= 0) {
                request.setAttribute("error", "Цаг эерэг тоо байх ёстой!");
                request.getRequestDispatcher("create.jsp").forward(request, response);
                return;
            }

            // Quiz үүсгэх
            Quiz quiz = new Quiz(title, user, time);
            int result = quizDAO.createQuiz(quiz);

            if (result > 0) {
                System.out.println("DEBUG: Quiz үүслээ. Quiz ID = " + quiz.getId());
                
                // Асуултуудыг нэмэх
                if (questionCountStr != null && !questionCountStr.trim().isEmpty()) {
                    int questionCount = Integer.parseInt(questionCountStr);
                    System.out.println("DEBUG: Нийт асуултын тоо = " + questionCount);
                    
                    int addedQuestions = 0;
                    List<String> errors = new ArrayList<>();
                    
                    for (int i = 1; i <= questionCount; i++) {
                        String questionType = request.getParameter("questionType_" + i);
                        
                        if (questionType == null) {
                            System.out.println("DEBUG: Асуулт " + i + " - questionType null байна");
                            continue;
                        }
                        
                        String question = request.getParameter("question_" + i);
                        String answer = request.getParameter("answer_" + i);
                        String pointStr = request.getParameter("point_" + i);
                        
                        // Параметрүүдийн validation
                        if (question == null || question.trim().isEmpty()) {
                            errors.add("Асуулт " + i + " - Асуулт хоосон байна");
                            System.out.println("DEBUG: Асуулт " + i + " - question хоосон байна");
                            continue;
                        }
                        
                        if (answer == null || answer.trim().isEmpty()) {
                            errors.add("Асуулт " + i + " - Хариулт хоосон байна");
                            System.out.println("DEBUG: Асуулт " + i + " - answer хоосон байна");
                            continue;
                        }
                        
                        if (pointStr == null || pointStr.trim().isEmpty()) {
                            errors.add("Асуулт " + i + " - Оноо хоосон байна");
                            System.out.println("DEBUG: Асуулт " + i + " - point хоосон байна");
                            continue;
                        }
                        
                        int point;
                        try {
                            point = Integer.parseInt(pointStr);
                            if (point <= 0) {
                                errors.add("Асуулт " + i + " - Оноо эерэг тоо байх ёстой");
                                continue;
                            }
                        } catch (NumberFormatException e) {
                            errors.add("Асуулт " + i + " - Оноо буруу форматтай");
                            continue;
                        }
                        
                        if ("mcq".equals(questionType)) {
                            // MCQ асуулт нэмэх
                            String choice1 = request.getParameter("choice1_" + i);
                            String choice2 = request.getParameter("choice2_" + i);
                            String choice3 = request.getParameter("choice3_" + i);
                            String choice4 = request.getParameter("choice4_" + i);
                            
                            // Сонголтуудын validation
                            if (choice1 == null || choice1.trim().isEmpty() ||
                                choice2 == null || choice2.trim().isEmpty() ||
                                choice3 == null || choice3.trim().isEmpty() ||
                                choice4 == null || choice4.trim().isEmpty()) {
                                
                                errors.add("Асуулт " + i + " - Бүх сонголтуудыг бөглөнө үү");
                                System.out.println("DEBUG: MCQ " + i + " - Сонголтууд дутуу байна");
                                continue;
                            }
                            
                            try {
                                // MCQ object үүсгэх - quiz_id, question, choices, answer, point, order
                                MCQ mcq = new MCQ(quiz.getId(), question, choice1, 
                                                choice2, choice3, choice4, 
                                                answer, point, i);
                                
                                int mcqResult = quizDAO.addMCQ(quiz, mcq);
                                
                                if (mcqResult > 0) {
                                    addedQuestions++;
                                    System.out.println("DEBUG: MCQ " + i + " амжилттай нэмэгдлээ. Result = " + mcqResult);
                                } else {
                                    errors.add("Асуулт " + i + " - Database-д нэмэхэд алдаа гарлаа");
                                    System.out.println("DEBUG: MCQ " + i + " - Нэмэхэд алдаа гарлаа. Result = " + mcqResult);
                                }
                                
                            } catch (Exception e) {
                                errors.add("Асуулт " + i + " - Үүсгэхэд алдаа: " + e.getMessage());
                                System.out.println("DEBUG: MCQ " + i + " - Exception: " + e.getMessage());
                                e.printStackTrace();
                            }
                        } 
                        else if ("saq".equals(questionType)) {
                            // SAQ асуулт нэмэх
                            try {
                                // SAQ object үүсгэх - quiz_id, question, answer, point, order
                                SAQ saq = new SAQ(quiz.getId(), question, answer, point, i);
                                
                                int saqResult = quizDAO.addSAQ(quiz, saq);
                                
                                if (saqResult > 0) {
                                    addedQuestions++;
                                    System.out.println("DEBUG: SAQ " + i + " амжилттай нэмэгдлээ. Result = " + saqResult);
                                } else {
                                    errors.add("Асуулт " + i + " - Database-д нэмэхэд алдаа гарлаа");
                                    System.out.println("DEBUG: SAQ " + i + " - Нэмэхэд алдаа гарлаа. Result = " + saqResult);
                                }
                                
                            } catch (Exception e) {
                                errors.add("Асуулт " + i + " - Үүсгэхэд алдаа: " + e.getMessage());
                                System.out.println("DEBUG: SAQ " + i + " - Exception: " + e.getMessage());
                                e.printStackTrace();
                            }
                        } else {
                            errors.add("Асуулт " + i + " - Асуултын төрөл тодорхойгүй: " + questionType);
                            System.out.println("DEBUG: Асуулт " + i + " - Тодорхойгүй төрөл: " + questionType);
                        }
                    }
                    
                    System.out.println("DEBUG: Нийт " + addedQuestions + " асуулт амжилттай нэмэгдлээ");
                    
                    // Алдаануудыг харуулах
                    if (!errors.isEmpty()) {
                        System.out.println("DEBUG: ========== АЛДААНУУД ==========");
                        for (String error : errors) {
                            System.out.println("DEBUG: " + error);
                        }
                        System.out.println("DEBUG: ================================");
                        
                        // Хэрэв ямар нэг асуулт нэмэгдсэн бол warning, үгүй бол error
                        if (addedQuestions > 0) {
                            request.setAttribute("warning", "Quiz үүслээ гэхдээ зарим асуултууд нэмэгдсэнгүй. Дэлгэрэнгүй: " + String.join(", ", errors));
                        } else {
                            request.setAttribute("error", "Quiz үүслээ гэхдээ ямар ч асуулт нэмэгдсэнгүй! Дэлгэрэнгүй: " + String.join(", ", errors));
                        }
                    }
                    
                } else {
                    System.out.println("DEBUG: questionCount параметр байхгүй эсвэл хоосон байна");
                    request.setAttribute("warning", "Quiz үүслээ гэхдээ асуулт нэмэгдсэнгүй!");
                }
                                
                request.setAttribute("message", quiz.getTitle() + " created successfully");
                request.getRequestDispatcher("main.jsp").forward(request, response); 
                
            } else {
                request.setAttribute("error", "Quiz үүсгэхэд алдаа гарлаа! Database-д хадгалагдсангүй.");
                request.getRequestDispatcher("error.jsp").forward(request, response);
            }
            
        } catch (NumberFormatException e) {
            e.printStackTrace();
            request.setAttribute("error", "Тоон утгуудыг зөв оруулна уу! (" + e.getMessage() + ")");
            request.getRequestDispatcher("create.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Алдаа гарлаа: " + e.getMessage());
            request.getRequestDispatcher("create.jsp").forward(request, response);
        }
    }

    private void submitQuiz(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
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

        quizTitle = java.net.URLDecoder.decode(quizTitle, "UTF-8");

        Quiz quiz = new Quiz();
        quizDAO.fetchQuizDataByTitle(quiz, quizTitle);
        quizDAO.fetchQuestions(quiz, quiz.getId());

        AnswerDAO answerDAO = new AnswerDAO();
        ResultDAO resultDAO = new ResultDAO();

        List<String> userAnswers = new ArrayList<>();
        List<Question> questions = quiz.getQuestions();

        for (Question q : questions) {
            String ans = request.getParameter("answer_" + q.getOrder());
            if (ans == null) ans = "";
            else ans = java.net.URLDecoder.decode(ans, "UTF-8");
            userAnswers.add(ans);

            System.out.println("DEBUG: Question ID = " + q.getId() +
                               ", Order = " + q.getOrder() +
                               ", User Answer = '" + ans + "'");

            answerDAO.saveUserAnswer(username, q.getId(), ans);
        }

        int totalScore = quiz.calculateScore(userAnswers);
        System.out.println("DEBUG: Total Score = " + totalScore);

        resultDAO.saveResult(quiz.getId(), username, totalScore);

        request.setAttribute("message", quizTitle + " submitted. Check your Result from Result section");
        request.getRequestDispatcher("main.jsp").forward(request, response); 
    }

    private void updateQuiz(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        String username = (String) session.getAttribute("username");

        if (username == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String quizIdStr = request.getParameter("quizId");
        String quizTitle = request.getParameter("quizTitle");
        String questionCountStr = request.getParameter("questionCount");

        if (quizIdStr == null || questionCountStr == null) {
            request.setAttribute("message", "Error occured");
            request.getRequestDispatcher("main.jsp").forward(request, response);
            return;
        }

        try {
            int quizId = Integer.parseInt(quizIdStr);
            int questionCount = Integer.parseInt(questionCountStr);
            
            System.out.println("DEBUG: Quiz засварлаж байна. Quiz ID = " + quizId);
            System.out.println("DEBUG: Нийт асуултын тоо = " + questionCount);

            int updatedCount = 0;
            int addedCount = 0;
            List<String> errors = new ArrayList<>();

            for (int i = 1; i <= questionCount; i++) {
                // Асуулт устгагдсан эсэхийг шалгах - form-оос ирээгүй бол skip
                String questionType = request.getParameter("questionType_" + i);
                if (questionType == null || questionType.trim().isEmpty()) {
                    System.out.println("DEBUG: Асуулт " + i + " устгагдсан буюу хоосон байна");
                    continue;
                }

                String question = request.getParameter("question_" + i);
                String answer = request.getParameter("answer_" + i);
                String pointStr = request.getParameter("point_" + i);
                String isExisting = request.getParameter("isExisting_" + i);

                // Validation
                if (question == null || question.trim().isEmpty()) {
                    errors.add("Асуулт " + i + " - Question is empty");
                    continue;
                }

                if (answer == null || answer.trim().isEmpty()) {
                    errors.add("Асуулт " + i + " - Answer is empty");
                    continue;
                }

                if (pointStr == null || pointStr.trim().isEmpty()) {
                    errors.add("Асуулт " + i + " - Point is empty");
                    continue;
                }

                int point;
                try {
                    point = Integer.parseInt(pointStr);
                    if (point <= 0) {
                        errors.add("Question " + i + " - Point can't be negative number");
                        continue;
                    }
                } catch (NumberFormatException e) {
                    errors.add("Question " + i + " - Wrong formatted");
                    continue;
                }

                if ("mcq".equals(questionType)) {
                    // MCQ асуулт
                    String choice1 = request.getParameter("choice1_" + i);
                    String choice2 = request.getParameter("choice2_" + i);
                    String choice3 = request.getParameter("choice3_" + i);
                    String choice4 = request.getParameter("choice4_" + i);

                    if (choice1 == null || choice1.trim().isEmpty() ||
                        choice2 == null || choice2.trim().isEmpty() ||
                        choice3 == null || choice3.trim().isEmpty() ||
                        choice4 == null || choice4.trim().isEmpty()) {
                        
                        errors.add("Асуулт " + i + " - Бүх сонголтуудыг бөглөнө үү");
                        continue;
                    }

                    try {
                        if ("true".equals(isExisting)) {
                            // Одоо байгаа асуултыг засварлах
                            String questionIdStr = request.getParameter("questionId_" + i);
                            if (questionIdStr != null && !questionIdStr.trim().isEmpty()) {
                                int questionId = Integer.parseInt(questionIdStr);
                                
                                // MCQ constructor ашиглан үүсгэх
                                MCQ mcq = new MCQ(questionId, question, choice1, choice2, 
                                                choice3, choice4, answer, point, i);

                                int result = quizDAO.editMCQ(mcq);
                                if (result > 0) {
                                    updatedCount++;
                                    System.out.println("DEBUG: MCQ " + i + " амжилттай засварлагдлаа");
                                } else {
                                    errors.add("Асуулт " + i + " - Засварлахад алдаа гарлаа");
                                }
                            }
                        } else {
                            // Шинэ асуулт нэмэх - quiz_id дамжуулах хэрэггүй, constructor-д байна
                            MCQ mcq = new MCQ(quizId, question, choice1, choice2, 
                                            choice3, choice4, answer, point, i);
                            
                            Quiz quiz = new Quiz();
                            quiz.setId(quizId);

                            int result = quizDAO.addMCQ(quiz, mcq);
                            if (result > 0) {
                                addedCount++;
                                System.out.println("DEBUG: Шинэ MCQ " + i + " амжилттай нэмэгдлээ");
                            } else {
                                errors.add("Асуулт " + i + " - Нэмэхэд алдаа гарлаа");
                            }
                        }
                    } catch (Exception e) {
                        errors.add("Асуулт " + i + " - " + e.getMessage());
                        e.printStackTrace();
                    }

                } else if ("saq".equals(questionType)) {
                    // SAQ асуулт
                    try {
                        if ("true".equals(isExisting)) {
                            // Одоо байгаа асуултыг засварлах
                            String questionIdStr = request.getParameter("questionId_" + i);
                            if (questionIdStr != null && !questionIdStr.trim().isEmpty()) {
                                int questionId = Integer.parseInt(questionIdStr);
                                
                                // SAQ constructor ашиглан үүсгэх
                                SAQ saq = new SAQ(questionId, question, answer, point, i);

                                int result = quizDAO.editSAQ(saq);
                                if (result > 0) {
                                    updatedCount++;
                                    System.out.println("DEBUG: SAQ " + i + " амжилттай засварлагдлаа");
                                } else {
                                    errors.add("Асуулт " + i + " - Засварлахад алдаа гарлаа");
                                }
                            }
                        } else {
                            // Шинэ асуулт нэмэх
                            Quiz quiz = new Quiz();
                            quiz.setId(quizId);
                            
                            SAQ saq = new SAQ(quizId, question, answer, point, i);

                            int result = quizDAO.addSAQ(quiz, saq);
                            if (result > 0) {
                                addedCount++;
                                System.out.println("DEBUG: Шинэ SAQ " + i + " амжилттай нэмэгдлээ");
                            } else {
                                errors.add("Асуулт " + i + " - Нэмэхэд алдаа гарлаа");
                            }
                        }
                    } catch (Exception e) {
                        errors.add("Асуулт " + i + " - " + e.getMessage());
                        e.printStackTrace();
                    }
                }
            }

            // Үр дүнг харуулах
            System.out.println("DEBUG: Засварласан асуулт: " + updatedCount);
            System.out.println("DEBUG: Нэмсэн асуулт: " + addedCount);

            if (!errors.isEmpty()) {
                System.out.println("DEBUG: ========== АЛДААНУУД ==========");
                for (String error : errors) {
                    System.out.println("DEBUG: " + error);
                }
                
                if (updatedCount > 0 || addedCount > 0) {
                    request.setAttribute("warning", 
                        "Зарим өөрчлөлт хадгалагдлаа. " + 
                        "Засварласан: " + updatedCount + ", " +
                        "Нэмсэн: " + addedCount + ". " +
                        "Алдаа: " + String.join(", ", errors));
                } else {
                    request.setAttribute("error", 
                        "Ямар ч өөрчлөлт хадгалагдсангүй! " +
                        "Алдаа: " + String.join(", ", errors));
                }
            } else {
                String message = quizTitle + " амжилттай шинэчлэгдлээ! ";
                if (updatedCount > 0) message += "Засварласан: " + updatedCount + " ";
                if (addedCount > 0) message += "Нэмсэн: " + addedCount;
                
                request.setAttribute("message", message);
            }

            request.getRequestDispatcher("main.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            e.printStackTrace();
            request.setAttribute("error", "Тоон утгууд буруу байна: " + e.getMessage());
            request.getRequestDispatcher("edit.jsp?title=" + quizTitle).forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Алдаа гарлаа: " + e.getMessage());
            request.getRequestDispatcher("edit.jsp?title=" + quizTitle).forward(request, response);
        }
    }

    private void getResult(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        int id = (int) session.getAttribute("id");
        String username = (String) session.getAttribute("username");

        ResultDAO resultDAO = new ResultDAO();
        Result result = resultDAO.getResult(username, id);
        session.setAttribute("result", result);
    }

    private void deleteResult(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Form-аас ирсэн parameter-уудыг авах
        String quizIdStr = request.getParameter("quizId");
        String username = request.getParameter("username");

        if (quizIdStr == null || username == null) {
            return;
        }

        int quizId = Integer.parseInt(quizIdStr);

        ResultDAO resultDAO = new ResultDAO();
        resultDAO.deleteResult(quizId, username);
    }

    private void deleteAnswer(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String quizIdStr = request.getParameter("quizId");
        String username = request.getParameter("username");

        if (quizIdStr == null || username == null) {
            return;
        }

        int quizId = Integer.parseInt(quizIdStr);

        AnswerDAO answerDAO = new AnswerDAO();
        answerDAO.deleteAnswer(username, quizId);

        RequestDispatcher dispatcher = request.getRequestDispatcher("submit.jsp");
        dispatcher.forward(request, response);
    }

    private void getAnswer(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        int id = (int) session.getAttribute("id");
        String username = (String) session.getAttribute("username");

        AnswerDAO answerDAO = new AnswerDAO();
        List<String> userAnswers = answerDAO.getUserAnswers(username, id);
        session.setAttribute("userAnswers", userAnswers);
    }
    
    private void deleteQuiz(HttpServletRequest request, HttpServletResponse response) 
    	throws ServletException, IOException {
    	
    	Quiz removedQuiz = new Quiz() ;
    	QuizDAO quizDAO = new QuizDAO() ;
    	AnswerDAO answerDAO = new AnswerDAO() ;
    	ResultDAO resultDAO = new ResultDAO() ;
    	
    	String title = (String) request.getParameter("title") ;
    	
    	System.out.println(title) ;
    	
    	quizDAO.fetchQuizDataByTitle(removedQuiz, title);
    	quizDAO.fetchQuestions(removedQuiz, removedQuiz.getId()); 
    	
    	if(quizDAO.deleteQuiz(removedQuiz) > 0) {
        	request.setAttribute("message", title + " deleted sucessfully");
	    	answerDAO.deleteAnswer(title, removedQuiz.getId());
	    	resultDAO.deleteResult(removedQuiz.getId(), removedQuiz.getUser());
    	}
    	else 
    		request.setAttribute("message", "Trouble occured. Please try again");
    	
    	request.getRequestDispatcher("main.jsp").forward(request, response); 
     }
}
