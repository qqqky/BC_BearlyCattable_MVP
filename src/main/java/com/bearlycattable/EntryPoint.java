package com.bearlycattable;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class EntryPoint
 */
@WebServlet("/EntryPoint")
public class EntryPoint extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
   
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String query = request.getQueryString();
		System.out.println("GET @EntryPoint: "+query);
		request.getRequestDispatcher("RefreshCust").forward(request, response);
		
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String query = request.getQueryString();
		System.out.println("POST @EntryPoint: "+query);
		request.getRequestDispatcher("RefreshCust").forward(request, response);
	}

}
