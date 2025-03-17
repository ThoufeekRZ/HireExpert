package com.example.resumeanalyser;

import DAO.DescriptionMatcher;
import DAO.InterviewDetailsDAO;
import DAO.RecruitDAO;
import DAO.ResumeDAO;
import DTO.InterviewDetails;
import DTO.Resume;
import com.mysql.cj.xdevapi.JsonArray;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import DTO.Recruit;
import org.json.JSONArray;
import org.json.JSONObject;

public class getRecruitAfterInfo extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter out = resp.getWriter();

        int recruitId = Integer.parseInt(req.getParameter("recruitId"));

        try {


            List<Resume> resumes = ResumeDAO.getCorresspondingResume(recruitId);



            JSONArray jsonArray = new JSONArray();

            for (Resume resume : resumes){
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("id",resume.getResumeId());
                jsonObject.put("title",resume.getExperiences().getFirst().getTitle());
                jsonObject.put("previousCompany",resume.getExperiences().getFirst().getCompany());
                jsonObject.put("skills",resume.getSkills());
                System.out.println(DescriptionMatcher.getExperienceYears(resume.getExperiences()));
                jsonObject.put("totalExperiences", DescriptionMatcher.getExperienceYears(resume.getExperiences()));
                jsonObject.put("candidateName",resume.getCandidateName());
                jsonObject.put("status",resume.getStatus());
                jsonObject.put("score",resume.getResumeScore()); resumes = resumes.stream().filter(resume1 -> resume1.getIsEmailSentStatus().equals("Interview")).toList();

                jsonArray.put(jsonObject);
            }


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }
}
