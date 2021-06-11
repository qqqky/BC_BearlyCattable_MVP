package com.bearlycattable;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 
 */
@WebServlet("/WaitingTime")
public class WaitingTime extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
   
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String query = request.getQueryString();
		System.out.println("GET @WaitingTime: "+query);
		
		String code = request.getParameter("waiting-code");
		String lastName = request.getParameter("waiting-last-name");
		
		String command = "seeWaitingTime";
		request.setAttribute("command", command);
		
		//some basic checks
		boolean allowed = false;
		if(code.isEmpty() || lastName.isEmpty()) {
			allowed = false;		
		}else if(code.length() == 3 && lastName.length() < 20) {
			allowed = true;
		}
		
		//if input OK
		if(allowed) {
			request.getRequestDispatcher("ClientView").forward(request, response);
		}else {
			request.setAttribute("waitResp", "Wrong input format. Please check if code is correct");
			request.getRequestDispatcher("waiting_time_page.jsp").forward(request, response);
		}
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String query = request.getQueryString();
		System.out.println("POST @WaitingTime: "+query);
	}

}
