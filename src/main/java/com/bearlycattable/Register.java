package com.bearlycattable;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import app.BasicInputCheck;

/**
 * Servlet implementation class Register
 */
@WebServlet("/Register")
public class Register extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String query = request.getQueryString();
		System.out.println("GET @Register: "+query);
		
		String name = request.getParameter("reg-first-name");
		String lastName = request.getParameter("reg-last-name");
		String command = "register";
		request.setAttribute("command", command);
		
		
		boolean allowed = false;
		
		/*
		 * Some basic checks for more informative feedback
		 */
		if(name.isEmpty() || lastName.isEmpty()) {
			request.setAttribute("regResp", "Error. Blank items are not allowed");
			request.setAttribute("successful", Boolean.valueOf(false).toString());
			allowed = false;		
		}else if(name.length() > 20 || lastName.length() > 20) {
			request.setAttribute("regResp", "Error. Provided input is too long");
			request.setAttribute("successful", Boolean.valueOf(false).toString());
			allowed = false;
		}else if(!BasicInputCheck.verify(name) || !BasicInputCheck.verify(lastName)) {
			request.setAttribute("regResp", "Error. Wrong input format (only English characters are allowed). Please try again");
			request.setAttribute("successful", Boolean.valueOf(false).toString());
			allowed = false;
		}else {
			allowed = true;
		}
		
		//if input OK
		if(allowed) {
			request.getRequestDispatcher("ClientView").forward(request, response);
		}else {
			request.getRequestDispatcher("registration_page.jsp").forward(request, response);
		}
		
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String query = request.getQueryString();
		System.out.println("POST @Register: "+query);
	}

}
