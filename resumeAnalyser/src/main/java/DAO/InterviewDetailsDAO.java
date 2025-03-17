package DAO;

import DTO.InterviewDetails;

import java.sql.*;

public class InterviewDetailsDAO {

    public static InterviewDetails getInterviewDetailsById(int interview_id) {
        String query = "SELECT * FROM emailTemplate WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            if (conn == null) {
                throw new SQLException("Database connection is null.");
            }

            ps.setInt(1, interview_id);
            System.out.println("Executing query for interview_id: " + interview_id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs == null) {
                    throw new SQLException("Query execution failed, ResultSet is null.");
                }

                if (rs.next()) {
                    System.out.println("Interview found for ID: " + interview_id);
                    return new InterviewDetails(
                            rs.getInt("id"),
                            rs.getString("position"),
                            rs.getDate("interview_date"),
                            rs.getTime("interview_time"),
                            rs.getString("interview_type"),
                            rs.getString("interview_location_link"),
                            rs.getString("interviewer_name"),
                            rs.getString("interviewer_title"),
                            rs.getString("additional_information")
                    );
                } else {
                    System.out.println("No interview found for ID: " + interview_id);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error fetching interview details", e);
        }

        return null;
    }

}
