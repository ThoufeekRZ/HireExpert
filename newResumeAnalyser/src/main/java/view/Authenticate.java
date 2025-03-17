package view;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import validation.GetUserIdBySessionId;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.JSONObject;

import dao.DBConnection;

/**
 * Servlet Filter implementation class Authenticate
 */
//@WebFilter("/Authenticate")
public class Authenticate extends HttpFilter implements Filter {

	/**
	 * @see HttpFilter#HttpFilter()
	 */
	public Authenticate() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
//		System.out.println("auth in");
//		
//		String jsonString = new String(request.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
//		System.out.println("inFilter"+jsonString);
//        JSONObject jsonData = new JSONObject(jsonString);
//        
//        String sessionId=jsonData.getString("sessionId");
//
//	    if (sessionId != null) {
//	    	boolean isValidSession = validateSession(sessionId);
//	        if (isValidSession) {
//	            chain.doFilter(request, response);
//	        } else {
//	            ((HttpServletResponse) response).sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid session.");
//	        }
//	    } else {
//	        ((HttpServletResponse) response).sendError(HttpServletResponse.SC_BAD_REQUEST, "Session ID is missing.");
//	    }
		
		
//		System.out.println("Auth Filter started");
//
//		HttpServletRequest httpRequest = (HttpServletRequest) request;
//		HttpServletResponse httpResponse = (HttpServletResponse) response;
//
//		if ("POST".equalsIgnoreCase(httpRequest.getMethod())) { // Ensure it's a POST request
//			StringBuilder sb = new StringBuilder();
//			String line;
//			try (BufferedReader reader = httpRequest.getReader()) {
//				while ((line = reader.readLine()) != null) {
//					sb.append(line);
//				}
//			}
//
//			String requestBody = sb.toString();
//			System.out.println("Request Body in Filter: " + requestBody);
//
//			if (requestBody.isEmpty()) {
//				httpResponse.sendError(HttpServletResponse.SC_BAD_REQUEST, "Request body is empty.");
//				return;
//			}
//
//			try {
//				JSONObject jsonData = new JSONObject(requestBody);
//				String sessionId = jsonData.optString("sessionId", null);
//
//				if (sessionId != null && validateSession(sessionId)) {
//					chain.doFilter(request, response); // Proceed if session is valid
//				} else {
//					httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid or missing session ID.");
//				}
//			} catch (Exception e) {
//				httpResponse.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid JSON format.");
//			}
//		} else {
//			// If it's not a POST request, just continue
//			chain.doFilter(request, response);
//		}
		chain.doFilter(request, response);

//      String userId=GetUserIdBySessionId.getUserID(sessionId);

		// Cookie[] cookies = ((HttpServletRequest) request).getCookies();
//	    String sessionId = null;
//	    if (cookies != null) {
//	        for (Cookie cookie : cookies) {
//	            if ("SESSION_ID".equals(cookie.getName())) {
//	                sessionId = cookie.getValue();
//	            }
//	        }
//	    }
	}

	private boolean validateSession(String sessionId) {


		boolean isValid = false;

		String query = "SELECT * FROM session WHERE sessionId = ?";

		try (Connection conn=DBConnection.getConnection();){
			PreparedStatement pstmt = conn.prepareStatement(query);

			pstmt.setString(1, sessionId);
			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {
				isValid = true;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return isValid;
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
