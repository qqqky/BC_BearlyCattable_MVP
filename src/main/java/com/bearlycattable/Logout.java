package com.bearlycattable;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Logout
 */
@WebServlet("/Logout")
public class Logout extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
   
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String query = request.getQueryString();
		System.out.println("GET @Logout: "+query);
		
		RequestDispatcher dispatcher = request.getRequestDispatcher("RefreshCust");
		request.getSession().invalidate();
		request.getSession(true).setAttribute("id", "-1");
		dispatcher.forward(request, response);
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String query = request.getQueryString();
		System.out.println("POST @Logout: "+query);
		
		RequestDispatcher dispatcher = request.getRequestDispatcher("RefreshCust");
		request.getSession().invalidate();
		request.getSession(true).setAttribute("id", "-1");
		dispatcher.forward(request, response);
	}

}
