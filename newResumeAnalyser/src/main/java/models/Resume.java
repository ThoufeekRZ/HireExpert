package models;

import java.util.ArrayList;
import java.util.List;

public class Resume {
	
	int resumeId;
	String candidateName;
	Contact contact;
	Experience experience;
	List<Experience> experiences;
	
	String education;
	String description;
	List<Skill> skills;
	
	double resumeScore;
	
	
	public Resume(String candidateName, Contact contact, String education, String description) {
		super();
		this.candidateName = candidateName;
		this.contact = contact;

		this.education = education;
		this.description = description;
		this.skills=new ArrayList<Skill>();
		this.experiences=new ArrayList<Experience>();
		this.resumeScore=0.0;
	}
	
	
	
	public double getResumeScore() {
		return resumeScore;
	}



	public void setResumeScore(double resumeScore) {
		this.resumeScore = resumeScore;
	}



	public List<Experience> getExperiences() {
		return experiences;
	}


	public void setExperiences(List<Experience> experiences) {
		this.experiences = experiences;
	}


	public List<Skill> getSkills() {
		return skills;
	}

	public void setSkills(List<Skill> skills) {
		this.skills = skills;
	}

	public String getCandidateName() {
		return candidateName;
	}
	public void setCandidateName(String candidateName) {
		this.candidateName = candidateName;
	}
	public String getEducation() {
		return education;
	}
	public void setEducation(String education) {
		this.education = education;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getResumeId() {
		return resumeId;
	}
	public void setResumeId(int resumeId) {
		this.resumeId = resumeId;
	}
	public Contact getContact() {
		return contact;
	}
	public void setContact(Contact contact) {
		this.contact = contact;
	}
	public Experience getExperience() {
		return experience;
	}
	public void setExperience(Experience experience) {
		this.experience = experience;
	}



	@Override
	public String toString() {
		return "Resume [resumeId=" + resumeId + ", candidateName=" + candidateName + ", contact=" + contact
				+ ", experience=" + experience + ", experiences=" + experiences + ", education=" + education
				+ ", description=" + description + ", skills=" + skills + ", resumeScore=" + resumeScore + "]";
	}
	
	

	
	
}
