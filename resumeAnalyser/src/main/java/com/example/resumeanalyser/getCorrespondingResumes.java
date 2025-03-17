package com.example.resumeanalyser;

import DAO.DescriptionMatcher;
import DAO.RecruitDAO;
import DAO.ResumeDAO;
import DTO.Recruit;
import DTO.Resume;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;

@WebServlet("/getCorrespondingResumes")
public class getCorrespondingResumes extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        int recruitId = Integer.parseInt(req.getParameter("recruitId"));
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter out = resp.getWriter();

        System.out.println("Inside get corresponding resumes. Recruit ID: " + recruitId);

        try {
            // Fetch resumes and recruit data
            List<Resume> resumes = ResumeDAO.getCorresspondingResume(recruitId);
            Recruit recruit = RecruitDAO.getRecruitById(recruitId);

            if (resumes == null || resumes.isEmpty()) {
                System.out.println("No resumes found for recruitId: " + recruitId);
                out.println("[]");
                return; // Exit if no resumes are found
            }

            if (recruit == null) {
                System.err.println("Recruit not found for ID: " + recruitId);
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid recruit ID.");
                return;
            }

            int accepted = recruit.getMaximumNumber();
            System.out.println("Total resumes fetched: " + resumes.size() + ", Max accepted: " + accepted);

            // Log each resume's score to detect null values
            for (Resume resume : resumes) {
                System.out.println("Resume ID: " + resume.getResumeId() + ", Score: " + resume.getResumeScore());
            }

            // Sort resumes with null-safe comparator
            resumes.sort(Comparator.comparing(Resume::getResumeScore, Comparator.nullsLast(Comparator.naturalOrder())).reversed());

            // Limit the list to accepted resumes
            if (resumes.size() > accepted) {
                resumes = resumes.subList(0, accepted);
            }

            JSONArray jsonArray = new JSONArray();

            // Process and log each resume
            for (Resume resume : resumes) {
                JSONObject jsonObject = new JSONObject();

                jsonObject.put("id", resume.getResumeId());

                // Check experiences list for null or empty values
                if (resume.getExperiences() == null || resume.getExperiences().isEmpty()) {
                    System.err.println("Null/Empty experiences for resume ID: " + resume.getResumeId());
                    jsonObject.put("title", "N/A");
                    jsonObject.put("previousCompany", "N/A");
                    jsonObject.put("totalExperiences", 0);
                } else {
                    jsonObject.put("title", resume.getExperiences().getFirst().getTitle());
                    jsonObject.put("previousCompany", resume.getExperiences().getFirst().getCompany());
                    jsonObject.put("totalExperiences", DescriptionMatcher.getExperienceYears(resume.getExperiences()));
                }

                jsonObject.put("skills", resume.getSkills() != null ? resume.getSkills() : "N/A");
                jsonObject.put("candidateName", resume.getCandidateName() != null ? resume.getCandidateName() : "N/A");
                jsonObject.put("status", resume.getStatus() != null ? resume.getStatus() : "N/A");
                jsonObject.put("score", resume.getResumeScore() != 0 ? resume.getResumeScore() : 0);
                jsonObject.put("isEmailSentStatus", resume.getIsEmailSentStatus());

                jsonArray.put(jsonObject);
            }

            out.println(jsonArray.toString());

        } catch (SQLException e) {
            e.printStackTrace(); // Log SQL errors for debugging
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error occurred.");
        } catch (Exception e) {
            e.printStackTrace(); // Catch any unexpected errors
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An error occurred.");
        }
    }



}
