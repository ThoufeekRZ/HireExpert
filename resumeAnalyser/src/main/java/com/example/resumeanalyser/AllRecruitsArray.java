package com.example.resumeanalyser;

import java.io.IOException;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;

import jakarta.servlet.annotation.WebServlet;
import org.json.JSONArray;

import DAO.RecruitDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

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

		System.out.println("allRecruit");

//		String sessionId=CookieUtil.getSessionId(request);
//		String userId=GetUserIdBySessionId.getUserID(sessionId);
	//	String userId=SessionUtil.getLoggedInUser(request);
		String userId="10fc0060ea0542e5";

		try {
			JSONArray allRecruitArray = RecruitDAO.getAllRecruits(userId);
			response.getWriter().write(allRecruitArray.toString());
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

//		ObjectMapper objectMapper = new ObjectMapper();
//        String jsonResponse = objectMapper.writeValueAsString(allRecruitArray);
//
//        // Send response as JSON

//        PrintWriter out = response.getWriter();
//        out.print(jsonResponse);



	}

}
