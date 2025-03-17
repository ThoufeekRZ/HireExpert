package controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
import java.util.List;

import jakarta.servlet.annotation.WebServlet;
import org.json.JSONArray;
import org.json.JSONObject;

import com.fasterxml.jackson.databind.ObjectMapper;

import dao.RecruitDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.Recruit;
import utils.SessionUtil;
import validation.GetUserIdBySessionId;
import view.CookieUtil;

/**
 * Servlet implementation class AllRecruitsArray
 */
//@WebServlet("/AllRecruitsArray")
	@WebServlet("/AllRecruitsArray")
public class AllRecruitsArray extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * Default constructor.
	 */
	public AllRecruitsArray() {
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

		System.out.println("allRecruit");

		BufferedReader reader = request.getReader();
		StringBuilder requestBody = new StringBuilder();
		String line;

		while ((line = reader.readLine()) != null) {
			requestBody.append(line);
		}
		JSONObject jsonObject = new JSONObject(requestBody.toString());
		String sessionId = jsonObject.optString("sessionId", null);
		String userId = GetUserIdBySessionId.getUserID(sessionId);


		JSONArray allRecruitArray = RecruitDAO.getAllRecruits(userId);


		response.getWriter().write(allRecruitArray.toString());

	}

}
