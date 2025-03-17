package com.example.resumeanalyser;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import DAO.DescriptionMatcher;
import DAO.RecruitDAO;
import DAO.ResumeDAO;
import DTO.Recruit;
import DTO.Resume;
import DAO.ResumeScorer;
import DTO.Resume;
import Models.MultiResumeProcessor;

import Validation.ValidateResume;
import com.mysql.cj.xdevapi.JsonArray;
import com.mysql.cj.xdevapi.JsonValue;
import jakarta.servlet.DispatcherType;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import org.json.JSONArray;
import org.json.JSONObject;

@WebServlet("/uploadResumes")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 2, // 2MB: Store files in memory before saving
        maxFileSize = 1024 * 1024 * 10,      // 10MB: Max file size per resume
        maxRequestSize = 1024 * 1024 * 100   // 100MB: Max request size for multiple files
)
public class ResumeUploadServlet extends HttpServlet {
    private static final String UPLOAD_DIR = System.getProperty("user.home") + "/eclipse-uploads/";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        System.out.println("in");

        // Define upload path
        String uploadPath = System.getProperty("user.home") + "/eclipse-uploads/resume/";
        File uploadDir = new File(uploadPath);

      String recruitIdParam = request.getParameter("recruitId");

        System.out.println("recruitIdParam...............................: " + recruitIdParam);
        System.out.println(recruitIdParam);
        if (recruitIdParam == null || recruitIdParam.isEmpty()) {
            System.err.println("Error: recruitId is missing!");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.println("{ \"error\": \"Missing recruitId in request\" }");
            out.flush();
            out.close();
            return;
        }

        int recruitId = Integer.parseInt(recruitIdParam);
       try {
           Recruit recruit = RecruitDAO.getRecruitById(recruitId);

           if (recruit != null && recruit.getStatus().equals("In progress")) {
               response.setStatus(HttpServletResponse.SC_CONFLICT);
               out.println("{ \"error\": \"Recruit already in progress\" }");
           }
       } catch (SQLException e) {
               throw new RuntimeException(e);
       }



        // Ensure directory exists
        if (!uploadDir.exists() && !uploadDir.mkdirs()) {
            System.err.println("Failed to create upload directory!");
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.println("{ \"error\": \"Failed to create upload directory.\" }");
            out.flush();
            out.close();
            return;
        }




        Collection<Part> fileParts = request.getParts();
        List<File> resumeFiles = new ArrayList<>();

        for (Part filePart : fileParts) {


            if (filePart.getSubmittedFileName() == null || filePart.getSubmittedFileName().isEmpty()) {
                System.err.println("Skipping empty file part.");
                continue;
            }

            String fileName = new File(filePart.getSubmittedFileName()).getName();
            File savedFile = new File(uploadPath + fileName);

            filePart.write(savedFile.getAbsolutePath());
            resumeFiles.add(savedFile);
            System.out.println("Saved: " + savedFile.getAbsolutePath());
        }

        int totalSubmittedFiles = resumeFiles.size();

        MultiResumeProcessor processor = new MultiResumeProcessor();
        List<String> jsonStrings = processor.processMultipleResumes(resumeFiles);

        //inserting all resumes in db
        for (String jsonString : jsonStrings) {
            ValidateResume.isValidResume(jsonString, String.valueOf(recruitId));
        }

        System.out.println("Processed: " + resumeFiles.size());

        List<Resume> resumes = new ArrayList<>();
        try {
           resumes = ResumeScorer.ScoreResume(recruitId);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        int approvedResumes = resumes.size();
        int rejectedResumes = totalSubmittedFiles - approvedResumes;

        try {
            RecruitDAO.updateAdditonalData(recruitId, 0, approvedResumes, rejectedResumes, totalSubmittedFiles);
            System.out.println("updated : " + totalSubmittedFiles+ " / " + approvedResumes + " / " + rejectedResumes);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

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
            jsonObject.put("status","New");
            jsonObject.put("score",resume.getResumeScore());
            jsonArray.put(jsonObject);
        }

        try {
            RecruitDAO.updateRecruitStatus(recruitId, "In progress");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        out.println(jsonArray.toString());

        out.flush();
        out.close();
    }


}


