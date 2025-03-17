package controllers;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import jakarta.servlet.annotation.WebServlet;
import org.json.JSONObject;

import dao.DBConnection;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class ChangeStatus
 */
//@WebServlet("/ChangeStatus")
    @WebServlet("/ChangeStatus")
public class ChangeStatus extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * Default constructor.
     */
    public ChangeStatus() {
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("doGet called");

        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        JSONObject jsonResponse = new JSONObject();

        try (Connection conn = DBConnection.getConnection()) { // Auto-close the connection

            // Retrieve parameters from the request URL
            int recruitId = Integer.parseInt(request.getParameter("recruitId"));
            String email = request.getParameter("email");
            String status = request.getParameter("status");

            int resumeId = getResumeIdByEmail(email);

            System.out.println(recruitId);
            System.out.println(email);
            System.out.println(status);

            if (email == null || status == null || email.isEmpty() || status.isEmpty()) {
                jsonResponse.put("success", false);
                jsonResponse.put("message", "Missing parameters");
                out.print(jsonResponse.toString());
                return;
            }

            // Update status in the database
            if (updateSuccess(conn, recruitId, status,resumeId)==1) {
                jsonResponse.put("success", true);
                jsonResponse.put("message", "Status updated successfully.");
            } else {
                jsonResponse.put("success", false);
                jsonResponse.put("message", "Failed to update status.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            jsonResponse.put("success", false);
            jsonResponse.put("message", "Error: " + e.getMessage());
        }

        out.print(jsonResponse.toString());
        out.flush();
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("doPost");

        Connection con = null;
        try {
            Connection conn = DBConnection.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        JSONObject jsonResponse = new JSONObject();

        try {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = request.getReader().readLine()) != null) {
                sb.append(line);
            }
            JSONObject jsonRequest = new JSONObject(sb.toString());

            int recruitId = jsonRequest.getInt("recruitId");
            String email = jsonRequest.getString("email");
            String status = jsonRequest.getString("status");

            int resumeId = getResumeIdByEmail(email);

            if (email == null || status == null) {
                jsonResponse.put("success", false);
                jsonResponse.put("message", "Missing parameters");
                out.print(jsonResponse.toString());
                return;
            }


            Connection conn = DBConnection.getConnection();


            if (updateSuccess(conn,recruitId,status,resumeId) == 2) {
                jsonResponse.put("success", true);
                jsonResponse.put("message", "Status updated successfully.");
            } else if (updateSuccess(conn,recruitId,status,resumeId) == 1) {
                jsonResponse.put("success", false);
                jsonResponse.put("message", "You already accepted/rejected this offer");
            } else {
                jsonResponse.put("success", false);
                jsonResponse.put("message", "Failed to update status.");
            }

            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
            jsonResponse.put("success", false);
            jsonResponse.put("message", "Error: " + e.getMessage());
        }

        out.print(jsonResponse.toString());
        out.flush();
    }

    private int updateSuccess(Connection conn,int recruitId, String status,int resumeId) throws SQLException {

        try {

            String query0 = "select isEmailSentStatus from recr_resu_relation where recruitId=? and resumeId=?";
            PreparedStatement ps = conn.prepareStatement(query0);
            ps.setInt(1, recruitId);
            ps.setInt(2, resumeId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String isEmailSentStatus = rs.getString("isEmailSentStatus");
                if (isEmailSentStatus.equals("InterviewConfirmed") || isEmailSentStatus.equals("Declined")) {
                    return 1;
                }
            }

            String query = "update recr_resu_relation set isEmailSentStatus = ? where recruitId = ? and resumeId = ?";



            if (status.equals("Accepted")) {
                status = "InterviewConfirmed";
            } else if (status.equals("Rejected")) {
                status = "Declined";
            }

            PreparedStatement pstm = conn.prepareStatement(query);
            pstm.setString(1, status);
            pstm.setInt(2, recruitId);
            pstm.setInt(3, resumeId);
            int success = pstm.executeUpdate();
            pstm.close();
            if (success == 1) {
                return 2;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private int getResumeIdByEmail(String email) throws SQLException {
        String query = "SELECT r.resumeId " +
                "FROM resume r " +
                "JOIN contactDetails cd ON r.contactId = cd.contactId " +
                "WHERE cd.email = ?";
        PreparedStatement pstm = DBConnection.getConnection().prepareStatement(query);
        pstm.setString(1, email);
        ResultSet rs = pstm.executeQuery();
        if (rs.next()) {
            return rs.getInt("resumeId");
        }
        return 0;
    }


}
