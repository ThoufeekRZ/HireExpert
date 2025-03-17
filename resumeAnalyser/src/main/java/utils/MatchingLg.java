package utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import DAO.DBConnection;
import DAO.RecruitDAO;
import DAO.ResumeDAO;
import DTO.Recruit;
import DTO.Requirement;
import DTO.Skill;

public class MatchingLg {
	public static Connection conn;

    static {
        try {
            conn = DBConnection.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean isMatched(int recruitId) throws SQLException {

		Recruit recruit = RecruitDAO.getRecruitById(recruitId);
		Requirement requirement = recruit.getRequirement();
		int maximumNumber = recruit.getMaximumNumber();

		int experienceYear = requirement.getExperienceYear();
		String qualification = requirement.getQualification();
		List<Skill> requirementSkills = requirement.getSkills();

		List<Integer> resumeIdArrayExperience = new ArrayList<Integer>();

		String query = "SELECT DISTINCT re.resumeId FROM resume_experience re "
				+ "JOIN experienceDetails ed ON re.experienceId = ed.experienceId " + "WHERE ed.year >= ?";
		List<Integer> filteredResumeIds = new ArrayList<>();
		String query1 = "SELECT resumeId FROM resume WHERE resumeId IN ("
				+ resumeIdArrayExperience.stream().map(id -> "?").collect(Collectors.joining(", "))
				+ ") AND education = ?";

		try {
			PreparedStatement pstmt = conn.prepareStatement(query);

			pstmt.setInt(1, experienceYear);
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				resumeIdArrayExperience.add(rs.getInt("resumeId"));
			}

			PreparedStatement pstmt1 = conn.prepareStatement(query1);

			int index = 1;
			for (int id : resumeIdArrayExperience) {
				pstmt1.setInt(index++, id);
			}
			
			pstmt1.setString(index, qualification);

			ResultSet rs1 = pstmt1.executeQuery();
			while (rs1.next()) {
				filteredResumeIds.add(rs1.getInt("resumeId"));
			}

			List<Integer> resumeIds = new ArrayList<>(); // final

//			if (requirementSkills == null || requirementSkills.isEmpty()) {
////		            return resumeIds;
//			}

			List<String> skillNames = requirementSkills.stream().map(Skill::getSkillName).collect(Collectors.toList());

			String query2 = "SELECT DISTINCT rs.resumeId " + "FROM resume_skill rs "
					+ "JOIN skills s ON rs.skillId = s.skillId " + "WHERE s.skillName IN ("
					+ skillNames.stream().map(name -> "?").collect(Collectors.joining(", ")) + ")";

			PreparedStatement pstmt2 = conn.prepareStatement(query2);

			int index1 = 1;
			for (String skillName : skillNames) {
				pstmt2.setString(index1++, skillName);
			}

			ResultSet rs2 = pstmt2.executeQuery();
			while (rs2.next()) {
				resumeIds.add(rs2.getInt("resumeId"));
			}
			
//			String query3="UPDATE INTO resume S"
			for(Integer iD : resumeIds) {
				ResumeDAO.toggleIsSelected(iD);
				System.out.println("update isSelected");
			}
			
			return true;

		} catch (SQLException e) {
			e.printStackTrace();
		}


		return false;
	}
}
