package DTO;

import java.util.List;

public class Requirement {
	int requirementId;
	int experienceYear;
	String qualification;
	List<Skill> skills;
	public Requirement(int experienceYear, String qualification, List<Skill> skills) {
		super();
		this.experienceYear = experienceYear;
		this.qualification = qualification;
		this.skills = skills;
	}
	
	public int getRequirementId() {
		return requirementId;
	}

	public void setRequirementId(int requirementId) {
		this.requirementId = requirementId;
	}

	public int getExperienceYear() {
		return experienceYear;
	}

	public void setExperienceYear(int experienceYear) {
		this.experienceYear = experienceYear;
	}

	public String getQualification() {
		return qualification;
	}

	public void setQualification(String qualification) {
		this.qualification = qualification;
	}

	public List<Skill> getSkills() {
		return skills;
	}

	public void setSkills(List<Skill> skills) {
		this.skills = skills;
	}

	@Override
	public String toString() {
		return "Requirement [requirementId=" + requirementId + ", experienceYear=" + experienceYear + ", qualification="
				+ qualification + ", skills=" + skills + "]";
	}
	
}
