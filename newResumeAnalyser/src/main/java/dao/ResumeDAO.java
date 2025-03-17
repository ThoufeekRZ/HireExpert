package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import models.Contact;
import models.Experience;
import models.Resume;
import models.Skill;

import java.util.logging.Logger;
import java.util.logging.Level;

public class ResumeDAO {

	private static final Logger logger = Logger.getLogger(ResumeDAO.class.getName());


	public static boolean insertResume(String jsonString) {
		try(Connection conn = DBConnection.getConnection();) {
			JSONObject jsonObject = new JSONObject(jsonString);
			System.out.println(jsonObject.toString());
			String candidateName = jsonObject.getString("Name");
			JSONObject contactDetails = jsonObject.getJSONObject("Contact Details");
			String email = contactDetails.getString("Email");
			String mobileNumber = contactDetails.optString("Contact Number", null);
			String address = contactDetails.optString("Address", null);
			String linkedInId = contactDetails.optString("LinkedIn", null);


			JSONArray skillsArray = jsonObject.getJSONArray("Skills");

			
			
			JSONArray experienceArray=jsonObject.optJSONArray("Experience"); //gvyhbgjh
			
//			JSONObject experienceDetails = jsonObject.optJSONObject("Experience");
//			
//			String company = experienceDetails.optString("CompanyName", null) ;
//			String location = experienceDetails != null ? experienceDetails.optString("Location", null) : null;
//			String title = experienceDetails != null ? experienceDetails.optString("Title", null) : null;
//			int years = experienceDetails != null ? experienceDetails.optInt("Years of Experience", 0) : 0;
//			JSONArray responsibilities = experienceDetails != null ? experienceDetails.optJSONArray("Responsibilities") : null;
//
			String education = jsonObject.optString("Education", null);
			String description = jsonObject.optString("Description", null);
//
			String contactSQL = "INSERT INTO contactDetails (email, mobileNumber, address, linkedInId) VALUES (?, ?, ?, ?)";
			PreparedStatement contactStmt = conn.prepareStatement(contactSQL, Statement.RETURN_GENERATED_KEYS);
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
//
//			String experienceSQL = "INSERT INTO experienceDetails (company, location, title, year, responsibilities) VALUES (?, ?, ?, ?, ?)";
//			PreparedStatement experienceStmt = conn.prepareStatement(experienceSQL, Statement.RETURN_GENERATED_KEYS);
//			experienceStmt.setString(1, company);
//			experienceStmt.setString(2, location);
//			experienceStmt.setString(3, title);
//			experienceStmt.setInt(4, years);
//			String responsibilitiesJson = (responsibilities != null) ? responsibilities.toString() : null;
//			experienceStmt.setString(5, responsibilitiesJson);
//			experienceStmt.executeUpdate();
//			ResultSet experienceRs = experienceStmt.getGeneratedKeys();
//			int experienceId = 0;
//			if (experienceRs.next()) {
//				experienceId = experienceRs.getInt(1);
//			}
//
//			String resumeSQL = "INSERT INTO resume (candidateName, contactId, education, description) VALUES (?, ?, ?, ?)";
//			PreparedStatement resumeStmt = conn.prepareStatement(resumeSQL, Statement.RETURN_GENERATED_KEYS);
//			resumeStmt.setString(1, candidateName);
//			resumeStmt.setInt(2, contactId);
//			resumeStmt.setString(3, education);
//			resumeStmt.setString(4, description);
//			resumeStmt.executeUpdate();
//			ResultSet resumeRs = resumeStmt.getGeneratedKeys();
//			int resumeId = 0;
//			if (resumeRs.next()) {
//				resumeId = resumeRs.getInt(1);
//			}
//			
//			//vguygig
			//			String linkSQL = "INSERT INTO resume_experience (resumeId, experienceId) VALUES (?, ?)";
//			PreparedStatement linkStmt = conn.prepareStatement(linkSQL);
//			linkStmt.setInt(1, resumeId);
//			linkStmt.setInt(2, experienceId);
//			linkStmt.executeUpdate();
//			conn.commit();
//			linkStmt.close();  
//			JSONArray experienceArray = jsonObject.optJSONArray("Experience");
			List<Integer> experienceIds = new ArrayList<>(); // To store multiple experience IDs

			if (experienceArray != null) {
			    String experienceSQL = "INSERT INTO experienceDetails (company, location, title, year, responsibilities) VALUES (?, ?, ?, ?, ?)";
			    PreparedStatement experienceStmt = conn.prepareStatement(experienceSQL, Statement.RETURN_GENERATED_KEYS);

			    for (int i = 0; i < experienceArray.length(); i++) {
			        JSONObject experienceDetails = experienceArray.optJSONObject(i);

			        String company = experienceDetails.optString("CompanyName", null);
			        String location = experienceDetails.optString("Location", null);
			        String title = experienceDetails.optString("Title", null);
			        int years = experienceDetails.optInt("Years of Experience", 0);
			        JSONArray responsibilities = experienceDetails.optJSONArray("Responsibilities");

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
			    experienceStmt.close();
			}

			// Insert into resume table
			String resumeSQL = "INSERT INTO resume (candidateName, contactId, education, description) VALUES (?, ?, ?, ?)";
			PreparedStatement resumeStmt = conn.prepareStatement(resumeSQL, Statement.RETURN_GENERATED_KEYS);
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
			resumeStmt.close();

			// Link resume to multiple experiences
			String linkSQL = "INSERT INTO resume_experience (resumeId, experienceId) VALUES (?, ?)";
			PreparedStatement linkStmt = conn.prepareStatement(linkSQL);
			for (int experienceId : experienceIds) {
			    linkStmt.setInt(1, resumeId);
			    linkStmt.setInt(2, experienceId);
			    linkStmt.addBatch();
			}
			linkStmt.executeBatch();
			linkStmt.close();

			conn.commit(); // Commit all changes
			System.out.println("Resume inserted successfully with multiple experiences.");
			
			String insertResumeSkillQuery = "INSERT INTO resume_skill (resumeId, skillId) VALUES (?, ?)";
			String skillQuery = "SELECT skillId FROM skills WHERE skillName = ?";
			String insertSkillQuery = "INSERT INTO skills (skillName) VALUES (?)";
//
			try {
			    conn.setAutoCommit(false);  

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
			    conn.commit(); 

			} catch (SQLException e) {
			    System.out.println(e.getMessage());
			    if (conn != null) {
			        try {
			            conn.rollback(); 
			        } catch (SQLException ex) {
			            ex.printStackTrace();
			        }
			    }
			}

			System.out.println("Resume inserted successfully.");
			return true;
		} catch (SQLException e) {
			
			    System.out.println(e.getMessage());


			System.out.println("Error inserting resume: " + e.getMessage());
		}	 catch (Exception e) {
			System.out.println("Error parsing JSON: " + e.getMessage());
		}
		return false;
	}

	public static Resume getResumeById(int resumeId) {
	    Resume resume = null;
	    Contact contact = null;
	    List<Experience> experiences = new ArrayList<>();
	    int resumeID = 0;
	    String candidateName = null;
	    String education = null;
	    String description = null;
	    double resumeScore=0.0;
	    String email = null;
	    String mobileNumber = null;
	    String address = null;
	    String linkedInId = null;

	    String company = null;
	    String location = null;
	    String title = null;
	    List<String> responsibilities = new ArrayList<>();
	    int experienceYear = 0;
	    
	    List<Skill> skillsArray = getSkillsByResumeId(resumeId);

	    try (Connection conn = DBConnection.getConnection();){
	        String resumeSQL = "SELECT * FROM resume WHERE resumeId = ?";
	        PreparedStatement resumeStmt = conn.prepareStatement(resumeSQL);
	        resumeStmt.setInt(1, resumeId);
	        ResultSet resumeRs = resumeStmt.executeQuery();

	        if (resumeRs.next()) {
	            resumeID = resumeRs.getInt("resumeId");
	            candidateName = resumeRs.getString("candidateName");
	            education = resumeRs.getString("education");
	            description = resumeRs.getString("description");
	            resumeScore=resumeRs.getDouble("resumeScore");
	            
	            int contactId = resumeRs.getInt("contactId");

	            String contactSQL = "SELECT * FROM contactDetails WHERE contactId = ?";
	            PreparedStatement contactStmt = conn.prepareStatement(contactSQL);
	            contactStmt.setInt(1, contactId);
	            ResultSet contactRs = contactStmt.executeQuery();
	            
	            if (contactRs.next()) {
	                email = contactRs.getString("email");
	                mobileNumber = contactRs.getString("mobileNumber");
	                address = contactRs.getString("address");
	                linkedInId = contactRs.getString("linkedInId");

	                contact = new Contact(mobileNumber, email, linkedInId);
	                contact.setContactId(contactId);
	                contact.setAddress(address);
	            }

	            String experienceSQL = "SELECT * FROM experienceDetails WHERE experienceId IN (SELECT experienceId FROM resume_experience WHERE resumeId = ?)";
	            PreparedStatement experienceStmt = conn.prepareStatement(experienceSQL);
	            experienceStmt.setInt(1, resumeId);
	            ResultSet experienceRs = experienceStmt.executeQuery();

	            while (experienceRs.next()) {
	                company = experienceRs.getString("company");
	                location = experienceRs.getString("location");
	                title = experienceRs.getString("title");
	                experienceYear = experienceRs.getInt("year");
	                responsibilities = getResponsibilities(new JSONArray(experienceRs.getString("responsibilities")));

	                Experience experience = new Experience(company, location, experienceYear);
	                experience.setTitle(title);
	                experience.setResponsibilities(responsibilities);
	                experiences.add(experience);
	            }

	            contactStmt.close();
	            experienceStmt.close();
	            resumeStmt.close();
	        }

	    } catch (SQLException e) {
	        System.out.println("Error fetching resume: " + e.getMessage());
	    }

	    resume = new Resume(candidateName, contact, education, description);
	    resume.setResumeId(resumeID);
	    resume.setSkills(skillsArray);
	    resume.setExperiences(experiences); 
	    resume.setResumeScore(resumeScore);

	    return resume;
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

	    try (Connection conn = DBConnection.getConnection();){
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
//	            System.out.println(skill);
	            skillsList.add(skill);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace(); // Log the error
	    } 
	   
	    return skillsList;
	}
	
	public static void toggleIsSelected(int resumeId) {

		try(Connection conn = DBConnection.getConnection();) {
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

		try(Connection conn = DBConnection.getConnection();) {
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
	
	public static void updateResumeScore(int resumeId, double score) throws SQLException {
		String query = "UPDATE resume SET resumeScore = ? WHERE resumeId = ?";



		try(    Connection conn = DBConnection.getConnection();
				PreparedStatement stm = conn.prepareStatement(query)) {

			stm.setDouble(1, score);
			stm.setInt(2, resumeId);
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
	


}
