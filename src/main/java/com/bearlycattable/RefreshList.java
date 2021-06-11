package com.bearlycattable;

import java.io.IOException;
import java.time.LocalTime;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import app.BCController;
import dto.CmdRetrieveCustomerList;
import dto.ResponseCustList;

/**
 * Servlet implementation class RefreshList
 */
@WebServlet("/RefreshList")
public class RefreshList extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private BCController controller = BCController.getInstance();
       
   
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String query = request.getQueryString();
		System.out.println("GET @RefreshList: "+query);
		
		Object id = request.getSession(false).getAttribute("id");
		
		if(id == null || (int)id < 0) {
			request.setAttribute("feedback", "Your session has expired. This action requires login");
			request.getRequestDispatcher("RefreshCust").forward(request, response);	
		}
		//System.out.println("Retrieved id is: "+id);
		
		ResponseCustList custList = (ResponseCustList)controller.setAndExecuteRequest(new CmdRetrieveCustomerList((int)id));
		
		//System.out.println("CustList retrieved is: \r\n"+custList);
		System.out.println("CustList of size ["+custList.size()+"] was retrieved in @RefreshList");
		request.setAttribute("size", custList.size());
		request.setAttribute("statuses", custList.getStatuses());
		request.setAttribute("codes", custList.getCodes());
		request.setAttribute("times", custList.getTimes());
		request.setAttribute("refresh", Boolean.valueOf(true).toString());
		request.setAttribute("updateNeeded", Boolean.valueOf(true).toString());
		
		System.out.println("codes retrieved from custList were: "+custList.getCodes());

		//update current time shown
				LocalTime current = LocalTime.now();
				String hour = current.getHour()<10? "0"+current.getHour() : ""+current.getHour();
				String minute = current.getMinute()<10? "0"+current.getMinute() : ""+current.getMinute();
				
				request.setAttribute("time", hour+":"+minute);
				System.out.println("time attribute is set to: "+hour+":"+minute);
		
		request.getRequestDispatcher("management_page.jsp").forward(request, response);	
		
	}
/*
 * Only initiated upon login (otherwise GET is used)
 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String query = request.getQueryString();
		System.out.println("POST @RefreshList: "+query);
		
		Object id = request.getSession(false).getAttribute("id");
		
		if(id == null || (int)id < 0) {
			request.setAttribute("feedback", "Your session has expired. This action requires login");
			request.getRequestDispatcher("RefreshCust").forward(request, response);	
		}
		
		ResponseCustList custList = (ResponseCustList)controller.setAndExecuteRequest(new CmdRetrieveCustomerList((int)id));
		
		request.setAttribute("size", custList.size());
		request.setAttribute("statuses", custList.getStatuses());
		request.setAttribute("codes", custList.getCodes());
		request.setAttribute("times", custList.getTimes());
		request.setAttribute("refresh", Boolean.valueOf(true).toString());
		request.setAttribute("updateNeeded", Boolean.valueOf(true).toString());
		
		
		//update current time shown
				LocalTime current = LocalTime.now();
				String hour = current.getHour()<10? "0"+current.getHour() : ""+current.getHour();
				String minute = current.getMinute()<10? "0"+current.getMinute() : ""+current.getMinute();
				
				request.setAttribute("time", hour+":"+minute);
		
		request.getRequestDispatcher("management_page.jsp").forward(request, response);	
		
		}

}
