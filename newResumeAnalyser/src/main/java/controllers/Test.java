package controllers;

import java.io.IOException;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;

import jakarta.servlet.annotation.WebServlet;
import org.json.JSONObject;

import dao.ResumeDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.Resume;

/**
 * Servlet implementation class Test
 */
//@WebServlet("/Test")
	@WebServlet("/Test")
public class Test extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public Test() {
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
//		String jsonString = new String(request.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
//		JSONObject jsonData = new JSONObject(jsonString);
		
//		boolean isInsert=ResumeDAO.insertResume(jsonString);
//		
//			response.getWriter().write(isInsert+"");
		
		//bbhi
//		Resume resume=ResumeDAO.getResumeById(2);
//		response.getWriter().write(resume.toString());
		
		//gwjhd
		ResumeDAO.toggleIsSelected(2);
//		ResumeDAO.toggleIsFinalized(2);
//		response.getWriter().write(null)
		
		
	}

}
