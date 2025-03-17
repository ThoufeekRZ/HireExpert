package controllers;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import jakarta.servlet.annotation.WebServlet;
import org.json.JSONObject;

import dao.UserDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import models.User;
import utils.SessionUtil;
import validation.GetUserIdBySessionId;
import view.CookieUtil;

/**
 * Servlet implementation class GetUserExtraInfo
 */
//@WebServlet("/GetUserExtraInfo")
@WebServlet("/GetUserExtraInfo")
public class GetUserExtraInfo extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * Default constructor.
	 */
	public GetUserExtraInfo() {
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

		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		System.out.println("....getuserextrainfo");
		String jsonString = new String(request.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
		JSONObject jsonData = new JSONObject(jsonString);
//
		String sessionId=jsonData.getString("sessionId");

		String userId = GetUserIdBySessionId.getUserID(sessionId);
		System.out.println("session" + userId);
		JSONObject jsonObject = UserDAO.getUserWithExtraInfo(userId);

		System.out.println(jsonObject);

		response.getWriter().write(jsonObject.toString());

	}

}
