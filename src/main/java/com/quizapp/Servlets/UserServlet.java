package com.quizapp.Servlets;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.quizapp.DAO.UserDAO; 
import com.quizapp.DAO.QuizDAO;
import com.quizapp.Model.User;
import com.quizapp.Model.Quiz;

@WebServlet("/user") 
public class UserServlet extends HttpServlet {

	private UserDAO userDAO; 
	
	@Override
	public void init() throws ServletException {
		userDAO = new UserDAO();
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		
		String action = request.getParameter("action");
		
		if(action == null || action.isEmpty()) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Action parameter is missing");
			return; 
		}
		
		switch(action) {
			case "register":
				handleRegister(request, response);
				break; 
			case "login":
				handleLogin(request, response);
				break; 
			case "reset":
				handleResetPassword(request, response);
				break; 
			default:
				response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action parameter");
				break; 
		}
	}
	
	private void handleLogin(HttpServletRequest request, HttpServletResponse response) 
		throws IOException, ServletException {
				
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		
		boolean valid = userDAO.login(username, password);

		if(valid) {
			HttpSession session = request.getSession();
			session.setAttribute("username", username);
			
			List<Quiz> quizzes ;
			
			QuizDAO dao = new QuizDAO() ;
			
			quizzes = dao.fetchQuizData() ;
			
			for(Quiz quiz: quizzes) {
				System.out.println(quiz.getTitle()); 
			}
			
			request.setAttribute("quizzes", quizzes); 
		    request.getRequestDispatcher("/main.jsp").forward(request, response);
		}
		
		else {
			request.setAttribute("loginError", "Username or Password is invalid");
			request.getRequestDispatcher("/login.jsp").forward(request, response);
		}
	}
	
	private void handleResetPassword(HttpServletRequest request, HttpServletResponse response)
		throws IOException, ServletException {
		
		String username = request.getParameter("username");
		String newPassword = request.getParameter("new-password");
		String email = request.getParameter("email");
		String number = request.getParameter("number");
		
		System.out.println(username + " " + newPassword + " " + email + " " + number + " ") ;
		
		int valid = userDAO.resetPassword(username, email, number, newPassword);
		
		if(valid > 0) {
			request.setAttribute("resetSuccess", "Password changed successfully!");
			request.getRequestDispatcher("login.jsp").forward(request, response);
		}
		else {
			request.setAttribute("resetError", "Unable to change password. Check your credentials again");
			request.getRequestDispatcher("reset-password.jsp").forward(request, response);
		}
	}
	
	private void handleRegister(HttpServletRequest request, HttpServletResponse response)
		throws IOException, ServletException {
		
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String email = request.getParameter("email");
		String number = request.getParameter("number");
		
		User newUser = new User(username, password, email, number);
		
		boolean valid = userDAO.register(newUser);
		
		if(valid) {
			request.setAttribute("registerSuccess", "Successfully registered! Please login");
			request.getRequestDispatcher("login.jsp").forward(request, response);
		}
		else {
			request.setAttribute("registerError", "Username already exists. Please try another username");
			request.getRequestDispatcher("login.jsp").forward(request, response);
		}
	}
}