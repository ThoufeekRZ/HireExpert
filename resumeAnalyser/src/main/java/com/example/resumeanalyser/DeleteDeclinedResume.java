package com.example.resumeanalyser;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import jakarta.servlet.annotation.WebServlet;
import org.json.JSONObject;

import DAO.DBConnection;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class DeleteDeclinedResume
 */
@WebServlet("/DeleteDeclinedResume")
public class DeleteDeclinedResume extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * Default constructor.
     */
    public DeleteDeclinedResume() {
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
        JSONObject jsonResponse = new JSONObject();

        System.out.println("Delete resume request received");

        try {
            BufferedReader reader = request.getReader();
            StringBuilder requestBody = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                requestBody.append(line);
            }
            JSONObject jsonObject = new JSONObject(requestBody.toString());
            int recruitId = jsonObject.getInt("recruitId");
            boolean isDelete = resetTables(recruitId);

            jsonResponse.put("success", isDelete);

        } catch (Exception e) {
            e.printStackTrace();
            jsonResponse.put("success", false);
            jsonResponse.put("status", "error");
            jsonResponse.put("message", "An error occurred: " + e.getMessage());
        }

        response.getWriter().write(jsonResponse.toString());

    }

    // change pananum
    public static boolean resetTables(int recruitId) {
        String[] dependentDeletes = {
                "DELETE FROM resume_experience WHERE resumeId IN (SELECT resumeId FROM recr_resu_relation WHERE recruitId = ? AND isEmailSentStatus = 'Declined')",

                "DELETE FROM contactDetails WHERE contactId IN (SELECT contactId FROM resume WHERE resumeId IN (SELECT resumeId FROM recr_resu_relation WHERE recruitId = ? AND isEmailSentStatus = 'Declined'))",

                "DELETE FROM experienceDetails WHERE experienceId IN (SELECT experienceId FROM resume_experience WHERE resumeId IN (SELECT resumeId FROM recr_resu_relation WHERE recruitId = ? AND isEmailSentStatus = 'Declined'))",

                "DELETE FROM resume_skill WHERE resumeId IN (SELECT resumeId FROM resume WHERE resumeId IN (SELECT resumeId FROM recr_resu_relation WHERE recruitId = ? AND isEmailSentStatus = 'Declined'))",

                "DELETE FROM recr_resu_relation WHERE recruitId = ? AND isEmailSentStatus = 'Declined'"
        };

        // Delete from resume last, after all dependencies are removed
        String conditionalDeleteQuery = "DELETE FROM resume WHERE resumeId IN (SELECT resumeId FROM recr_resu_relation WHERE recruitId = ? AND isEmailSentStatus = 'Declined')";

        try (Connection conn = DBConnection.getConnection()) {
            if (conn == null) {
                throw new SQLException("Database connection failed.");
            }

            conn.setAutoCommit(false);

            // Delete dependent records first
            for (String query : dependentDeletes) {
                try (PreparedStatement stmt = conn.prepareStatement(query)) {
                    stmt.setInt(1, recruitId);
                    stmt.executeUpdate();
                }
            }

            // Now delete from resume (since dependencies are gone)
            try (PreparedStatement stmt = conn.prepareStatement(conditionalDeleteQuery)) {
                stmt.setInt(1, recruitId);
                stmt.executeUpdate();
            }

            conn.commit();
            System.out.println("Filtered deletion completed successfully for declined resumes.");
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }




}