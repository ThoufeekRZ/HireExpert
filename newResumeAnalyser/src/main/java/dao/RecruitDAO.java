package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import models.Recruit;
import models.Requirement;
import models.Skill;

public class RecruitDAO {


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
public static Recruit getRecruitById(int recruitId) {
	String recruitQuery = "SELECT r.recruitId, r.recruitName, r.description, r.requirementId, r.maximumNumber, r.createdAt, r.accepted, r.reject, r.mailSented, r.status, r.total_Resume, r.emailId, "
			+ "req.experience, req.qualification "
			+ "FROM recruit r "
			+ "JOIN requirement req ON r.requirementId = req.requirementId "
			+ "WHERE r.recruitId = ?";

	try (Connection conn = DBConnection.getConnection();
		 PreparedStatement pstmtRecruit = conn.prepareStatement(recruitQuery)) {

		pstmtRecruit.setInt(1, recruitId);
		try (ResultSet rsRecruit = pstmtRecruit.executeQuery()) {
			if (rsRecruit.next()) {
				// ✅ Extract everything BEFORE calling another method
				int requirementId = rsRecruit.getInt("requirementId");
				int experience = rsRecruit.getInt("experience");
				String qualification = rsRecruit.getString("qualification");

				// ✅ Store recruit information
				String recruitName = rsRecruit.getString("recruitName");
				int maximumNumber = rsRecruit.getInt("maximumNumber");
				Timestamp createdAtTimestamp = rsRecruit.getTimestamp("createdAt");
				LocalDateTime createdAt = (createdAtTimestamp != null) ? createdAtTimestamp.toLocalDateTime() : null;
				String description = rsRecruit.getString("description");
				int accepted = rsRecruit.getInt("accepted");
				int reject = rsRecruit.getInt("reject");
				int mailSented = rsRecruit.getInt("mailSented");
				int totalResume = rsRecruit.getInt("total_Resume");
				String status = rsRecruit.getString("status");

				// ✅ Get skills AFTER ResultSet is processed
				List<Skill> skills = getSkillsForRequirement(requirementId);

				// ✅ Create Requirement object
				Requirement requirement = new Requirement(experience, qualification, skills);
				requirement.setRequirementId(requirementId);

				// ✅ Create Recruit object
				Recruit recruit = new Recruit(recruitName, maximumNumber);
				recruit.setRecruitId(recruitId);
				recruit.setRequirement(requirement);
				recruit.setCreatedAt(createdAt);
				recruit.setDescription(description);
				recruit.setAccepted(accepted);
				recruit.setReject(reject);
				recruit.setMailSented(mailSented);
				recruit.setTotal_Resume(totalResume);
				recruit.setStatus(status);

				return recruit;
			}
		}
	} catch (SQLException e) {
		e.printStackTrace();
	}
	return null;
}


	private static List<Skill> getSkillsForRequirement(int requirementId) {
		List<Skill> skills = new ArrayList<>();
		String skillQuery = "SELECT s.skillId, s.skillName " +
				"FROM skills s " +
				"JOIN requirement_skill rs ON s.skillId = rs.skillId " +
				"WHERE rs.requirementId = ?";

		try (Connection conn = DBConnection.getConnection();
			 PreparedStatement pstmtSkill = conn.prepareStatement(skillQuery)) {

			pstmtSkill.setInt(1, requirementId);
			System.out.println("Executing query with requirementId: " + requirementId);

			try (ResultSet rsSkill = pstmtSkill.executeQuery()) {
				// ✅ Ensure there are results before accessing columns
				if (!rsSkill.isBeforeFirst()) {
					System.out.println("No skills found for requirementId: " + requirementId);
					return skills;  // Return empty list if no rows are found
				}

				while (rsSkill.next()) {
					System.out.println("Found skill: " + rsSkill.getString("skillName"));
					Skill skill = new Skill(rsSkill.getString("skillName"));
					skill.setSkillId(rsSkill.getInt("skillId"));
					skills.add(skill);
				}
			}
		} catch (SQLException e) {
			System.err.println("Error fetching Skills: " + e.getMessage());
			e.printStackTrace();
		}
		return skills;  // Return an empty list if no rows are fetched
	}



	public static boolean insertRecruit(Recruit recruit, String userId) {
		String recruitQuery = "INSERT INTO recruit (recruitName, requirementId, userId, maximumNumber,description) VALUES (?, ?, ?, ?,?)";
		String requirementQuery = "INSERT INTO requirement (experience, qualification) VALUES (?, ?)";
		String skillQuery = "INSERT INTO requirement_skill (requirementId, skillId) VALUES (?, ?)";

		PreparedStatement pstmtRecruit = null;
		PreparedStatement pstmtRequirement = null;
		PreparedStatement pstmtSkill = null;
		ResultSet generatedKeys = null;

		try( Connection conn = DBConnection.getConnection()) {
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

			e.printStackTrace();
			return false;

		}
	}

//	public static boolean isUpdateRecruit(JSONObject jsonObject,String userId) {
//		String recruitName=jsonObject.getString("recruitName");
//		int maximumNumber=jsonObject.getInt("maximumNumber");
//		String description=jsonObject.getString("description");
//		int experience=jsonObject.getInt("experienceYear");
//		String qualification=jsonObject.getString("qualification");
//		int recruitId=jsonObject.getInt("recruitId");
//		JSONArray skills=jsonObject.getJSONArray("skills");
//		
//		String query="UPDATE recruit SET recruitName = ?, description = ?, maximumNumber = ? where recruitId = ?";
//		try {
//			PreparedStatement pstmt=conn.prepareStatement(query);
//			pstmt.setString(1, recruitName);
//			pstmt.setString(2, description);
//			pstmt.setInt(3, maximumNumber);
//			pstmt.setInt(4, recruitId);
//			int affectedRow= pstmt.executeUpdate();
//			if(affectedRow>0) {
//				
//			}
//		} catch (SQLException e) {
//			// TODO: handle exception
//		}
//		
//		return false;
//	}

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

		try( Connection conn = DBConnection.getConnection()) {

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
			return false;
		}
//	    finally {
//	        try {
//	          	  if (rs != null) rs.close();
//	            if (pstmt != null) pstmt.close();
//	            if (conn != null) conn.setAutoCommit(true);
//	            if (conn != null) conn.close();
//	        } catch (SQLException e) {
//	            e.printStackTrace();
//	        }
//	    }
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
	public static JSONArray getAllRecruits(String userId) {
		JSONArray jsonArray = new JSONArray();
		int count = 1;

		// Modify query to filter by userId
//		    String recruitQuery = "SELECT recruitId, recruitName, createdAt, maximumNumber, description FROM recruit WHERE userId = ?";
		String recruitQuery = "SELECT r.recruitId, r.recruitName, r.createdAt, r.maximumNumber, r.status, r.description, req.experience "
				+ "FROM recruit r " + "LEFT JOIN requirement req ON r.requirementId = req.requirementId "
				+ "WHERE r.userId = ? ORDER BY r.createdAt DESC";

		try (   Connection conn = DBConnection.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(recruitQuery)) {
			pstmt.setString(1, userId); // Set userId parameter
			try (ResultSet rs = pstmt.executeQuery()) {
				while (rs.next()) {
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("ID", rs.getInt("recruitId"));
					jsonObject.put("recruitId", count++);
					jsonObject.put("recruitName", rs.getString("recruitName"));
					jsonObject.put("createdAt", rs.getTimestamp("createdAt").toString()); // Convert to String
					jsonObject.put("maxHire", rs.getInt("maximumNumber"));
					jsonObject.put("description", rs.getString("description"));
					jsonObject.put("experience", rs.getInt("experience"));
					jsonObject.put("status", rs.getString("status"));
					
//					System.out.println(jsonObject);
					jsonArray.put(jsonObject);
				}
			}
		} catch (SQLException e) {
			System.err.println("Error fetching recruits: " + e.getMessage());
		}

		return jsonArray;
	}

	public static boolean closeRecruit(int recruitId) {
		String updateQuery = "UPDATE recruit SET status = 'Closed' WHERE recruitId = ?";
		boolean isUpdated = false;

		try(Connection conn = DBConnection.getConnection();) {
			PreparedStatement pstmt = conn.prepareStatement(updateQuery);

			pstmt.setInt(1, recruitId);
			int rowsAffected = pstmt.executeUpdate();

			if (rowsAffected > 0) {
				isUpdated = true;
				System.out.println("Recruit ID " + recruitId + " status updated to 'Closed'.");
			} else {
				System.out.println("Recruit ID " + recruitId + " not found.");
			}

		} catch (SQLException e) {
			System.err.println("Error updating recruit status: " + e.getMessage());
		}

		return isUpdated;
	}

	public static JSONObject deleteRecruit(JSONObject jsonObj) {
		JSONObject jsonResponse = new JSONObject();
		try (Connection conn = DBConnection.getConnection();){


//  	        JSONObject jsonObj = new JSONObject(sb.toString());
			int recruitId = jsonObj.getInt("recruitId");
			System.out.println(recruitId + " recId");

			// Check if recruit exists and get its status and requirementId
			String checkQuery = "SELECT status, requirementId FROM recruit WHERE recruitId = ?";
			try (PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {
				checkStmt.setInt(1, recruitId);
				ResultSet rs = checkStmt.executeQuery();

				if (rs.next()) {
					String status = rs.getString("status");
					int requirementId = rs.getInt("requirementId");
					System.out.println(status + " " + requirementId);

					if (!"In progress".equals(status) && !"Closed".equals(status)) {
						System.out.println("delete....");
						// **Step 2: First, delete the recruit record**
						try (PreparedStatement deleteRecruitStmt = conn
								.prepareStatement("DELETE FROM recruit WHERE recruitId = ?")) {
							deleteRecruitStmt.setInt(1, recruitId);
							int deletedRows = deleteRecruitStmt.executeUpdate();

							if (deletedRows > 0) {
								System.out.println("Row Deleted correctly");
							} else {
								System.out.println("Row Did not deleted successfully..");
							}

						}

						// **Step 3: Delete related requirement skills**
						try (PreparedStatement deleteSkillsStmt = conn
								.prepareStatement("DELETE FROM requirement_skill WHERE requirementId = ?")) {
							deleteSkillsStmt.setInt(1, requirementId);
							deleteSkillsStmt.executeUpdate();
						}

						// **Step 4: Delete requirement**
						try (PreparedStatement deleteRequirementStmt = conn
								.prepareStatement("DELETE FROM requirement WHERE requirementId = ?")) {
							deleteRequirementStmt.setInt(1, requirementId);
							int result = deleteRequirementStmt.executeUpdate();

							if (result == 1) {
								jsonResponse.put("success", true);
								jsonResponse.put("message", "Recruit and requirement deleted successfully.");
							} else {
								jsonResponse.put("success", false);
								jsonResponse.put("message", "Requirement not found or could not be deleted.");
							}
						}
					} else {
						jsonResponse.put("success", false);
						jsonResponse.put("message", "Recruit cannot be deleted as it is 'In progress'.");
					}
				} else {
					jsonResponse.put("success", false);
					jsonResponse.put("message", "Recruit not found.");
				}
			}

		} catch (Exception e) {
			jsonResponse.put("success", false);
			jsonResponse.put("message", "Error: " + e.getMessage());
			e.printStackTrace();
		}
		return jsonResponse;
	}

	public static void resumeEmailCheck(String email) {
		String resumeEmail = email;

		int resumeId = 0;
		String query = "SELECT r.resumeId FROM resume r " + "JOIN contactDetails c ON r.contactId = c.contactId "
				+ "WHERE c.email = ?";

		try (   Connection conn = DBConnection.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(query)) {
			pstmt.setString(1, resumeEmail);

			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					resumeId = rs.getInt("resumeId");
				}
			}
		} catch (SQLException e) {
			System.err.println("Error fetching resumeId: " + e.getMessage());
		}
	}

}
