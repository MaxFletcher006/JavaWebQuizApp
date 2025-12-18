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
        
        // Set UTF-8 encoding for all requests
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

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
        
        // Set UTF-8 encoding for all requests
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

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

        // Check if user is logged in
        if (username == null || username.trim().isEmpty()) {
            response.sendRedirect("login.jsp");
            return;
        }

        String title = request.getParameter("title");
        String user = request.getParameter("user");
        String timeStr = request.getParameter("time");
        String questionCountStr = request.getParameter("questionCount");

        // Validate all required fields
        if (title == null || title.trim().isEmpty() ||
            user == null || user.trim().isEmpty() ||
            timeStr == null || timeStr.trim().isEmpty()) {
            request.setAttribute("error", "Please fill in all required fields!");
            request.getRequestDispatcher("create.jsp").forward(request, response);
            return;
        }

        try {
            int time = Integer.parseInt(timeStr);

            // Time must be positive
            if (time <= 0) {
                request.setAttribute("error", "Time must be a positive number!");
                request.getRequestDispatcher("create.jsp").forward(request, response);
                return;
            }

            // Create Quiz
            Quiz quiz = new Quiz(title, user, time);
            int result = quizDAO.createQuiz(quiz);

            if (result > 0) {
                System.out.println("DEBUG: Quiz created. Quiz ID = " + quiz.getId());

                // Add questions
                if (questionCountStr != null && !questionCountStr.trim().isEmpty()) {
                    int questionCount = Integer.parseInt(questionCountStr);
                    System.out.println("DEBUG: Total questions = " + questionCount);

                    int addedQuestions = 0;
                    List<String> errors = new ArrayList<>();

                    for (int i = 1; i <= questionCount; i++) {
                        String questionType = request.getParameter("questionType_" + i);

                        if (questionType == null) {
                            System.out.println("DEBUG: Question " + i + " - questionType is null");
                            continue;
                        }

                        String question = request.getParameter("question_" + i);
                        String answer = request.getParameter("answer_" + i);
                        String pointStr = request.getParameter("point_" + i);

                        // Validation
                        if (question == null || question.trim().isEmpty()) {
                            errors.add("Question " + i + " - Question is empty");
                            continue;
                        }

                        if (answer == null || answer.trim().isEmpty()) {
                            errors.add("Question " + i + " - Answer is empty");
                            continue;
                        }

                        if (pointStr == null || pointStr.trim().isEmpty()) {
                            errors.add("Question " + i + " - Point is empty");
                            continue;
                        }

                        int point;
                        try {
                            point = Integer.parseInt(pointStr);
                            if (point <= 0) {
                                errors.add("Question " + i + " - Point must be positive");
                                continue;
                            }
                        } catch (NumberFormatException e) {
                            errors.add("Question " + i + " - Invalid point format");
                            continue;
                        }

                        if ("mcq".equalsIgnoreCase(questionType)) {
                            // Add MCQ question
                            String choice1 = request.getParameter("choice1_" + i);
                            String choice2 = request.getParameter("choice2_" + i);
                            String choice3 = request.getParameter("choice3_" + i);
                            String choice4 = request.getParameter("choice4_" + i);

                            if (choice1 == null || choice1.trim().isEmpty() ||
                                choice2 == null || choice2.trim().isEmpty() ||
                                choice3 == null || choice3.trim().isEmpty() ||
                                choice4 == null || choice4.trim().isEmpty()) {
                                errors.add("Question " + i + " - Please fill in all choices");
                                continue;
                            }

                            MCQ mcq = new MCQ(
                                quiz.getId(), question,
                                choice1, choice2, choice3, choice4,
                                answer, point, i
                            );

                            int mcqResult = quizDAO.addMCQ(quiz, mcq);
                            if (mcqResult > 0) {
                                addedQuestions++;
                                System.out.println("DEBUG: MCQ " + i + " added successfully");
                            } else {
                                errors.add("Question " + i + " - Database error");
                            }

                        } else if ("saq".equalsIgnoreCase(questionType)) {
                            // Add SAQ question
                            SAQ saq = new SAQ(quiz.getId(), question, answer, point, i);
                            int saqResult = quizDAO.addSAQ(quiz, saq);
                            if (saqResult > 0) {
                                addedQuestions++;
                                System.out.println("DEBUG: SAQ " + i + " added successfully");
                            } else {
                                errors.add("Question " + i + " - Database error");
                            }

                        } else {
                            errors.add("Question " + i + " - Unknown question type: " + questionType);
                        }
                    }

                    System.out.println("DEBUG: Total " + addedQuestions + " questions added");

                    if (!errors.isEmpty()) {
                        if (addedQuestions > 0) {
                            request.setAttribute("warning", 
                                "Quiz created but some questions failed: " + String.join(", ", errors));
                        } else {
                            request.setAttribute("error", 
                                "Quiz created but no questions added: " + String.join(", ", errors));
                        }
                    }

                } else {
                    request.setAttribute("warning", "Quiz created but no questions added!");
                }

                request.setAttribute("message", quiz.getTitle() + " created successfully!");
                request.getRequestDispatcher("main.jsp").forward(request, response);

            } else {
                request.setAttribute("error", "Failed to create quiz. Database error.");
                request.getRequestDispatcher("error.jsp").forward(request, response);
            }

        } catch (NumberFormatException e) {
            e.printStackTrace();
            request.setAttribute("error", "Invalid number format: " + e.getMessage());
            request.getRequestDispatcher("create.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error occurred: " + e.getMessage());
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
            request.setAttribute("message", "Error occurred");
            request.getRequestDispatcher("main.jsp").forward(request, response);
            return;
        }

        try {
            int quizId = Integer.parseInt(quizIdStr);
            int questionCount = Integer.parseInt(questionCountStr);
            
            System.out.println("DEBUG: Updating quiz. Quiz ID = " + quizId);
            System.out.println("DEBUG: Total questions = " + questionCount);

            int updatedCount = 0;
            int addedCount = 0;
            int deletedCount = 0;
            List<String> errors = new ArrayList<>();

            for (int i = 1; i <= questionCount; i++) {
                String questionType = request.getParameter("questionType_" + i);
                String isExisting = request.getParameter("isExisting_" + i);
                String isDeleted = request.getParameter("deletedQuestion_" + i);
                
                System.out.println("DEBUG: ===== Question " + i + " =====");
                System.out.println("DEBUG: questionType = " + questionType);
                System.out.println("DEBUG: isExisting = " + isExisting);
                System.out.println("DEBUG: isDeleted = " + isDeleted);
                
                // Check if question is deleted
                if ("true".equals(isDeleted)) {
                    System.out.println("DEBUG: Question " + i + " is marked for deletion");
                    
                    String questionIdStr = request.getParameter("questionId_" + i);
                    String questionOrderStr = request.getParameter("questionOrder_" + i);
                    questionType = request.getParameter("questionType_" + i);
                    
                    if (questionIdStr != null && !questionIdStr.trim().isEmpty() && 
                        questionOrderStr != null && !questionOrderStr.trim().isEmpty() &&
                        questionType != null && !questionType.trim().isEmpty()) {
                        
                        try {
                            int questionQuizId = Integer.parseInt(questionIdStr);
                            int questionOrder = Integer.parseInt(questionOrderStr);
                            
                            if ("mcq".equals(questionType)) {
                                quizDAO.deleteMCQByOrder(questionQuizId, questionOrder);
                                deletedCount++;
                                System.out.println("DEBUG: MCQ question deleted (quiz_id=" + questionQuizId + ", order=" + questionOrder + ")");
                            } else if ("saq".equals(questionType)) {
                                quizDAO.deleteSAQByOrder(questionQuizId, questionOrder);
                                deletedCount++;
                                System.out.println("DEBUG: SAQ question deleted (quiz_id=" + questionQuizId + ", order=" + questionOrder + ")");
                            }
                        } catch (Exception e) {
                            errors.add("Question " + i + " deletion failed: " + e.getMessage());
                            e.printStackTrace();
                        }
                    }
                    continue;
                }
                
                questionType = request.getParameter("questionType_" + i);
                if (questionType == null || questionType.trim().isEmpty()) {
                    System.out.println("DEBUG: Question " + i + " - questionType is empty, skipping");
                    continue;
                }

                String question = request.getParameter("question_" + i);
                String answer = request.getParameter("answer_" + i);
                String pointStr = request.getParameter("point_" + i);
                isExisting = request.getParameter("isExisting_" + i);
                
                System.out.println("DEBUG: Question " + i + " - question = " + (question != null ? question.substring(0, Math.min(20, question.length())) + "..." : "null"));

                // Validation
                if (question == null || question.trim().isEmpty()) {
                    errors.add("Question " + i + " - Question is empty");
                    continue;
                }

                if (answer == null || answer.trim().isEmpty()) {
                    errors.add("Question " + i + " - Answer is empty");
                    continue;
                }

                if (pointStr == null || pointStr.trim().isEmpty()) {
                    errors.add("Question " + i + " - Point is empty");
                    continue;
                }

                int point;
                try {
                    point = Integer.parseInt(pointStr);
                    if (point <= 0) {
                        errors.add("Question " + i + " - Point can't be negative");
                        continue;
                    }
                } catch (NumberFormatException e) {
                    errors.add("Question " + i + " - Wrong format");
                    continue;
                }

                if ("mcq".equals(questionType)) {
                    String choice1 = request.getParameter("choice1_" + i);
                    String choice2 = request.getParameter("choice2_" + i);
                    String choice3 = request.getParameter("choice3_" + i);
                    String choice4 = request.getParameter("choice4_" + i);

                    if (choice1 == null || choice1.trim().isEmpty() ||
                        choice2 == null || choice2.trim().isEmpty() ||
                        choice3 == null || choice3.trim().isEmpty() ||
                        choice4 == null || choice4.trim().isEmpty()) {
                        
                        errors.add("Question " + i + " - Please fill in all choices");
                        continue;
                    }

                    try {
                        if ("true".equals(isExisting)) {
                            String questionIdStr = request.getParameter("questionId_" + i);
                            if (questionIdStr != null && !questionIdStr.trim().isEmpty()) {
                                int questionQuizId = Integer.parseInt(questionIdStr);
                                
                                MCQ mcq = new MCQ(questionQuizId, question, choice1, choice2, 
                                                choice3, choice4, answer, point, i);

                                int result = quizDAO.editMCQ(mcq);
                                if (result > 0) {
                                    updatedCount++;
                                    System.out.println("DEBUG: MCQ " + i + " updated successfully (quiz_id=" + questionQuizId + ", order=" + i + ")");
                                } else {
                                    errors.add("Question " + i + " - Update failed");
                                }
                            }
                        } else {
                            String newOrderStr = request.getParameter("newQuestionOrder_" + i);
                            int newOrder = i;
                            if (newOrderStr != null && !newOrderStr.trim().isEmpty()) {
                                try {
                                    newOrder = Integer.parseInt(newOrderStr);
                                    System.out.println("DEBUG: New question order = " + newOrder);
                                } catch (NumberFormatException e) {
                                    System.out.println("DEBUG: newQuestionOrder parse failed, using default " + i);
                                }
                            }
                            
                            MCQ mcq = new MCQ(quizId, question, choice1, choice2, 
                                            choice3, choice4, answer, point, newOrder);
                            
                            Quiz quiz = new Quiz();
                            quiz.setId(quizId);

                            int result = quizDAO.addMCQ(quiz, mcq);
                            if (result > 0) {
                                addedCount++;
                                System.out.println("DEBUG: New MCQ " + i + " added successfully (quiz_id=" + quizId + ", order=" + newOrder + ")");
                            } else {
                                errors.add("Question " + i + " - Failed to add");
                            }
                        }
                    } catch (Exception e) {
                        errors.add("Question " + i + " - " + e.getMessage());
                        e.printStackTrace();
                    }

                } else if ("saq".equals(questionType)) {
                    try {
                        if ("true".equals(isExisting)) {
                            String questionIdStr = request.getParameter("questionId_" + i);
                            if (questionIdStr != null && !questionIdStr.trim().isEmpty()) {
                                int questionQuizId = Integer.parseInt(questionIdStr);
                                
                                SAQ saq = new SAQ(questionQuizId, question, answer, point, i);

                                int result = quizDAO.editSAQ(saq);
                                if (result > 0) {
                                    updatedCount++;
                                    System.out.println("DEBUG: SAQ " + i + " updated successfully (quiz_id=" + questionQuizId + ", order=" + i + ")");
                                } else {
                                    errors.add("Question " + i + " - Update failed");
                                }
                            }
                        } else {
                            SAQ saq = new SAQ(quizId, question, answer, point, i);
                            
                            Quiz quiz = new Quiz();
                            quiz.setId(quizId);

                            int result = quizDAO.addSAQ(quiz, saq);
                            if (result > 0) {
                                addedCount++;
                                System.out.println("DEBUG: New SAQ " + i + " added successfully");
                            } else {
                                errors.add("Question " + i + " - Failed to add");
                            }
                        }
                    } catch (Exception e) {
                        errors.add("Question " + i + " - " + e.getMessage());
                        e.printStackTrace();
                    }
                }
            }

            System.out.println("DEBUG: Updated questions: " + updatedCount);
            System.out.println("DEBUG: Added questions: " + addedCount);
            System.out.println("DEBUG: Deleted questions: " + deletedCount);

            if (!errors.isEmpty()) {
                System.out.println("DEBUG: ========== ERRORS ==========");
                for (String error : errors) {
                    System.out.println("DEBUG: " + error);
                }
                
                if (updatedCount > 0 || addedCount > 0) {
                    String msg = "Some changes saved. ";
                    if (updatedCount > 0) msg += "Updated: " + updatedCount + " ";
                    if (addedCount > 0) msg += "Added: " + addedCount + " ";
                    if (deletedCount > 0) msg += "Deleted: " + deletedCount + " ";
                    msg += "Errors: " + String.join(", ", errors);
                    request.setAttribute("warning", msg);
                } else {
                    request.setAttribute("error", 
                        "No changes saved! Errors: " + String.join(", ", errors));
                }
            } else {
                String message = quizTitle + " updated successfully! ";
                if (updatedCount > 0) message += "Updated: " + updatedCount + " ";
                if (addedCount > 0) message += "Added: " + addedCount + " ";
                if (deletedCount > 0) message += "Deleted: " + deletedCount;
                
                request.setAttribute("message", message);
            }

            request.getRequestDispatcher("main.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            e.printStackTrace();
            request.setAttribute("error", "Invalid number format: " + e.getMessage());
            request.getRequestDispatcher("edit.jsp?title=" + quizTitle).forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error occurred: " + e.getMessage());
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
    	
    	Quiz removedQuiz = new Quiz();
    	QuizDAO quizDAO = new QuizDAO();
    	AnswerDAO answerDAO = new AnswerDAO();
    	ResultDAO resultDAO = new ResultDAO();
    	
    	String title = (String) request.getParameter("title");
    	
    	System.out.println(title);
    	
    	quizDAO.fetchQuizDataByTitle(removedQuiz, title);
    	quizDAO.fetchQuestions(removedQuiz, removedQuiz.getId()); 
    	
    	if(quizDAO.deleteQuiz(removedQuiz) > 0) {
        	request.setAttribute("message", title + " deleted successfully");
	    	answerDAO.deleteAnswer(title, removedQuiz.getId());
	    	resultDAO.deleteResult(removedQuiz.getId(), removedQuiz.getUser());
    	}
    	else 
    		request.setAttribute("message", "Trouble occurred. Please try again");
    	
    	request.getRequestDispatcher("main.jsp").forward(request, response); 
     }
}