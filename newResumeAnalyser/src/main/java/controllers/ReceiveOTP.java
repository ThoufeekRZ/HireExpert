package controllers;

import java.io.BufferedReader;
import java.io.IOException;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;

import jakarta.servlet.annotation.WebServlet;
import org.json.JSONObject;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Servlet implementation class ReceiveOTP
 */
@WebServlet("/ReceiveOTP")
public class ReceiveOTP extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public ReceiveOTP() {
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

		StringBuilder jsonData = new StringBuilder();
        String line;
        try (BufferedReader reader = request.getReader()) {
            while ((line = reader.readLine()) != null) {
                jsonData.append(line);
            }
        }

        // Convert request JSON to object
        JSONObject jsonObject = new JSONObject(jsonData.toString());
        int receivedOtp = jsonObject.getInt("enteredOtp"); // OTP received from the frontend

        // Retrieve OTP from session
        HttpSession session = request.getSession(false); // Get existing session
        JSONObject responseJson = new JSONObject();
        System.out.println(session);
        
        int storOTP=(Integer) jsonObject.getInt("storedOtp");
        
//        if (session == null || session.getAttribute("otp") == null) {
//        	System.out.println("Hi...");
//            responseJson.put("status", "error");
//            responseJson.put("message", "Session expired. Please request OTP again.");
//        } else {
//        	System.out.println("Hello...");
//            int storedOtp = (int) session.getAttribute("otp"); // OTP stored in session
            
            if (receivedOtp == storOTP) {
                responseJson.put("status", "success");
                responseJson.put("message", "OTP verified successfully!");
                
                // OTP verified, remove from session (optional)
//                session.removeAttribute("otp");
            } else {
                responseJson.put("status", "error");
                responseJson.put("message", "Invalid OTP. Please try again.");
            }
//        }

        // Send JSON response
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(responseJson.toString());

	}

}
