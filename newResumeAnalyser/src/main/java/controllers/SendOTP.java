package controllers;

import java.io.BufferedReader;
import java.io.IOException;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
import java.util.Random;

import jakarta.servlet.annotation.WebServlet;
import org.json.JSONObject;

//import java.util.Properties;
//import java.util.Random;
//
//import javax.mail.Message;
//import javax.mail.MessagingException;
//import javax.mail.PasswordAuthentication;
//import javax.mail.Session;
//import javax.mail.Transport;
//import javax.mail.internet.InternetAddress;
//import javax.mail.internet.MimeMessage;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import utils.SendOTPMail;
import validation.ValidEmail;

/**
 * Servlet implementation class SendOTP
 */
//@WebServlet("/SendOTP")
	@WebServlet("/SendOTP")
public class SendOTP extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public SendOTP() {
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
//		String email = request.getParameter("email");
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS, PUT, DELETE");
		response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
		response.setHeader("Access-Control-Allow-Credentials", "true");
		response.setContentType("application/json");
		
		System.out.println("Inside send otp");
		StringBuilder jsonBuffer = new StringBuilder();
        String line;
        try (BufferedReader reader = request.getReader()) {
            while ((line = reader.readLine()) != null) {
                jsonBuffer.append(line);
            }
        }

        // Parse JSON
        JSONObject jsonRequest = new JSONObject(jsonBuffer.toString());
        String email = jsonRequest.optString("email", "").trim(); 
        JSONObject responseJson = new JSONObject();
		
		boolean isValidEmail=ValidEmail.isValidEmail(email);
		JSONObject SendObj=null;
		if(isValidEmail) {
        
        HttpSession mySession = request.getSession();
       
			SendObj=SendOTPMail.isSendOTP(email,mySession);
			if (SendObj.getBoolean("isSend")) {
	            responseJson.put("status", "success");
	            responseJson.put("message", "OTP sent successfully to " + email);
	            responseJson.put("otp", SendObj.getInt("otp"));
	        } else {
	            responseJson.put("status", "error");
	            responseJson.put("message", "Failed to send OTP. Please try again.");
	        }	
//			request.setAttribute("message","OTP is sent to your email id "+isSend);
			
		}
		else {
			 responseJson.put("status", "error");
	            responseJson.put("message", "Failed to send OTP. Please try again.");
//			request.setAttribute("message", "Invalid email");
		}
			
			

	        // Send JSON response
	        response.setContentType("application/json");
	        response.setCharacterEncoding("UTF-8");
	        response.getWriter().write(responseJson.toString());
			
		
	}
	
//	protected void doOptions(HttpServletRequest request, HttpServletResponse response) {
//		response.setHeader("Access-Control-Allow-Origin", "*");
//		response.setHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS, PUT, DELETE");
//		response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
//		response.setHeader("Access-Control-Allow-Credentials", "true");
//		 
//		response.setStatus(HttpServletResponse.SC_OK);
//	}

}
