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


@WebServlet("/getResumeFor2")
public class getResumeForRound2Servlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();

        int id = Integer.parseInt(req.getParameter("id"));
        int recruitId = Integer.parseInt(req.getParameter("recruitId"));

        Resume resume = ResumeDAO.getResumeById(id,recruitId);

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
        jsonObject.put("status",resume.getIsEmailSentStatus());
        jsonObject.put("score",resume.getResumeScore());

        out.print(jsonObject.toString());



    }
}
