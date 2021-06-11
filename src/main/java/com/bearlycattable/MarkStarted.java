package com.bearlycattable;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import app.BCController;
import dto.CmdMarkVisitBegan;
import dto.ResponseBool;

/**
 * Servlet MarkStarted. 
 * 
 * Used when specialist wants to change visit status to ONGOING.
 * A visit can only be marked ongoing if current time vs visit time is both not
 * too late (+10mins) and not too early (-10mins)
 */
@WebServlet("/MarkStarted")
public class MarkStarted extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private BCController controller = BCController.getInstance();
    
   
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String query = request.getQueryString();
		System.out.println("GET @MarkStarted: "+query);
		
		Object check = request.getSession(false).getAttribute("id");
		
		if(check==null || (int)check < 0) {
			request.getSession().invalidate();
			request.setAttribute("feedback", "Your session has expired. This action requires login");
			request.getRequestDispatcher("RefreshCust").forward(request, response);
		}else {
			int id = (int)check;
			String time = request.getParameter("time");
		
			if(!time.isEmpty() && time.length()==4) {	//eg. 9:00 -> 09:00
				time = "0"+time;
			}
		
		String status = request.getParameter("status");
		String code = request.getParameter("code");
		
		System.out.println("Retrieved status is: "+status);
		System.out.println("Retrieved code is: "+code);
		
		
		ResponseBool resp = (ResponseBool)controller.setAndExecuteRequest(new CmdMarkVisitBegan(time, id));
		System.out.println("Response was retrieved for STARTED - it is: "+resp.getValue());
		if(resp.getValue()) {
			request.getSession(true).setAttribute("id", resp.getId());
			System.out.println("ID inside ResponseBool is: "+resp.getId());
			request.setAttribute("updateNeeded", "true");
			request.setAttribute("cancelled", "true");
			request.getRequestDispatcher("RefreshList").forward(request, response);
		}else {
			request.setAttribute("feedback", "Cannot mark the visit at time "+time+" as ONGOING");
			request.getRequestDispatcher("RefreshList").forward(request, response);
		}
	}
}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String query = request.getQueryString();
		System.out.println("POST @MarkStarted: "+query);
	}

}
