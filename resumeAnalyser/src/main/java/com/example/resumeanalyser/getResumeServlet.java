package com.example.resumeanalyser;

import DAO.DescriptionMatcher;
import DAO.ResumeDAO;
import DTO.Resume;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

@WebServlet("/getResume")
public class getResumeServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        int id = Integer.parseInt(req.getParameter("id"));
        String status = req.getParameter("status");

        int recruitId = Integer.parseInt(req.getParameter("recruitId"));

        Resume resume = ResumeDAO.getResumeById(id,recruitId);
        try{
            if(status.equals("New")) {
                ResumeDAO.updateResumeStatus(resume.getResumeId(), recruitId, "Reviewed");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        JSONObject jsonObject = new JSONObject();

        jsonObject.put("email",resume.getContact().getEmail());
        jsonObject.put("id",resume.getResumeId());
        jsonObject.put("title",resume.getExperiences().getFirst().getTitle());
        jsonObject.put("mobile-number",resume.getContact().getContactNumber());
        jsonObject.put("previousCompany",resume.getExperiences().getFirst().getCompany());
        jsonObject.put("experiences",resume.getExperiences());
        jsonObject.put("education",resume.getEducation());
        jsonObject.put("skills",resume.getSkills());
        jsonObject.put("totalExperiences", DescriptionMatcher.getExperienceYears(resume.getExperiences()));
        jsonObject.put("candidateName",resume.getCandidateName());
        jsonObject.put("status",resume.getStatus());
        jsonObject.put("score",resume.getResumeScore());

        out.print(jsonObject.toString());


    }
}
