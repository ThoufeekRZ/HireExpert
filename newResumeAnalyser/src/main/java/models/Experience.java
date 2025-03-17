package models;

import java.util.List;

public class Experience {
	String company;
	String location;
	String title;
	List<String> responsibilities;
	int experienceYear;
	
	public Experience(String company, String location, int experienceYear) {
		super();
		this.company = company;
		this.location = location;
		this.experienceYear = experienceYear;
	}
	
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public List<String> getResponsibilities() {
		return responsibilities;
	}
	public void setResponsibilities(List<String> responsibilities) {
		this.responsibilities = responsibilities;
	}
	public int getExperienceYear() {
		return experienceYear;
	}
	public void setExperienceYear(int experienceYear) {
		this.experienceYear = experienceYear;
	}
	
	@Override
	public String toString() {
		return "Experience [company=" + company + ", location=" + location + ", title=" + title + ", responsibilities="
				+ responsibilities + ", experienceYear=" + experienceYear + "]";
	}
	
	
}
