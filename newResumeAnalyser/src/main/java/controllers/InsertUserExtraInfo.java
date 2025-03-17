package controllers;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;

import jakarta.servlet.annotation.WebServlet;
import org.json.JSONObject;

import dao.DBConnection;
import dao.UserDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.SessionUtil;
import validation.GetUserIdBySessionId;
import view.CookieUtil;

/**
 * Servlet implementation class InsertUserExtraInfo
 */
//@WebServlet("/InsertUserExtraInfo")
	@WebServlet("/InsertUserExtraInfo")
public class InsertUserExtraInfo extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public InsertUserExtraInfo() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    response.setContentType("application/json");
	    response.setCharacterEncoding("UTF-8");

	    String jsonString = new String(request.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
	    JSONObject jsonData = new JSONObject(jsonString);
	    String sessionId=jsonData.getString("sessionId");
//
	    String userId=GetUserIdBySessionId.getUserID(sessionId);
	    boolean isInsertUserInfo = UserDAO.insertUserExtraInfo(jsonData, userId);

	    JSONObject jsonResponse = new JSONObject();
	    if (isInsertUserInfo) {
	        jsonResponse.put("status", "success");
	        jsonResponse.put("message", "User extra info inserted successfully.");
	    } else {
	        jsonResponse.put("status", "error");
	        jsonResponse.put("message", "Failed to insert user extra info.");
	    }

	    response.getWriter().write(jsonResponse.toString());
	}


}
