package controllers;

import java.io.BufferedReader;
import java.io.IOException;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import jakarta.servlet.annotation.WebServlet;
import org.json.JSONObject;

import dao.DBConnection;
import dao.RecruitDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.EmailService;

/**
 * Servlet implementation class SendEmails
 */
@WebServlet("/SendEmails")
public class SendEmails extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public SendEmails() {
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
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("inside all emails");
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		JSONObject jsonResponse = new JSONObject();

		try {
			BufferedReader reader = request.getReader();
			StringBuilder requestBody = new StringBuilder();
			String line;

			while ((line = reader.readLine()) != null) {
				requestBody.append(line);
			}
			JSONObject jsonObject = new JSONObject(requestBody.toString());
			String status = jsonObject.getString("sendEmailTo");
			int recruitId = jsonObject.getInt("recruitId");
			String subject = jsonObject.getString("subject");
			String emailBody = jsonObject.getString("body");

			List<String> emails = EmailService.getSelectedFinalizedEmails(status, recruitId);

			try(Connection conn = DBConnection.getConnection()) {

				String query = "update recruit set mailSented = ?, status = 'Closed' where recruitId = ?";
				PreparedStatement ps = conn.prepareStatement(query);
				ps.setInt(1, emails.size());
				ps.setInt(2, recruitId);
				int success = ps.executeUpdate();
				if (success > 0) {
					System.out.println("mail send updated");
				}
				else {
					System.out.println("mail send failed");
				}

			} catch (Exception e) {
				throw new RuntimeException(e);
			}

			jsonResponse.put("success", true);
			jsonResponse.put("status", "success");
			jsonResponse.put("message", "Emails retrieved successfully");
			jsonResponse.put("emails", emails); // Sending emails as JSON array

		} catch (Exception e) {
			e.printStackTrace();
			jsonResponse.put("success", false);
			jsonResponse.put("status", "error");
			jsonResponse.put("message", "An error occurred: " + e.getMessage());
		}

		response.getWriter().write(jsonResponse.toString());
	}


	public static void resetTables(int recruitId) {
		String conditionalDeleteQuery = "DELETE FROM resume WHERE resumeId IN (SELECT resumeId FROM recr_resu_relation  GROUP BY resumeId HAVING COUNT(DISTINCT recruitId) = 1 AND MAX(recruitId) = ?) AND isEmailSentStatus='Declined'";

		String[] dependentDeletes = {
				"DELETE FROM resume_experience WHERE resumeId IN (SELECT resumeId FROM resume WHERE resumeId IN (SELECT resumeId FROM recr_resu_relation GROUP BY resumeId HAVING COUNT(DISTINCT recruitId) = 1 AND MAX(recruitId) = ?))",
				"DELETE FROM contactDetails WHERE contactId IN (SELECT contactId FROM resume WHERE resumeId IN (SELECT resumeId FROM recr_resu_relation GROUP BY resumeId HAVING COUNT(DISTINCT recruitId) = 1 AND MAX(recruitId) = ?))",
				"DELETE FROM experienceDetails WHERE experienceId IN (SELECT experienceId FROM resume_experience WHERE resumeId IN (SELECT resumeId FROM recr_resu_relation GROUP BY resumeId HAVING COUNT(DISTINCT recruitId) = 1 AND MAX(recruitId) = ?))",
				"DELETE FROM recr_resu_relation WHERE recruitId = ?",
				"DELETE FROM resume_skill WHERE resumeId IN (SELECT resumeId FROM resume WHERE resumeId IN (SELECT resumeId FROM recr_resu_relation GROUP BY resumeId HAVING COUNT(DISTINCT recruitId) = 1 AND MAX(recruitId) = ?))"
		};
		try (Connection conn = DBConnection.getConnection()) {
			if (conn == null) {
				throw new SQLException("Database connection failed.");
			}

			conn.setAutoCommit(false);

			try (PreparedStatement stmt = conn.prepareStatement(conditionalDeleteQuery)) {
				stmt.setInt(1, recruitId);
				stmt.executeUpdate();
			}

			for (String query : dependentDeletes) {
				try (PreparedStatement stmt = conn.prepareStatement(query)) {
					stmt.setInt(1, recruitId);
					stmt.executeUpdate();
				}
			}

			conn.commit();
			System.out.println("Filtered deletion completed successfully.");

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}


