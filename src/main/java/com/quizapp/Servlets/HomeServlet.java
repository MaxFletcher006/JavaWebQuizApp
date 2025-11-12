package com.quizapp.Servlets;

import java.io.*;
import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse; 

@WebServlet("/")
public class HomeServlet extends HttpServlet{
	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
		throws IOException {
		response.sendRedirect(request.getContextPath() + "/login.jsp");
	}
}

