package controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import jakarta.servlet.annotation.WebServlet;
import org.json.JSONObject;

import dao.UserDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import validation.EmailPasswordValidation;

/**
 * Servlet implementation class Signup
 */
//@WebServlet("/Signup")
	@WebServlet("/Signup")
public class Signup extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Signup() {
		super();
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
		
		JSONObject responseJSON = new JSONObject();

		try {

			String jsonString = new String(request.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
			System.out.println("Received JSON: " + jsonString);
			JSONObject jsonData = new JSONObject(jsonString);

			String username = jsonData.optString("username", "").trim();
			String password = jsonData.optString("password", "").trim();
			String email = jsonData.optString("email", "").trim();

			if (username.isEmpty() || password.isEmpty() || email.isEmpty()) {
				responseJSON.put("error", "Missing required fields");
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			} else {
				boolean isValidEmailPassword = EmailPasswordValidation.isValid(email, password);

				if (isValidEmailPassword) {
					boolean isSignup = UserDAO.insertUser(username, email, password);
					responseJSON.put("isSignup", isSignup);
					responseJSON.put("isValid", true);
				} else {
					responseJSON.put("isValid", false);
				}
			}
		} catch (Exception e) {
			responseJSON.put("error", "Invalid JSON format");
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST); // 400
		}

		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(responseJSON.toString());

	}

}
