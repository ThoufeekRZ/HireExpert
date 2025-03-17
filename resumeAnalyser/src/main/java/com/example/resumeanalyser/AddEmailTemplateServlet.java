package com.example.resumeanalyser;

import DAO.DBConnection;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import java.time.LocalTime;
import java.sql.Date;
import jakarta.servlet.http.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import java.io.*;
import java.sql.*;
import org.json.JSONObject;

@WebServlet("/addEmailTemplate")
public class AddEmailTemplateServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        System.out.println("Insert add template");

        // Read JSON body
        StringBuilder jsonBuffer = new StringBuilder();
        String line;
        try (BufferedReader reader = req.getReader()) {
            while ((line = reader.readLine()) != null) {
                jsonBuffer.append(line);
            }
        }
        JSONObject json = new JSONObject(jsonBuffer.toString());

        System.out.println(json.toString());

        // Extract fields from JSON (ensure alignment with your database schema)
        String position = json.optString("position", "");
        String interviewDate = json.optString("interviewDate", "");
        String interviewTime = json.optString("interviewTime", "");
        String interviewType = json.optString("interviewType", "Video Call");
        String interviewLocationLink = json.optString("interviewLocation", "");
        String interviewerName = json.optString("interviewerName", "");
        String interviewerTitle = json.optString("interviewerTitle", "");
        String additionalInformation = json.optString("additionalInfo", "");
        int recruitId = json.optInt("recruitId", -1);

        if (recruitId == -1) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("Missing or invalid recruitId.");
            return;
        }

        Connection conn = null;
        PreparedStatement insertStmt = null;
        PreparedStatement updateStmt = null;
        ResultSet generatedKeys = null;

        try {
            // Database connection
            conn = DBConnection.getConnection();
            if (conn == null) {
                throw new SQLException("Database connection failed.");
            }
            conn.setAutoCommit(false);

            // Step 1: Insert into emailTemplate table
            String insertQuery = "INSERT INTO emailTemplate (position, interview_date, interview_time, interview_type, interview_location_link, interviewer_name, interviewer_title, additional_information) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            insertStmt = conn.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);
            insertStmt.setString(1, position);

            if (interviewDate.isEmpty()) {
                insertStmt.setNull(2, Types.DATE);
            } else {
                insertStmt.setDate(2, Date.valueOf(interviewDate));
            }

            if (interviewTime.isEmpty()) {
                insertStmt.setNull(3, Types.TIME);
            } else {
                try {
                    LocalTime parsedTime = LocalTime.parse(interviewTime, DateTimeFormatter.ofPattern("HH:mm"));
                    insertStmt.setTime(3, Time.valueOf(parsedTime));
                } catch (DateTimeParseException e) {
                    throw new IllegalArgumentException("Invalid interview time format. Use HH:mm.");
                }
            }

            insertStmt.setString(4, interviewType);
            insertStmt.setString(5, interviewLocationLink);
            insertStmt.setString(6, interviewerName);
            insertStmt.setString(7, interviewerTitle);
            insertStmt.setString(8, additionalInformation);
            insertStmt.executeUpdate();

            // Retrieve generated id (emailId)
            generatedKeys = insertStmt.getGeneratedKeys();
            if (!generatedKeys.next()) {
                throw new SQLException("Failed to retrieve generated emailId.");
            }
            int emailId = generatedKeys.getInt(1);

            // Step 2: Update recruit table with the new emailId
            String updateQuery = "UPDATE recruit SET emailId = ?, status = 'Closed' WHERE recruitId = ?";
            updateStmt = conn.prepareStatement(updateQuery);
            updateStmt.setInt(1, emailId);
            updateStmt.setInt(2, recruitId);
            updateStmt.executeUpdate();

            conn.commit();
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write("Email template added with id: " + emailId + " and linked to recruit: " + recruitId);

        } catch (Exception e) {
            try {
                if (conn != null) conn.rollback(); // Rollback in case of failure
                e.printStackTrace();
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                resp.getWriter().write("Error: " + e.getMessage());
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

        } finally {
            try {
                if (generatedKeys != null) generatedKeys.close();
                if (insertStmt != null) insertStmt.close();
                if (updateStmt != null) updateStmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}
