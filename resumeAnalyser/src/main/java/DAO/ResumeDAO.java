package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;

import DTO.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ResumeDAO {
	public static Connection conn;
	private static final Logger logger = Logger.getLogger(ResumeDAO.class.getName());


	static {
        try {
            conn = DBConnection.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

	public static boolean insertResume(String jsonString, String recruit) {
		Connection conn = null;
		PreparedStatement contactStmt = null;
		PreparedStatement experienceStmt = null;
		PreparedStatement resumeStmt = null;
		PreparedStatement linkStmt = null;

		String existingResumeSQL = "SELECT r.resumeId FROM resume r " +
				"JOIN contactDetails c ON r.contactId = c.contactId " +
				"WHERE r.candidateName = ? AND c.email = ?";



		try {
			conn = DBConnection.getConnection();
			JSONObject jsonObject = new JSONObject(jsonString);

			PreparedStatement selectExistingResume = conn.prepareStatement(existingResumeSQL);

			selectExistingResume.setString(1, jsonObject.getString("Name"));
			selectExistingResume.setString(2, jsonObject.getJSONObject("Contact Details").getString("Email"));

			int existingResumeId = 0;
			ResultSet success = selectExistingResume.executeQuery();
			if (success.next()) {
				existingResumeId = success.getInt("resumeId");
			}

			if(existingResumeId != 0){
				String relationSQL = "INSERT INTO recr_resu_relation (recruitId, resumeId) VALUES (?, ?)";
				PreparedStatement insertRelation = conn.prepareStatement(relationSQL);
				insertRelation.setString(1, recruit);
				insertRelation.setInt(2, existingResumeId);
				int successCount = insertRelation.executeUpdate();
				if(successCount == 1){
					return true;
				}
				return false;
			}


			conn.setAutoCommit(false); // ✅ Disable auto-commit


			System.out.println(jsonObject.toString());

			String candidateName = jsonObject.getString("Name");
			JSONObject contactDetails = jsonObject.getJSONObject("Contact Details");
			String email = contactDetails.getString("Email");
			String mobileNumber = contactDetails.optString("Contact Number", "");
			if (!mobileNumber.matches("^\\+?[0-9-\\s]{7,20}$")) {
				System.out.println("Invalid mobile number format: " + mobileNumber);
				mobileNumber = "UNKNOWN"; // Set default if invalid
			}
			String address = contactDetails.optString("Address", null);
			String linkedInId = contactDetails.optString("LinkedIn", null);
			JSONArray skillsArray = jsonObject.getJSONArray("Skills");
			JSONArray experienceArray = jsonObject.optJSONArray("Experience");
			String education = jsonObject.getJSONArray("Education").getJSONObject(0).getString("Degree");
			education = education.contains("Bachelor") ?"UG":"PG";
			String description = jsonObject.optString("Summary", null);

			// Insert into contactDetails table
			String contactSQL = "INSERT INTO contactDetails (email, mobileNumber, address, linkedInId) VALUES (?, ?, ?, ?)";
			contactStmt = conn.prepareStatement(contactSQL, Statement.RETURN_GENERATED_KEYS);
			contactStmt.setString(1, email);
			contactStmt.setString(2, mobileNumber);
			contactStmt.setString(3, address);
			contactStmt.setString(4, linkedInId);
			contactStmt.executeUpdate();
			ResultSet contactRs = contactStmt.getGeneratedKeys();

			int contactId = 0;
			if (contactRs.next()) {
				contactId = contactRs.getInt(1);
			}
			contactRs.close();

			// Insert into experienceDetails table
			List<Integer> experienceIds = new ArrayList<>();
			if (experienceArray != null) {
				String experienceSQL = "INSERT INTO experienceDetails (company, location, title, year, responsibilities) VALUES (?, ?, ?, ?, ?)";
				experienceStmt = conn.prepareStatement(experienceSQL, Statement.RETURN_GENERATED_KEYS);

				for (int i = 0; i < experienceArray.length(); i++) {
					JSONObject experienceDetails = experienceArray.optJSONObject(i);
					String company = experienceDetails.optString("CompanyName", null);
					String location = experienceDetails.optString("Location", null);
					String title = experienceDetails.optString("Title", null);
					int years = experienceDetails.optInt("YearsOfExperience", 0);
					JSONArray responsibilities = experienceDetails.optJSONArray("KeyResponsibilities");
					if (responsibilities == null) {
						responsibilities = new JSONArray(); // Assign an empty array as a fallback
					}
					String responsibilitiesJson = (responsibilities != null) ? responsibilities.toString() : null;

					experienceStmt.setString(1, company);
					experienceStmt.setString(2, location);
					experienceStmt.setString(3, title);
					experienceStmt.setInt(4, years);
					experienceStmt.setString(5, responsibilitiesJson);
					experienceStmt.executeUpdate();

					ResultSet experienceRs = experienceStmt.getGeneratedKeys();
					if (experienceRs.next()) {
						experienceIds.add(experienceRs.getInt(1));
					}
					experienceRs.close();
				}
			}

			// Insert into resume table
			String resumeSQL = "INSERT INTO resume (candidateName, contactId, education, description) VALUES (?, ?, ?, ?)";
			resumeStmt = conn.prepareStatement(resumeSQL, Statement.RETURN_GENERATED_KEYS);
			resumeStmt.setString(1, candidateName);
			resumeStmt.setInt(2, contactId);
			resumeStmt.setString(3, education);
			resumeStmt.setString(4, description);
			resumeStmt.executeUpdate();

			ResultSet resumeRs = resumeStmt.getGeneratedKeys();
			int resumeId = 0;
			if (resumeRs.next()) {
				resumeId = resumeRs.getInt(1);
			}
			resumeRs.close();

			//adding in recr_resume_table

			// Insert into recr_resu_relation after obtaining resumeId

			String relationSQL = "INSERT INTO recr_resu_relation (recruitId, resumeId) VALUES (?, ?)";
			PreparedStatement relationStmt = conn.prepareStatement(relationSQL);
			relationStmt.setInt(1, Integer.parseInt(recruit));
			relationStmt.setInt(2, resumeId);
			relationStmt.executeUpdate();
			relationStmt.close();


			// Link resume to multiple experiences
			if (!experienceIds.isEmpty()) {
				String linkSQL = "INSERT INTO resume_experience (resumeId, experienceId) VALUES (?, ?)";
				linkStmt = conn.prepareStatement(linkSQL);
				for (int experienceId : experienceIds) {
					linkStmt.setInt(1, resumeId);
					linkStmt.setInt(2, experienceId);
					linkStmt.addBatch();
				}
				linkStmt.executeBatch();
			}

			conn.commit(); // ✅ Commit transaction

			String insertResumeSkillQuery = "INSERT INTO resume_skill (resumeId, skillId) VALUES (?, ?)";
			String skillQuery = "SELECT skillId FROM skills WHERE skillName = ?";
			String insertSkillQuery = "INSERT INTO skills (skillName) VALUES (?)";
//

//				conn.setAutoCommit(false);

				PreparedStatement pstmtSkill = conn.prepareStatement(skillQuery);
				PreparedStatement pstmtInsertSkill = conn.prepareStatement(insertSkillQuery, Statement.RETURN_GENERATED_KEYS);
				PreparedStatement pstmtResumeSkill = conn.prepareStatement(insertResumeSkillQuery);

				for (int i = 0; i < skillsArray.length(); i++) {
					String skillName = skillsArray.getString(i);
					int skillId = -1;

					pstmtSkill.setString(1, skillName);
					ResultSet rs = pstmtSkill.executeQuery();

					if (rs.next()) {
						skillId = rs.getInt("skillId");
					} else {

						pstmtInsertSkill.setString(1, skillName);
						pstmtInsertSkill.executeUpdate();

						ResultSet rs1 = pstmtInsertSkill.getGeneratedKeys();
						if (rs1.next()) {
							skillId = rs1.getInt(1);
						}
						rs1.close();
					}
					rs.close();

					pstmtResumeSkill.setInt(1, resumeId);
					pstmtResumeSkill.setInt(2, skillId);
					pstmtResumeSkill.addBatch();
				}

				pstmtResumeSkill.executeBatch();
//				conn.commit();



			System.out.println("Resume inserted successfully with multiple experiences.");
			return true;
		} catch (SQLException e) {
			System.err.println("SQL Error: " + e.getMessage());
			if (conn != null) {
				try {
					conn.rollback(); // ✅ Rollback transaction
					System.out.println("Transaction rolled back.");
				} catch (SQLException rollbackEx) {
					rollbackEx.printStackTrace();
				}
			}
		} catch (Exception e) {
			System.err.println("Error parsing JSON: " + e.getMessage());
		} finally {
			// ✅ Close all resources
			try {
				if (contactStmt != null) contactStmt.close();
				if (experienceStmt != null) experienceStmt.close();
				if (resumeStmt != null) resumeStmt.close();
				if (linkStmt != null) linkStmt.close();
				if (conn != null) {
					conn.setAutoCommit(true); // ✅ Restore auto-commit
//				//	conn.close();
				}
			} catch (SQLException closeEx) {
				closeEx.printStackTrace();
			}
		}
		return false;
	}

	public static List<Resume> getCorresspondingResume(int recruitId) throws SQLException {
		List<Resume> resumes = new ArrayList<>();

		try (Connection connection = DBConnection.getConnection()) {
			String getAllResumeQuery = "SELECT resume.resumeId FROM resume " +
					"JOIN recr_resu_relation ON resume.resumeId = recr_resu_relation.resumeId " +
					"WHERE recr_resu_relation.recruitId = ?";

			PreparedStatement preparedStatement = connection.prepareStatement(getAllResumeQuery);
			preparedStatement.setInt(1, recruitId);

			// Collect all resumeIds first
			List<Integer> resumeIds = new ArrayList<>();
			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				while (resultSet.next()) {
					resumeIds.add(resultSet.getInt("resumeId"));
				}
			}

			// Fetch the recruit after result set is closed
			Recruit recruit = RecruitDAO.getRecruitById(recruitId);

			// Now safely call ResumeDAO
			for (int resumeId : resumeIds) {
				resumes.add(ResumeDAO.getResumeById(resumeId, recruitId));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return resumes;
	}


	public static Resume getResumeById(int resumeId, int recruitId) {
		Resume resume = null;
		Contact contact = null;
		List<Experience> experiences = new ArrayList<>();
		List<Skill> skillsArray = getSkillsByResumeId(resumeId);
		System.out.println("✅ Skills Array: " + skillsArray);

		String resumeSQL = "SELECT r.resumeId, r.candidateName, r.education, r.description, " +
				"rr.resumeScore, rr.status, rr.isEmailSentStatus, r.contactId " +
				"FROM resume r " +
				"JOIN recr_resu_relation rr ON r.resumeId = rr.resumeId " +
				"WHERE rr.recruitId = ? AND rr.resumeId = ?";

		try (Connection conn = DBConnection.getConnection()) {
			if (conn == null || conn.isClosed()) {
				throw new SQLException("❌ Connection is null or closed.");
			}
			try (PreparedStatement resumeStmt = conn.prepareStatement(resumeSQL)) {
				resumeStmt.setInt(1, recruitId);
				resumeStmt.setInt(2, resumeId);
				System.out.println("🔎 Executing query: " + resumeStmt);

				try (ResultSet resumeRs = resumeStmt.executeQuery()) {
					if (!resumeRs.next()) {
						System.out.println("❌ No resume found for recruitId: " + recruitId + ", resumeId: " + resumeId);
						return null; // Exit if no matching resume
					}

					// Extract resume information with null-checks
					int resumeID = resumeRs.getInt("resumeId");
					String candidateName = resumeRs.getString("candidateName");
					candidateName = (candidateName != null) ? candidateName : "Unknown";

					String education = resumeRs.getString("education");
					education = (education != null) ? education : "N/A";

					String description = resumeRs.getString("description");
					description = (description != null) ? description : "";

					double resumeScore = resumeRs.getDouble("resumeScore");
					String status = resumeRs.getString("status");
					status = (status != null) ? status : "Pending";

					String isEmailSentStatus = resumeRs.getString("isEmailSentStatus");
					isEmailSentStatus = (isEmailSentStatus != null) ? isEmailSentStatus : "Not Sent";

					int contactId = resumeRs.getInt("contactId");

					// Fetch contact and experiences
					contact = getContactById(conn, contactId);
					experiences = getExperiencesByResumeId(resumeId);

					// Construct the Resume object
					resume = new Resume(candidateName, contact, education, description);
					resume.setResumeId(resumeID);
					resume.setSkills(skillsArray);
					resume.setExperiences(experiences);
					resume.setResumeScore(resumeScore);
					resume.setStatus(status);
					resume.setIsEmailSentStatus(isEmailSentStatus);

					System.out.println("✅ Resume fetched successfully: " + resume);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println("❌ Error fetching resume: " + e.getMessage());
		}
		return resume;
	}


	// Fetch contact details
	private static Contact getContactById(Connection conn, int contactId) throws SQLException {
		String contactSQL = "SELECT * FROM contactDetails WHERE contactId = ?";
		try (PreparedStatement contactStmt = conn.prepareStatement(contactSQL)) {
			contactStmt.setInt(1, contactId);
			try (ResultSet contactRs = contactStmt.executeQuery()) {
				if (!contactRs.next()) {
					System.out.println("No contact found for contactId: " + contactId);
					return null;
				}
				System.out.println("Contact found: " + contactRs.getString("mobileNumber"));
				return new Contact(
						contactRs.getString("mobileNumber"),
						contactRs.getString("email"),
						contactRs.getString("linkedInId"),
						contactRs.getString("address")
				);
			}
		}
	}


	// Fetch experiences by resumeId
	private static List<Experience> getExperiencesByResumeId(int resumeId) throws SQLException {
		List<Experience> experiences = new ArrayList<>();

		// Ensure connection is valid
		Connection conn = DBConnection.getConnection();
		if (conn == null) {
			throw new SQLException("Database connection is null.");
		}

		String experienceSQL = "SELECT * FROM experienceDetails WHERE experienceId IN " +
				"(SELECT experienceId FROM resume_experience WHERE resumeId = ?)";

		try (PreparedStatement experienceStmt = conn.prepareStatement(experienceSQL)) {
			experienceStmt.setInt(1, resumeId);
			System.out.println("Executing query for resumeId: " + resumeId);
			ResultSet experienceRs = experienceStmt.executeQuery();
			try {
				if (!experienceRs.next()) {
					System.out.println("No experiences found for resumeId: " + resumeId);
					return experiences; // Return an empty list
				}

				do {
					Experience experience = new Experience(
							experienceRs.getString("company") != null ? experienceRs.getString("company") : "Unknown",
							experienceRs.getString("location") != null ? experienceRs.getString("location") : "Unknown",
							experienceRs.getInt("year")
					);
					experience.setTitle(experienceRs.getString("title") != null ? experienceRs.getString("title") : "N/A");

					// Parse responsibilities JSON safely
					String responsibilitiesJson = experienceRs.getString("responsibilities");
					JSONArray responsibilitiesArray;
					try {
						responsibilitiesArray = (responsibilitiesJson != null && !responsibilitiesJson.isEmpty())
								? new JSONArray(responsibilitiesJson)
								: new JSONArray();
					} catch (JSONException e) {
						System.err.println("Invalid JSON format for resumeId: " + resumeId);
						responsibilitiesArray = new JSONArray();
					}

					experience.setResponsibilities(getResponsibilities(responsibilitiesArray));
					experiences.add(experience);
				} while (experienceRs.next());
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Error retrieving experiences: " + e.getMessage(), e);
		}

		return experiences;
	}





//	Experience experience = null;
//	String experienceSQL = "SELECT * FROM experienceDetails WHERE experienceId IN (SELECT experienceId FROM resume_experience WHERE resumeId = ?)";
//	PreparedStatement experienceStmt = conn.prepareStatement(experienceSQL);
//	experienceStmt.setInt(1, resumeId);
//	ResultSet experienceRs = experienceStmt.executeQuery();
////    JSONArray experienceArray = new JSONArray();
//
//	while (experienceRs.next()) {
//
////        JSONObject experienceJson = new JSONObject();
//		company = experienceRs.getString("company");
//		location = experienceRs.getString("location");
//		title = experienceRs.getString("title");
//		experienceYear = experienceRs.getInt("year");
//		responsibilities = getResponsibilities(new JSONArray(experienceRs.getString("responsibilities")));
//		experience = new Experience(company, location, experienceYear);
//		experience.setTitle(title);
//		experience.setResponsibilities(responsibilities);
//
//	}
//	List<Experience> experiences = new ArrayList<>();
	public static List<String> getResponsibilities(JSONArray jsonArray) {
		List<String> responsibilitiesList = new ArrayList<>();
		System.out.println(jsonArray+"JSArayyggjg");
		try {
//            jsonArray = new JSONArray(jsonString);
			for (int i = 0; i < jsonArray.length(); i++) {
				responsibilitiesList.add(jsonArray.getString(i));
			}
		} catch (Exception e) {
			System.out.println("Error converting JSON to List<String>: " + e.getMessage());
		}
		return responsibilitiesList;
	}

	public static void updateResumeScore(int resumeId, double score, int recruitId) throws SQLException {
		String query = "UPDATE recr_resu_relation\n" +
				"SET resumeScore = ?\n" +
				"WHERE recruitId = ? AND resumeId = ?;\n";

        if(conn.isClosed()){
			conn = DBConnection.getConnection();
		}
		try(PreparedStatement stm = conn.prepareStatement(query)) {

			stm.setDouble(1, score);
			stm.setInt(2, recruitId);
			stm.setInt(3, resumeId);
			int result = stm.executeUpdate();

			if (result == 1) {
				logger.info("Resume ID " + resumeId + " updated successfully with score: " + score);
			} else {
				logger.warning("Failed to update Resume ID " + resumeId);
			}

		} catch (SQLException e) {
			logger.log(Level.SEVERE, "Error updating resume score", e);
		}
	}

//	public static List<Skill> getSkillsByResumeId(int resumeId) {
//	    List<Skill> skillsList = new ArrayList<>();
////	    Connection conn = null;
//	    PreparedStatement pstmt = null;
//	    ResultSet rs = null;
//
//	    String query = "SELECT s.skillId, s.skillName FROM skills s " +
//	                   "JOIN resume_skill rs ON s.skillId = rs.skillId " +
//	                   "WHERE rs.resumeId = ?";
//
//	    try {
////	        conn = DatabaseConnection.getConnection();
//	        pstmt = conn.prepareStatement(query);
//	        pstmt.setInt(1, resumeId);
//	        rs = pstmt.executeQuery();
//
//	        while (rs.next()) {
//	            int skillId = rs.getInt("skillId");
//	            String skillName = rs.getString("skillName");
//	            Skill skill=new Skill(skillName);
//	            skill.setSkillId(skillId);
//	            skillsList.add(skill);
//	        }
//	    } catch (SQLException e) {
//	        e.printStackTrace();
//	    } finally {
//	        try {
//	            if (rs != null) rs.close();
//	            if (pstmt != null) pstmt.close();
////	            if (conn != null) conn.close();
//	        } catch (SQLException e) {
//	            e.printStackTrace();
//	        }
//	    }
//	    return skillsList;
//	}
	
	public static List<Skill> getSkillsByResumeId(int resumeId) {
		System.out.println("InsideRS");
	    List<Skill> skillsList = new ArrayList<>();
//	    Connection connection = null;
	    PreparedStatement pstmt = null;
	    ResultSet rs = null;

	    String query = "SELECT s.skillId, s.skillName FROM skills s " +
	                   "JOIN resume_skill rs ON s.skillId = rs.skillId " +
	                   "WHERE rs.resumeId = ?";

	    try{

			if(conn.isClosed()){
				conn = DBConnection.getConnection();
			}

	    	System.out.println("InsideRS1");
//	        connection = DBConnection.getConnection(); // Ensure connection is initialized
	        pstmt = conn.prepareStatement(query);
	        pstmt.setInt(1, resumeId);
	        rs = pstmt.executeQuery();

	        while (rs.next()) {
//	        	System.out.println("InsideRS2");
	            int skillId = rs.getInt("skillId");
	            String skillName = rs.getString("skillName");
	            Skill skill = new Skill(skillName);
	            skill.setSkillId(skillId);
	            System.out.println("skill : "+skill);
	            skillsList.add(skill);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace(); // Log the error
	    } 
	   
	    return skillsList;
	}

	public static void updateResumeStatus(int resumeId, int recruitId, String status) throws SQLException {
		if (conn == null || conn.isClosed()) {
			conn = DBConnection.getConnection();
		}

		boolean nextAction = true;
		String oldStatus = null;

		String query1 = "SELECT status FROM recr_resu_relation WHERE resumeId = ? AND recruitId = ?";
		String query2 = "UPDATE recr_resu_relation SET status = ? WHERE resumeId = ? AND recruitId = ?";

		try (PreparedStatement stm = conn.prepareStatement(query1)) {
			stm.setInt(1, resumeId);
			stm.setInt(2, recruitId);

			logger.info("Executing: " + stm);

			try (ResultSet rs = stm.executeQuery()) {
				if (rs.next()) {
					oldStatus = rs.getString("status");
					logger.info("Current status for resumeId=" + resumeId + " is: " + oldStatus);
				}
			}
		}

		if (oldStatus == null) {
			logger.warning("No record found for resumeId=" + resumeId + " and recruitId=" + recruitId);
			return; // Exit if no matching record found
		}

		if (oldStatus.equals(status)) {
			nextAction = false;
			logger.info("Status unchanged. No update required for resumeId=" + resumeId);
		}

		if (nextAction) {
			try (PreparedStatement updateStmt = conn.prepareStatement(query2)) {
				updateStmt.setString(1, status);
				updateStmt.setInt(2, resumeId);
				updateStmt.setInt(3, recruitId);

				logger.info("Executing: " + updateStmt);

				int result = updateStmt.executeUpdate();

				logger.info("Old Status: " + oldStatus + ", New Status: " + status);
				logger.info("Condition Check: " + (status.equals("Shortlisted") && oldStatus.equals("Rejected")) + " or " + (status.equals("Rejected") && oldStatus.equals("Shortlisted")));

				if (result == 1) {
					logger.info("Updated status to '" + status + "' for resumeId=" + resumeId);

					if (status.equalsIgnoreCase("Shortlisted") && oldStatus.equalsIgnoreCase("Rejected")) {
						logger.info("Calling updateRecruitCounts for resumeId=" + resumeId + ", recruitId=" + recruitId + ", status=" + status);
						updateRecruitCounts(conn, resumeId, 1, -1, status);
					} else if (status.equalsIgnoreCase("Rejected") && oldStatus.equalsIgnoreCase("Shortlisted")) {
						logger.info("Calling updateRecruitCounts for resumeId=" + resumeId + ", recruitId=" + recruitId + ", status=" + status);

						updateRecruitCounts(conn, resumeId, -1, 1, status);
					}
					else if(status.equalsIgnoreCase("Rejected") && oldStatus.equalsIgnoreCase("Reviewed")){
						updateRecruitCounts(conn, resumeId, 0, 1, status);
					}
				} else {
					logger.warning("Failed to update status for resumeId=" + resumeId);
				}
			}
		}
	}

	private static void updateRecruitCounts(Connection conn, int resumeId, int acceptedChange, int rejectedChange, String status) throws SQLException {
		logger.info("updateRecruitCounts called with resumeId=" + resumeId + ", acceptedChange=" + acceptedChange + ", rejectedChange=" + rejectedChange + ", status=" + status);

		String updateRecruitExtraInfo = "SELECT recruitId FROM recr_resu_relation WHERE resumeId=?";
		try (PreparedStatement updateRecruitStmt = conn.prepareStatement(updateRecruitExtraInfo)) {
			updateRecruitStmt.setInt(1, resumeId);
			ResultSet updateRecruitRs = updateRecruitStmt.executeQuery();

			if (updateRecruitRs.next()) {
				int recruitId = updateRecruitRs.getInt("recruitId");
				logger.info("Found recruitId=" + recruitId + " for resumeId=" + resumeId);

				// Fetch current accepted and rejected counts
				String getDataString = "SELECT accepted, reject FROM recruit WHERE recruitId = ?";
				try (PreparedStatement getDataStmt = conn.prepareStatement(getDataString)) {
					getDataStmt.setInt(1, recruitId);
					ResultSet rs = getDataStmt.executeQuery();

					if (rs.next()) {
						int accepted = rs.getInt("accepted");
						int rejected = rs.getInt("reject");
						logger.info("Before update: recruitId=" + recruitId + ", accepted=" + accepted + ", rejected=" + rejected);

						// Update counts
						accepted = Math.max(accepted + acceptedChange, 0); // Prevent negative values
						rejected = Math.max(rejected + rejectedChange, 0);

						updateRecruitDetails(conn, recruitId, accepted, rejected);
						logger.info("After update: recruitId=" + recruitId + ", accepted=" + accepted + ", rejected=" + rejected);
					} else {
						logger.warning("No data found for recruitId=" + recruitId + " in recruit table.");
					}
				}
			} else {
				logger.warning("No recruitId found for resumeId=" + resumeId + " in recr_resu_relation.");
			}
		}
	}


	private static void updateRecruitDetails(Connection conn, int recruitId, int acceptedChange, int rejectedChange) throws SQLException {
		String getCurrentCounts = "SELECT accepted, reject FROM recruit WHERE recruitId = ?";
		String updateRecruitQuery = "UPDATE recruit SET accepted = ?, reject = ? WHERE recruitId = ?";

		try (PreparedStatement getCountsStmt = conn.prepareStatement(getCurrentCounts)) {
			getCountsStmt.setInt(1, recruitId);

			logger.info("Executing: " + getCountsStmt);

			try (ResultSet rs = getCountsStmt.executeQuery()) {
				if (rs.next()) {
					int accepted = rs.getInt("accepted");
					int rejected = rs.getInt("reject");

					accepted = Math.max(accepted + acceptedChange, 0);
					rejected = Math.max(rejected + rejectedChange, 0);

					logger.info("Updating recruitId=" + recruitId + " to accepted=" + accepted + ", rejected=" + rejected);

					try (PreparedStatement updateStmt = conn.prepareStatement(updateRecruitQuery)) {
						updateStmt.setInt(1, accepted);
						updateStmt.setInt(2, rejected);
						updateStmt.setInt(3, recruitId);

						logger.info("Executing: " + updateStmt);

						updateStmt.executeUpdate();
					}
				} else {
					logger.warning("Recruit record not found for recruitId=" + recruitId);
				}
			}
		}
	}

	public static void toggleIsSelected(int resumeId) {

		try {
	        String selectSQL = "SELECT isSelected FROM resume WHERE resumeId = ?";
	        PreparedStatement selectStmt = conn.prepareStatement(selectSQL);
	        selectStmt.setInt(1, resumeId);
	        ResultSet rs = selectStmt.executeQuery();
	        
	        if (rs.next()) {
	            boolean currentStatus = rs.getBoolean("isSelected");
	            boolean newStatus = !currentStatus; 

	            String updateSQL = "UPDATE resume SET isSelected = ? WHERE resumeId = ?";
	            PreparedStatement updateStmt = conn.prepareStatement(updateSQL);
	            updateStmt.setBoolean(1, newStatus);
	            updateStmt.setInt(2, resumeId);
	            
	            int rowsAffected = updateStmt.executeUpdate();
	            if (rowsAffected > 0) {
	                System.out.println("isSelected toggled successfully for resumeId: " + resumeId);
	            } else {
	                System.out.println("Update failed for resumeId: " + resumeId);
	            }

//	            updateStmt.close();
	        } else {
	            System.out.println("No record found for resumeId: " + resumeId);
	        }
	        
//	        rs.close();
//	        selectStmt.close();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	
	public static void toggleIsFinalized(int resumeId) {

		try {
	        String selectSQL = "SELECT isFinalized FROM resume WHERE resumeId = ?";
	        PreparedStatement selectStmt = conn.prepareStatement(selectSQL);
	        selectStmt.setInt(1, resumeId);
	        ResultSet rs = selectStmt.executeQuery();
	        
	        if (rs.next()) {
	            boolean currentStatus = rs.getBoolean("isFinalized");
	            boolean newStatus = !currentStatus; 

	            String updateSQL = "UPDATE resume SET isFinalized = ? WHERE resumeId = ?";
	            PreparedStatement updateStmt = conn.prepareStatement(updateSQL);
	            updateStmt.setBoolean(1, newStatus);
	            updateStmt.setInt(2, resumeId);
	            
	            int rowsAffected = updateStmt.executeUpdate();
	            if (rowsAffected > 0) {
	                System.out.println("isFinalized toggled successfully for resumeId: " + resumeId);
	            } else {
	                System.out.println("Update failed for resumeId: " + resumeId);
	            }

//	            updateStmt.close();
	        } else {
	            System.out.println("No record found for resumeId: " + resumeId);
	        }
	        
//	        rs.close();
//	        selectStmt.close();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}




	


}
