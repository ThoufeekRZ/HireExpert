package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.List;

import DTO.InterviewDetails;
import com.mysql.cj.xdevapi.DbDoc;
import org.json.JSONArray;
import org.json.JSONObject;

import DTO.Recruit;
import DTO.Requirement;
import DTO.Skill;

public class RecruitDAO {
	public static Connection conn;
	private static final Logger logger = Logger.getLogger(RecruitDAO.class.getName());

    static {
        try {
            conn = DBConnection.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

//	public static Recruit getRecruitById(int recruitId) {
//
//		String recruitQuery = "SELECT r.recruitId, r.recruitName, r.requirementId, r.maximumNumber, "
//				+ "req.experience, req.qualification " + "FROM recruit r "
//				+ "JOIN requirement req ON r.requirementId = req.requirementId " + "WHERE r.recruitId = ?";
//
//		try (PreparedStatement pstmtRecruit = conn.prepareStatement(recruitQuery)) {
//			pstmtRecruit.setInt(1, recruitId);
//			try (ResultSet rsRecruit = pstmtRecruit.executeQuery()) {
//				if (rsRecruit.next()) {
//					int requirementId = rsRecruit.getInt("requirementId");
//					Requirement requirement = new Requirement(
////                requirementId,
//							rsRecruit.getInt("experience"), rsRecruit.getString("qualification"),
//							getSkillsForRequirement(requirementId));
//					requirement.setRequirementId(requirementId);
//
//					Recruit recruit = new Recruit(
//							rsRecruit.getString("recruitName"),
//							rsRecruit.getInt("maximumNumber"));
//					recruit.setRecruitId(rsRecruit.getInt("recruitId"));
//					recruit.setRequirement(requirement);
//
//					return recruit;
//				}
//			}
//		} catch (SQLException e) {
//			System.err.println("Error fetching Recruit: " + e.getMessage());
//		}
//		return null;
//	}
public static Recruit getRecruitById(int recruitId) throws SQLException {
	String recruitQuery = "SELECT r.recruitId, r.recruitName, r.description, r.requirementId, r.maximumNumber, r.createdAt, r.accepted, r.reject, r.mailSented, r.status, r.total_Resume, r.emailId, "
			+ "req.experience, req.qualification "
			+ "FROM recruit r "
			+ "JOIN requirement req ON r.requirementId = req.requirementId "
			+ "WHERE r.recruitId = ?";

	if (conn == null || conn.isClosed()) {
		conn = DBConnection.getConnection();
	}

	try (PreparedStatement pstmtRecruit = conn.prepareStatement(recruitQuery)) {
		pstmtRecruit.setInt(1, recruitId);
		try (ResultSet rsRecruit = pstmtRecruit.executeQuery()) {
			if (rsRecruit.next()) {
				int requirementId = rsRecruit.getInt("requirementId");
				int emailId = rsRecruit.getInt("emailId");

				// Create Requirement object
				Requirement requirement = new Requirement(
						rsRecruit.getInt("experience"),
						rsRecruit.getString("qualification"),
						getSkillsForRequirement(requirementId)
				);
				requirement.setRequirementId(requirementId);

				// Convert Timestamp to LocalDateTime
				LocalDateTime createdAt = null;
				Timestamp createdAtTimestamp = rsRecruit.getTimestamp("createdAt");
				if (createdAtTimestamp != null) {
					createdAt = createdAtTimestamp.toLocalDateTime();
				}

				// Create Recruit object
				Recruit recruit = new Recruit(
						rsRecruit.getString("recruitName"),
						rsRecruit.getInt("maximumNumber")
				);
				recruit.setRecruitId(rsRecruit.getInt("recruitId"));
				recruit.setRequirement(requirement);
				recruit.setCreatedAt(createdAt);

				recruit.setDescription(rsRecruit.getString("description"));
				recruit.setAccepted(rsRecruit.getInt("accepted"));
				recruit.setReject(rsRecruit.getInt("reject"));
				recruit.setMailSented(rsRecruit.getInt("mailSented"));
				recruit.setTotal_Resume(rsRecruit.getInt("total_Resume"));
				recruit.setStatus(rsRecruit.getString("status"));

				// Fetch InterviewDetails only after closing the result set
				if (emailId != 0) {
					InterviewDetails details = InterviewDetailsDAO.getInterviewDetailsById(emailId);
					recruit.setInterviewDetails(details);
				}

				return recruit;
			}
		}
	} catch (SQLException e) {
		System.err.println("Error fetching Recruit: " + e.getMessage());
	}
	return null;
}


	private static List<Skill> getSkillsForRequirement(int requirementId) {
		List<Skill> skills = new ArrayList<>();
		String skillQuery = "SELECT s.skillId, s.skillName " + "FROM skills s "
				+ "JOIN requirement_skill rs ON s.skillId = rs.skillId " + "WHERE rs.requirementId = ?";

		try (PreparedStatement pstmtSkill = conn.prepareStatement(skillQuery)) {
			pstmtSkill.setInt(1, requirementId);
			try (ResultSet rsSkill = pstmtSkill.executeQuery()) {
				while (rsSkill.next()) {
					Skill skill = new Skill(rsSkill.getString("skillName"));
					skill.setSkillId(rsSkill.getInt("skillId"));
					skills.add(skill);
				}
			}
		} catch (SQLException e) {
			System.err.println("Error fetching Skills: " + e.getMessage());
		}
		return skills;
	}
	
	public static boolean insertRecruit(Recruit recruit,String userId) {
		String recruitQuery = "INSERT INTO recruit (recruitName, requirementId, userId, maximumNumber,description) VALUES (?, ?, ?, ?,?)";
        String requirementQuery = "INSERT INTO requirement (experience, qualification) VALUES (?, ?)";
        String skillQuery = "INSERT INTO requirement_skill (requirementId, skillId) VALUES (?, ?)";

        PreparedStatement pstmtRecruit = null;
        PreparedStatement pstmtRequirement = null;
        PreparedStatement pstmtSkill = null;
        ResultSet generatedKeys = null;

        try {
            conn.setAutoCommit(false);

            pstmtRequirement = conn.prepareStatement(requirementQuery, Statement.RETURN_GENERATED_KEYS);
            pstmtRequirement.setInt(1, recruit.getRequirement().getExperienceYear());
            pstmtRequirement.setString(2, recruit.getRequirement().getQualification());
            pstmtRequirement.executeUpdate();

            generatedKeys = pstmtRequirement.getGeneratedKeys();
            int requirementId;
            if (generatedKeys.next()) {
                requirementId = generatedKeys.getInt(1);
            } else {
                throw new SQLException("Failed to get generated requirementId");
            }

            pstmtRecruit = conn.prepareStatement(recruitQuery, Statement.RETURN_GENERATED_KEYS);
            pstmtRecruit.setString(1, recruit.getRecruitName());
            pstmtRecruit.setInt(2, requirementId);
            pstmtRecruit.setString(3, userId);
            pstmtRecruit.setInt(4, recruit.getMaximumNumber());
            pstmtRecruit.setString(5, recruit.getDescription());
            pstmtRecruit.executeUpdate();

            pstmtSkill = conn.prepareStatement(skillQuery);
            for (Skill skill : recruit.getRequirement().getSkills()) {
                pstmtSkill.setInt(1, requirementId);
                pstmtSkill.setInt(2, skill.getSkillId());
                pstmtSkill.addBatch();
            }
            pstmtSkill.executeBatch();           
            conn.commit();
            return true;

        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException rollbackEx) {
                    rollbackEx.printStackTrace();
                }
            }
            e.printStackTrace();
            return false;

        } 
//        finally {
//            try {
//                if (generatedKeys != null) generatedKeys.close();
//                if (pstmtRequirement != null) pstmtRequirement.close();
//                if (pstmtRecruit != null) pstmtRecruit.close();
//                if (pstmtSkill != null) pstmtSkill.close();
//                if (conn != null) conn.setAutoCommit(true);
//            } catch (SQLException closeEx) {
//                closeEx.printStackTrace();
//            }
//        }
    }
	
//	public static JSONArray getAllRecruits() {
//		
////	    List<Recruit> recruits = new ArrayList<>();
//	    JSONArray jsonArray=new JSONArray();
//	    JSONObject jsonObject=new JSONObject();
//	    
//
//	    String recruitIdQuery = "SELECT recruitId FROM recruit"; // First, get all recruit IDs
//
//	    try (PreparedStatement pstmt = conn.prepareStatement(recruitIdQuery);
//	         ResultSet rs = pstmt.executeQuery()) {
//
//	        while (rs.next()) {
//	            int recruitId = rs.getInt("recruitId");
//	            Recruit recruit = getRecruitById(recruitId); // Use getRecruitById to get full recruit details
//	            System.out.println(recruit);
//	            if (recruit != null) {
//	                recruits.add(recruit);
//	            }
//	        }
//	    } catch (SQLException e) {
//	        System.err.println("Error fetching Recruit IDs: " + e.getMessage());
//	    }
//
//	    return recruits;
//	}
	public static JSONArray getAllRecruits(String userId) throws SQLException {
	    JSONArray jsonArray = new JSONArray();

		String recruitQuery = "SELECT r.recruitId, r.recruitName, r.createdAt, r.maximumNumber, r.status, r.description, req.experience, r.accepted, r.reject, r.mailSented, r.total_Resume "
				+ "FROM recruit r " + "LEFT JOIN requirement req ON r.requirementId = req.requirementId "
				+ "WHERE r.userId = ?";


		if(conn.isClosed()){
			conn = DBConnection.getConnection();
		}

	    try (PreparedStatement pstmt = conn.prepareStatement(recruitQuery)){


			pstmt.setString(1, userId);

	         ResultSet rs = pstmt.executeQuery();

	        while (rs.next()) {
	            JSONObject jsonObject = new JSONObject();
	            jsonObject.put("recruitId", rs.getInt("recruitId"));
	            jsonObject.put("recruitName", rs.getString("recruitName"));
				jsonObject.put("max-hire",rs.getInt("maximumNumber"));
	            jsonObject.put("createdAt", rs.getTimestamp("createdAt").toString()); // Convert to String
				jsonObject.put("description", rs.getString("description"));
				jsonObject.put("accepted", rs.getInt("accepted"));
				jsonObject.put("rejected",rs.getInt("reject"));
				jsonObject.put("mailSented",rs.getInt("mailSented"));
				jsonObject.put("total_Resume",rs.getInt("total_Resume"));
	            jsonArray.put(jsonObject);
	        }
	    } catch (SQLException e) {
	        System.err.println("Error fetching recruits: " + e.getMessage());
	    }

	    return jsonArray;
	}

	public static boolean updateRecruit(JSONObject jsonObject, String userId) {
		System.out.println(jsonObject);
		System.out.println(userId);
		String recruitName = jsonObject.getString("title");
		int maximumNumber = jsonObject.getInt("maximumRecruit");
		String description = jsonObject.getString("jobDescription");
		int experience = jsonObject.getInt("experience");
		String qualification = jsonObject.getString("newQualification");
		int recruitId = jsonObject.getInt("recruitId");
		JSONArray skills = jsonObject.getJSONArray("selectedSkills");

//	    Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			conn.setAutoCommit(false); // Start transaction

			// Step 1: Get requirementId from recruit table
			int requirementId = -1;
			String selectQuery = "SELECT requirementId FROM recruit WHERE recruitId = ? AND userId = ?";
			pstmt = conn.prepareStatement(selectQuery);
			pstmt.setInt(1, recruitId);
			pstmt.setString(2, userId);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				requirementId = rs.getInt("requirementId");
			} else {
				return false; // Recruit ID not found for the user
			}

			// Step 2: Update recruit table
			String updateRecruitQuery = "UPDATE recruit SET recruitName=?, maximumNumber=?, description=? WHERE recruitId=? AND userId=?";
			pstmt = conn.prepareStatement(updateRecruitQuery);
			pstmt.setString(1, recruitName);
			pstmt.setInt(2, maximumNumber);
			pstmt.setString(3, description);
			pstmt.setInt(4, recruitId);
			pstmt.setString(5, userId);
			pstmt.executeUpdate();

			// Step 3: Update requirement table
			String updateRequirementQuery = "UPDATE requirement SET experience=?, qualification=? WHERE requirementId=?";
			pstmt = conn.prepareStatement(updateRequirementQuery);
			pstmt.setInt(1, experience);
			pstmt.setString(2, qualification);
			pstmt.setInt(3, requirementId);
			pstmt.executeUpdate();

			// Step 4: Delete old skills from requirement_skill table
			String deleteSkillsQuery = "DELETE FROM requirement_skill WHERE requirementId=?";
			pstmt = conn.prepareStatement(deleteSkillsQuery);
			pstmt.setInt(1, requirementId);
			pstmt.executeUpdate();

			// Step 5: Insert new skills into requirement_skill table
//	        String insertSkillsQuery = "INSERT INTO requirement_skill (requirementId, skillId) VALUES (?, ?)";
//	        pstmt = conn.prepareStatement(insertSkillsQuery);
//	        for (int i = 0; i < skills.length(); i++) {
//	            int skillId = skills.getInt(i);
//	            pstmt.setInt(1, requirementId);
//	            pstmt.setInt(2, skillId);
//	            pstmt.addBatch();
//	        }
//	        pstmt.executeBatch();
			String getSkillIdQuery = "SELECT skillId FROM skills WHERE skillName = ?";
			String insertSkillsQuery = "INSERT INTO requirement_skill (requirementId, skillId) VALUES (?, ?)";

			pstmt = conn.prepareStatement(insertSkillsQuery);
			PreparedStatement skillStmt = conn.prepareStatement(getSkillIdQuery);

			for (int i = 0; i < skills.length(); i++) {
				String skillName = skills.getString(i);

				// Fetch skillId from skills table
				skillStmt.setString(1, skillName);
				ResultSet skillRs = skillStmt.executeQuery();

				if (skillRs.next()) {
					int skillId = skillRs.getInt("skillId");

					// Insert into requirement_skill table
					pstmt.setInt(1, requirementId);
					pstmt.setInt(2, skillId);
					pstmt.addBatch();
				}
				skillRs.close();
			}

			pstmt.executeBatch();
			skillStmt.close();

			conn.commit(); // Commit transaction
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			if (conn != null) {
				try {
					conn.rollback(); // Rollback on error
				} catch (SQLException ex) {
					ex.printStackTrace();
				}
			}
			return false;
		}



	}

	public static boolean updateRecruitStatus(int recruitId, String status) throws SQLException {
		String updateQuery = "UPDATE recruit SET status = ? WHERE recruitId = ?";
		boolean isUpdated = false;

		if(conn != null && conn.isClosed()) {
			conn = DBConnection.getConnection();
		}



		try (PreparedStatement pstmt = conn.prepareStatement(updateQuery)) {
			pstmt.setString(1, status);
			pstmt.setInt(2, recruitId);
			int rowsAffected = pstmt.executeUpdate();

			if (rowsAffected > 0) {
				isUpdated = true;
				System.out.println(status);
				logger.log(Level.INFO, "Recruit ID {0} status updated to '{1}'.", new Object[]{recruitId, status});
			} else {
				logger.log(Level.WARNING, "Recruit ID {0} not found.", recruitId);
			}

		} catch (SQLException e) {
			logger.log(Level.SEVERE, "Error updating recruit status: {0}", e.getMessage());
		}

		return isUpdated;
	}


	public static void updateAdditonalData(int recruitId,int emailSented,int accepted,int rejected,int total_Resume) throws SQLException {

		if(conn == null || conn.isClosed()){
			conn = DBConnection.getConnection();
		}

		try {
			PreparedStatement stm = conn.prepareStatement("UPDATE recruit SET total_Resume = ?,mailSented =?,accepted = ?,reject =? WHERE recruitId = ?");
			stm.setInt(1, total_Resume);
			stm.setInt(2,emailSented);
			stm.setInt(3, accepted);
			stm.setInt(4, rejected);
			stm.setInt(5, recruitId);
			int result = stm.executeUpdate();

			if(result == 1) {
				System.out.println("update succesfully");
			}
			else {
				System.out.println("update failed");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}


	public static JSONObject getRecruitAdditionalDetails(int recruitId) throws SQLException {
		if(conn == null || conn.isClosed()){
			conn = DBConnection.getConnection();
		}
		JSONObject additionalDetails = new JSONObject();

		String query = "select accepted, reject, mailSented, total_Resume from recruit where recruitId = ?";
		try (PreparedStatement pstmt = conn.prepareStatement(query)) {
			pstmt.setInt(1, recruitId);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				int accepted = rs.getInt("accepted");
				int rejected = rs.getInt("reject");
				int mailSented = rs.getInt("mailSented");
				int total_Resume = rs.getInt("total_Resume");
				additionalDetails.put("accepted", accepted);
				additionalDetails.put("reject", rejected);
				additionalDetails.put("mailSented", mailSented);
				additionalDetails.put("total_Resume", total_Resume);

			}
			return additionalDetails;
		}
	}

}
