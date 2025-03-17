package com.example.resumeanalyser;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import jakarta.servlet.annotation.WebServlet;
import org.json.JSONObject;

import DAO.RecruitDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import Validation.GetUserIdBySessionId;

/**
 * Servlet implementation class UpdateRecruit
 */
//@WebServlet("/UpdateRecruit")
	@WebServlet("/updateRecruit")
public class UpdateRecruit extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public UpdateRecruit() {
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
	    try {
	    	System.out.println("update recruit");
	        // Read JSON input from request body
	        String jsonString = new String(request.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
	        JSONObject jsonData = new JSONObject(jsonString);

	        // Extract userId (if needed, assuming it's sent in request)
//	        String sessionId=CookieUtil.getSessionId(request);
//	        System.out.println("updateRecruit"+sessionId);
	        String sessionId=jsonData.getString("sessionId");
	        String userId=GetUserIdBySessionId.getUserID(sessionId);

//	        String userId=SessionUtil.getLoggedInUser(request);
	        // Call DAO method to update the recruit
//	        userId="10fc0060ea0542e5";
	        boolean updateSuccess = RecruitDAO.updateRecruit(jsonData, userId);

	        // Prepare JSON response
	        JSONObject responseJSON = new JSONObject();
	        if (updateSuccess) {
	            responseJSON.put("success", true);
	            responseJSON.put("message", "Recruit details updated successfully.");
	        } else {
	            responseJSON.put("success", false);
	            responseJSON.put("message", "Failed to update recruit details.");
	        }

	        // Send response
	        response.setContentType("application/json");
	        response.setCharacterEncoding("UTF-8");
	        response.getWriter().write(responseJSON.toString());

	    } catch (Exception e) {
	        // Handle unexpected errors
	        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	        JSONObject errorResponse = new JSONObject();
	        errorResponse.put("status", "error");
	        errorResponse.put("message", "An error occurred while processing the request.");
	        response.getWriter().write(errorResponse.toString());

	        e.printStackTrace(); // Log error for debugging
	    }
	}

}
