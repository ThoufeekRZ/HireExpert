package controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import jakarta.servlet.annotation.WebServlet;
import org.json.JSONObject;

import dao.DBConnection;
import dao.RecruitDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class DeleteRecruit
 */
//@WebServlet("/DeleteRecruit")
	@WebServlet("/DeleteRecruit")
public class DeleteRecruit extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * Default constructor.
	 */
	public DeleteRecruit() {
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
		System.out.println("vghj");
		
//		Connection conn = DBConnection.getConnection();
		
		response.setContentType("application/json");
	    response.setCharacterEncoding("UTF-8");

	    
	    StringBuilder sb = new StringBuilder();
	    String line;

	    // Read JSON request body
	    try (BufferedReader reader = request.getReader()) {
	        while ((line = reader.readLine()) != null) {
	            sb.append(line);
	        }
	    }

//	    try {
//	        if (conn == null) {
//	            throw new SQLException("Database connection failed.");
//	        }
//
	        JSONObject jsonObj = new JSONObject(sb.toString());
	        
	        JSONObject jsonResponse = RecruitDAO.deleteRecruit(jsonObj);
//	        int recruitId = jsonObj.getInt("recruitId");
//	        System.out.println(recruitId+" recId");
//
//	        // Check if recruit exists and get its status and requirementId
//	        String checkQuery = "SELECT status, requirementId FROM recruit WHERE recruitId = ?";
//	        try (PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {
//	            checkStmt.setInt(1, recruitId);
//	            ResultSet rs = checkStmt.executeQuery();
//
//	            if (rs.next()) {
//	                String status = rs.getString("status");
//	                int requirementId = rs.getInt("requirementId");
//
//	                if (!"In progress".equals(status)) {
//	                	System.out.println("delete....");
//	                    // **Step 2: First, delete the recruit record**
//	                    try (PreparedStatement deleteRecruitStmt = conn.prepareStatement("DELETE FROM recruit WHERE recruitId = ?")) {
//	                        deleteRecruitStmt.setInt(1, recruitId);
//	                        deleteRecruitStmt.executeUpdate();
//	                    }
//
//	                    // **Step 3: Delete related requirement skills**
//	                    try (PreparedStatement deleteSkillsStmt = conn.prepareStatement("DELETE FROM requirement_skill WHERE requirementId = ?")) {
//	                        deleteSkillsStmt.setInt(1, requirementId);
//	                        deleteSkillsStmt.executeUpdate();
//	                    }
//
//	                    // **Step 4: Delete requirement**
//	                    try (PreparedStatement deleteRequirementStmt = conn.prepareStatement("DELETE FROM requirement WHERE requirementId = ?")) {
//	                        deleteRequirementStmt.setInt(1, requirementId);
//	                        int result = deleteRequirementStmt.executeUpdate();
//
//	                        if (result == 1) {
//	                            jsonResponse.put("success", true);
//	                            jsonResponse.put("message", "Recruit and requirement deleted successfully.");
//	                        } else {
//	                            jsonResponse.put("success", false);
//	                            jsonResponse.put("message", "Requirement not found or could not be deleted.");
//	                        }
//	                    }
//	                } else {
//	                    jsonResponse.put("success", false);
//	                    jsonResponse.put("message", "Recruit cannot be deleted as it is 'In progress'.");
//	                }
//	            } else {
//	                jsonResponse.put("success", false);
//	                jsonResponse.put("message", "Recruit not found.");
//	            }
//	        }
//	    } catch (Exception e) {
//	        jsonResponse.put("success", false);
//	        jsonResponse.put("message", "Error: " + e.getMessage());
//	        e.printStackTrace();
//	    }

//	    try (PrintWriter out = response.getWriter()) {
//	        out.print(jsonResponse.toString());
//	        out.flush();
//	    }
	        response.getWriter().write(jsonResponse.toString());
	}

}
