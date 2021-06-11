package com.bearlycattable;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class CancelVisit
 */
@WebServlet("/CancelVisit")
public class CancelVisit extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String query = request.getQueryString();
		System.out.println("GET @CancelVisit: "+query);
		
		String code = request.getParameter("cancel-code");
		String lastName = request.getParameter("cancel-last-name");
		
		String command = "cancelVisit";
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
			request.setAttribute("cancelResp", "Wrong input format. Please check if info is correct");
			request.getRequestDispatcher("cancel_reservation_page.jsp").forward(request, response);
		}
		
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String query = request.getQueryString();
		System.out.println("POST @CancelVisit: "+query);
	}

}
