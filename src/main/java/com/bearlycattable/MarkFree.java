package com.bearlycattable;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import app.BCController;
import dto.CmdMarkVisitFree;
import dto.ResponseBool;

/**
 * Servlet implementation class MarkFree
 */
@WebServlet("/MarkFree")
public class MarkFree extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private BCController controller = BCController.getInstance();
    
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String query = request.getQueryString();
		System.out.println("GET @MarkFree: "+query);
		
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
		
		
		ResponseBool resp = (ResponseBool)controller.setAndExecuteRequest(new CmdMarkVisitFree(time, id));
		System.out.println("Response was retrieved for FREE - it is: "+resp.getValue());
		if(resp.getValue()) {
			request.getSession(true).setAttribute("id", resp.getId());
			request.setAttribute("updateNeeded", "true");
			request.setAttribute("free", "true");
			request.getRequestDispatcher("RefreshList").forward(request, response);
		}else {
			request.setAttribute("feedback", "Cannot mark the visit at time "+time+" as FREE");
			request.getRequestDispatcher("RefreshList").forward(request, response);
		}
	}
}
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String query = request.getQueryString();
		System.out.println("POST @MarkFree: "+query);
	}

}
