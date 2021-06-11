package com.bearlycattable;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import app.BCController;
import dto.CmdGetWaitingTime;
import dto.CmdMarkVisitCancelled;
import dto.CmdRegistrationRequest;
import dto.CmdRetrieveGUIString;
import dto.ResponseBool;
import dto.ResponseGUIString;
import dto.ResponseToRegistration;
import dto.ResponseWaitTime;
import dto.SpawnCredentials;

/**
 * Servlet implementation class ClientView
 */
@WebServlet("/ClientView")
public class ClientView extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private BCController controller = BCController.getInstance();
    
   
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String query = request.getQueryString();
		System.out.println("GET @ClientView: "+query);
	
		String command = (String)request.getAttribute("command");

		if(command!=null && command.isEmpty()) {
			request.setAttribute("changed", "false");
			request.setAttribute("feedback", "Command not recognized");
			request.getRequestDispatcher("index.jsp").forward(request, response);
		}
		String code = "";
		String name = "";
		String lastName = "";
		String time = "";
		System.out.println("Current command: "+command);
		
		switch(command) {
		
			
			case "register":
				name = request.getParameter("reg-first-name");
				lastName = request.getParameter("reg-last-name");
				
				ResponseToRegistration respReg = (ResponseToRegistration)controller.setAndExecuteRequest(new CmdRegistrationRequest(name, lastName));
				time = controller.askTime();
				request.setAttribute("time", time);
				
				if(respReg.isACode()) {
					request.setAttribute("regResp", name+","+lastName+","+respReg.getCode());
					request.setAttribute("successful", Boolean.valueOf(true).toString());
				}else {
					request.setAttribute("regResp", respReg.getCode());
					request.setAttribute("successful", Boolean.valueOf(false).toString());
					}
				
				request.getRequestDispatcher("registration_page.jsp").forward(request, response);
				break;
			case "cancelVisit":
				code = request.getParameter("cancel-code");
				lastName = request.getParameter("cancel-last-name");
				
				ResponseBool respCancel = (ResponseBool)controller.setAndExecuteRequest(new CmdMarkVisitCancelled(code, lastName));
				
				if(respCancel.getValue()) {
					request.setAttribute("cancelResp", "Your visit for code "+code+" has been cancelled successfully");
				}else {
					request.setAttribute("cancelResp", "Visit for code "+code+" has NOT been cancelled (either too late or reservation does not exist)");
				}
				
				request.getRequestDispatcher("cancel_reservation_page.jsp").forward(request, response);	
				break;
			case "seeWaitingTime":
				code = request.getParameter("waiting-code");
				lastName = request.getParameter("waiting-last-name");
				
				ResponseWaitTime respWait = (ResponseWaitTime)controller.setAndExecuteRequest(new CmdGetWaitingTime(code, lastName));
				request.setAttribute("waitResp", respWait.getWaitTime());
				
				request.getRequestDispatcher("waiting_time_page.jsp").forward(request, response);
				break;
			case "refresh":
				ResponseGUIString GUIrefresh = (ResponseGUIString)controller.setAndExecuteRequest(new CmdRetrieveGUIString());
				System.out.println("GUI string obtained in ClientView#refresh was: "+GUIrefresh.getCoordString());
				time = controller.askTime();
				request.setAttribute("changed", "true");
				request.setAttribute("time", time);
				request.setAttribute("gui", GUIrefresh.getCoordString());
				
				//only set "feedback" field if it doesn't already exist
				String feedback = (String)request.getAttribute("feedback");
				if(feedback == null || feedback.equals("")) {
					request.setAttribute("feedback", "Live view has been refreshed");
				}
				request.getRequestDispatcher("index.jsp").forward(request, response);
				break;
			case "spawn":
				
				SpawnCredentials creds = controller.getSpawnCredentials();
				name = creds.getName();
				lastName = creds.getLastName();
				
				ResponseToRegistration respRegTemp = (ResponseToRegistration)controller.setAndExecuteRequest(new CmdRegistrationRequest(name, lastName));
				time = controller.askTime();
				request.setAttribute("time", time);
				
				if(respRegTemp.isACode()) {
					request.setAttribute("feedback", "Customer was spawned successfully: "+name+" "+lastName+", code="+respRegTemp.getCode());
				}else {
					request.setAttribute("feedback", "Customer cannot be spawned right now. Reason: "+respRegTemp.getCode());
					}
				
				request.getRequestDispatcher("RefreshCust").forward(request, response);
				break;
			default:
				request.setAttribute("changed", false);
				request.setAttribute("feedback", "");
				request.getRequestDispatcher("index.jsp").forward(request, response);
		}
	
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String query = request.getQueryString();
		System.out.println("POST @ClientView: "+query);
		
		//POST here only called upon logout
		request.setAttribute("command", "refresh");
		doGet(request, response);
		
	}

}
