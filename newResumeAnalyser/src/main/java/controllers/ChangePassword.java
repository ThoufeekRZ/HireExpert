package controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jakarta.servlet.annotation.WebServlet;
import org.json.JSONObject;

import dao.DBConnection;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.PasswordChangeService;
import utils.UpdatePassword;
import validation.GetUserIdBySessionId;
/**
 * Servlet implementation class ChangePassword
 */
//@WebServlet("/ChangePassword")
    @WebServlet("/ChangePassword")
public class ChangePassword extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public ChangePassword() {
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
//	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		response.setContentType("application/json");
//        response.setCharacterEncoding("UTF-8");
//        System.out.println("inside changepassword");
//        response.setHeader("Access-Control-Allow-Origin", "*");
//		response.setHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS, PUT, DELETE");
//		response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
//		response.setHeader("Access-Control-Allow-Credentials", "true");
//		response.setContentType("application/json");
////        JSONObject responseJson = new JSONObject();
////        String newPassword = request.getParameter("newPassword");
//        BufferedReader reader = request.getReader();
//        StringBuilder sb = new StringBuilder();
//        String line;
//        while ((line = reader.readLine()) != null) {
//            sb.append(line);
//        }
//
//        JSONObject requestJson = new JSONObject(sb.toString());
//        String newPassword = requestJson.getString("newPassword");
//        
////        String sessionId = null;
////        Cookie[] cookies = request.getCookies();
////        if (cookies != null) {
////            for (Cookie cookie : cookies) {
////                if ("SESSION_ID".equals(cookie.getName())) {
////                    sessionId = cookie.getValue();
////                    break;
////                }
////            }
////        }
////        
////        JSONObject responseJson = new JSONObject();
////        if (sessionId == null) {
////            responseJson.put("status", "error");
////            responseJson.put("message", "Session expired. Please log in again.");
////            response.getWriter().write(responseJson.toString());
////            return;
////        }
//
//        JSONObject result = PasswordChangeService.changePassword("session_123", newPassword);
//        response.getWriter().write(result.toString());
//	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		JSONObject jsonObject = parseRequestBody(request);
        String newPassword = jsonObject.optString("newPassword", "");

        try (Connection conn = DBConnection.getConnection()) {

            // Get sessionId from cookies
//        String sessionId = getSessionIdFromCookies(request);
//        String sessionId="session_123";

//        if (sessionId == null) {
//            sendResponse(response, "error", "Session expired. Please log in again.");
//            return;
//        }

            // Fetch userId from session table using sessionId
//        String sessionId=jsonObject.getString("sessionId");
            String email = jsonObject.getString("enterEmail");
            String userId = null;
            String query = "SELECT userId FROM userDetails WHERE email = ?";

            try {
                PreparedStatement pstmt = conn.prepareStatement(query);

                pstmt.setString(1, email);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        userId = rs.getString("userId");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


//        String userId = GetUserIdBySessionId.getUserID(sessionId); 
            if (userId == null) {
                sendResponse(response, "error", "Invalid session. Please log in again.");
                return;
            }

            // Update password in database
            boolean isUpdated = UpdatePassword.updatePassword(userId, newPassword);

            if (isUpdated) {
                sendResponse(response, "success", "Password updated successfully!");
            } else {
                sendResponse(response, "error", "Failed to update password.");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
	}
	
	private JSONObject parseRequestBody(HttpServletRequest request) throws IOException {
        StringBuilder jsonData = new StringBuilder();
        String line;
        try (BufferedReader reader = request.getReader()) {
            while ((line = reader.readLine()) != null) {
                jsonData.append(line);
            }
        }
        return new JSONObject(jsonData.toString());
    }
	
//	private String getSessionIdFromCookies(HttpServletRequest request) {
//        Cookie[] cookies = request.getCookies();
//        if (cookies != null) {
//            for (Cookie cookie : cookies) {
//                if ("SESSION_ID".equals(cookie.getName())) {
//                    return cookie.getValue();
//                }
//            }
//        }
//        return null;
//    }
	
	private void sendResponse(HttpServletResponse response, String status, String message) throws IOException {
        JSONObject responseJson = new JSONObject();
        responseJson.put("status", status);
        responseJson.put("message", message);
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(responseJson.toString());
    }

}
