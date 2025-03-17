package controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import jakarta.servlet.annotation.WebServlet;
import org.json.JSONObject;

import com.fasterxml.jackson.databind.ObjectMapper;

import dao.UserDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import models.User;
import utils.SessionUtil;
import validation.GenerateSessionId;
import validation.InsertSessionId;

/**
 * Servlet implementation class Login
 */
//@WebServlet("/Login")
	@WebServlet("/Login")
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * Default constructor.
	 */
	public Login() {
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

		String email = jsonData.getString("email");
		String password = jsonData.getString("password");

		User user = UserDAO.getUser(email, password);
		JSONObject responseJSON = new JSONObject();

		if (user != null) {
			responseJSON.put("username", user.getName());
			responseJSON.put("email", user.getEmail());
			responseJSON.put("isValidUser", true);
			String sessionId = GenerateSessionId.generateUniqueId();
			
			responseJSON.put("sessionId", sessionId);
			
			boolean isInsert = InsertSessionId.insertSession(email, password, sessionId);
			System.out.println("isInsert "+isInsert);
			
			responseJSON.put("isInsertSession", isInsert);


		} else {
			responseJSON.put("isValidUser", false);
		}

		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");

		response.getWriter().write(responseJSON.toString());

	}

}
