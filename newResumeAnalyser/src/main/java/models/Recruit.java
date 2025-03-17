package models;

import java.time.LocalDateTime;

public class Recruit {
	int recruitId;
	String recruitName;
	Requirement requirement;
	int maximumNumber;
	LocalDateTime createdAt;
	String description;
	String status;
	int accepted;
	int reject;
	int mailSented;
	int total_Resume;

	public Recruit(String recruitName, int maximumNumber) {
//		super();
		this.recruitName = recruitName;
		this.maximumNumber = maximumNumber;
	}
	
	
	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}


	public int getAccepted() {
		return accepted;
	}


	public void setAccepted(int accepted) {
		this.accepted = accepted;
	}


	public int getReject() {
		return reject;
	}


	public void setReject(int reject) {
		this.reject = reject;
	}


	public int getMailSented() {
		return mailSented;
	}


	public void setMailSented(int mailSented) {
		this.mailSented = mailSented;
	}


	public int getTotal_Resume() {
		return total_Resume;
	}


	public void setTotal_Resume(int total_Resume) {
		this.total_Resume = total_Resume;
	}


	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public LocalDateTime getCreatedAt() {
		return createdAt;
	}



	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}



	public int getRecruitId() {
		return recruitId;
	}
	public void setRecruitId(int recruitId) {
		this.recruitId = recruitId;
	}
	public String getRecruitName() {
		return recruitName;
	}
	public void setRecruitName(String recruitName) {
		this.recruitName = recruitName;
	}
	public Requirement getRequirement() {
		return requirement;
	}
	public void setRequirement(Requirement requirement) {
		this.requirement = requirement;
	}
	public int getMaximumNumber() {
		return maximumNumber;
	}
	public void setMaximumNumber(int maximumNumber) {
		this.maximumNumber = maximumNumber;
	}


	@Override
	public String toString() {
		return "Recruit [recruitId=" + recruitId + ", recruitName=" + recruitName + ", requirement=" + requirement
				+ ", maximumNumber=" + maximumNumber + ", createdAt=" + createdAt + ", description=" + description
				+ ", status=" + status + ", accepted=" + accepted + ", reject=" + reject + ", mailSented=" + mailSented
				+ ", total_Resume=" + total_Resume + "]";
	}





	

	
	
}
