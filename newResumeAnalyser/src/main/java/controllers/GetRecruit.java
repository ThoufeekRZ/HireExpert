package controllers;

import java.io.IOException;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
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
import models.Requirement;
import models.Skill;

/**
 * Servlet implementation class GetRecruit
 */

@WebServlet("/GetRecruit")
public class GetRecruit extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public GetRecruit() {
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
	    	System.out.println("get recruit");
	        // Read JSON input from request
	        String jsonString = new String(request.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
	        JSONObject jsonData = new JSONObject(jsonString);

	        int recruitId = jsonData.getInt("recruitId");
	        System.out.println(recruitId +" recId");

	        // Fetch Recruit from DAO
	        Recruit recruit = RecruitDAO.getRecruitById(recruitId);
	        System.out.println(recruit);
	        
	        // Handle case where recruit is not found
	        if (recruit == null) {
	            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
	            response.getWriter().write("{\"error\": \"Recruit not found\"}");
	            return;
	        }

	        // Prepare response JSON
	        JSONObject responseJSON = new JSONObject();
	        responseJSON.put("recruitName", recruit.getRecruitName());
	        responseJSON.put("maximumNumber", recruit.getMaximumNumber());
	        responseJSON.put("description", recruit.getDescription());
	        
	        responseJSON.put("accepted", recruit.getAccepted());
	        responseJSON.put("reject", recruit.getReject());
	        responseJSON.put("mailSented", recruit.getMailSented());
	        responseJSON.put("totalResume", recruit.getTotal_Resume());
	        responseJSON.put("status", recruit.getStatus());
	        System.out.println(recruit.getCreatedAt());
	        responseJSON.put("createdAt", recruit.getCreatedAt());

	        // Get Requirement details
	        Requirement requirement = recruit.getRequirement();
	        if (requirement != null) {
	            responseJSON.put("experienceYear", requirement.getExperienceYear());
	            responseJSON.put("qualification", requirement.getQualification());

	            // Get Skills list
	            List<Skill> skills = requirement.getSkills();
	            JSONArray skillsArray = new JSONArray();
	            for (Skill skill : skills) {
	                skillsArray.put(skill.getSkillName());
	            }
	            responseJSON.put("skills", skillsArray);
	        } else {
	            responseJSON.put("experienceYear", JSONObject.NULL);
	            responseJSON.put("qualification", JSONObject.NULL);
	            responseJSON.put("skills", new JSONArray());
	        }
	        System.out.println(responseJSON+" getRecruit");
	        // Set response headers and send JSON
	        response.setStatus(HttpServletResponse.SC_OK);
	        response.setContentType("application/json");
	        response.setCharacterEncoding("UTF-8");
	        response.getWriter().write(responseJSON.toString());

	    } catch (Exception e) {
	        // Handle unexpected errors
	        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	        response.getWriter().write("{\"error\": \"An error occurred\"}");
	        e.printStackTrace(); // Log for debugging
	    }
	}


}
