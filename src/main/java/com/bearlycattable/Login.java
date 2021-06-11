package com.bearlycattable;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;

import app.BCController;
import dto.CmdLogin;
import dto.ResponseBool;

/**
 * Servlet implementation class Login
 */
@WebServlet("/Login")
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private BCController controller = BCController.getInstance();
       
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String query = request.getQueryString();
		System.out.println("GET @Login: "+query);
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String query = request.getQueryString();
		System.out.println("POST @Login: "+query);
		
		
		if(request.getParameter("username").equals("") || request.getParameter("password").equals("")) {
			request.getRequestDispatcher("login_page.jsp").forward(request, response);
		}else {
			String salt = controller.getSalt(request.getParameter("username"));
			//System.out.println("Salt retrieved: "+salt);
			
			if(DigestUtils.sha256Hex(request.getParameter("password")+salt).equals(controller.getHash(request.getParameter("username")))){
				ResponseBool resp = (ResponseBool)controller.setAndExecuteRequest(new CmdLogin(request.getParameter("username")));	
				if(resp.getValue() == true) {
					request.getSession(true).setAttribute("id", resp.getId());
					//System.out.println("id attribute should be set to: "+resp.getId());
					request.getRequestDispatcher("RefreshList").forward(request, response);
				}else {
					request.setAttribute("feedback", "Login failed. Incorrect username or password");
					request.getRequestDispatcher("login_page.jsp").forward(request, response);
				}
			}else{
				request.setAttribute("feedback", "Incorrect username or password.");
				request.getRequestDispatcher("login_page.jsp").forward(request, response);
			}
				
		}
		
	}

}
