package com.example.resumeanalyser;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
import java.util.List;

import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.annotation.WebServlet;
import org.json.JSONArray;
import org.json.JSONObject;

import DAO.DBConnection;
import DAO.RecruitDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import DTO.Recruit;
import DTO.Requirement;
import DTO.Skill;
import utils.SessionUtil;
import Validation.GetUserIdBySessionId;
import view.CookieUtil;

/**
 * Servlet implementation class InsertRecruit
 */
@WebServlet("/InsertRecruit")
public class InsertRecruit extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * Default constructor.
	 */
	public InsertRecruit() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 * response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 * response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("Inside insert recruit");


		try (Connection conn = DBConnection.getConnection();){

			String jsonString = new String(request.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
			JSONObject jsonData = new JSONObject(jsonString);

			System.out.println(jsonData);
			String recruitName = jsonData.getString("title");
			int maximumNumber = jsonData.getInt("maximumRecruit");
			String sessionId = jsonData.getString("sessionId");
			String userId = GetUserIdBySessionId.getUserID(sessionId);

//		JSONObject requirementData = jsonData.getJSONObject("requirement");
			int experienceYear = jsonData.getInt("experience");
			String qualification = jsonData.getString("newQualification");

			List<Skill> skills = new ArrayList<>();
			JSONArray skillArray = jsonData.getJSONArray("selectedSkills");

			String description = jsonData.getString("jobDescription");

			PreparedStatement pstmt = null;
			ResultSet rs = null;
			for (int i = 0; i < skillArray.length(); i++) {
				System.out.println("In....");
//		    JSONObject skillObj = skillArray.getJSONObject(i);
				String skillName = skillArray.getString(i);

				int skillId = -1; // Default value if skill not found

				try {
					// Query to fetch skillId using skillName
					String query = "SELECT skillId FROM skills WHERE skillName = ?";
					pstmt = conn.prepareStatement(query);
					pstmt.setString(1, skillName);
					rs = pstmt.executeQuery();

					// If skill exists in the table, retrieve its skillId
					if (rs.next()) {
						skillId = rs.getInt("skillId");
					}

					// Create skill object and set skillId
					Skill skill = new Skill(skillName);
					skill.setSkillId(skillId);
					skills.add(skill);

				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			Requirement requirement = new Requirement(experienceYear, qualification, skills);
			Recruit recruit = new Recruit(recruitName, maximumNumber);
			recruit.setRequirement(requirement);
			recruit.setDescription(description);

			JSONObject responseJSON = new JSONObject();

			System.out.println("recruit " + userId);
			try {
				boolean isInserted = RecruitDAO.insertRecruit(recruit, userId);
				System.out.println("insertRecruit " + isInserted);
				responseJSON.put("success", isInserted);
				responseJSON.put("message", isInserted ? "Recruit inserted successfully" : "Recruit insertion failed");
			} catch (Exception e) {
				responseJSON.put("success", false);
				responseJSON.put("message", "Error: " + e.getMessage());
			}

			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(responseJSON.toString());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}


}
