package controllers;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import jakarta.servlet.annotation.WebServlet;
import org.json.JSONObject;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.LogoutService;
import validation.GetUserIdBySessionId;

/**
 * Servlet implementation class Logout
 */
@WebServlet("/Logout")
public class Logout extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * Default constructor.
	 */
	public Logout() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String jsonString = new String(request.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
		JSONObject jsonData = new JSONObject(jsonString);
		
		String sessionId=jsonData.getString("sessionId");
		boolean isLogout=LogoutService.logout(sessionId);
		if(isLogout) {
			 response.setStatus(HttpServletResponse.SC_OK);
           response.getWriter().write("{\"message\": \"Session deleted successfully\"}");
		}else {
          response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
          response.getWriter().write("{\"error\": \"Database error occurred\"}");
		}
		
	}

}
