package com.bearlycattable;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;

import app.BCController;
import dto.CmdReset;
import dto.ResponseReset;

/**
 * 
 */
@WebServlet("/Reset")
public class Reset extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private BCController controller = BCController.getInstance();
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String query = request.getQueryString();
		System.out.println("GET @Reset: "+query);
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String query = request.getQueryString();
		System.out.println("POST @Reset: "+query);
		
		//hardcoded admin account
		String salt = controller.getSalt("admin");
		
		if(DigestUtils.sha256Hex(request.getParameter("pass-reset")+salt).equals(controller.getHash("admin"))){
			request.setAttribute("feedback", "OK, database has been reset to blank slate");
			ResponseReset resetResp = (ResponseReset)controller.setAndExecuteRequest(new CmdReset());
			request.setAttribute("feedback", resetResp.getResponse());
			request.getRequestDispatcher("RefreshCust").forward(request, response);
		}else {
			request.setAttribute("feedback", "Incorrect password. Database was not reset");
			request.getRequestDispatcher("RefreshCust").forward(request, response);
		}
		
	}

}
