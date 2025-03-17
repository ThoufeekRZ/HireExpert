package models;

public class UserExtraInfo {
	User user;
	int id;
	String phoneNumber;
	String role;
	String bio;
	public UserExtraInfo(User user, int id, String phoneNumber, String role, String bio) {
//		super();
		this.user = user;
		this.id = id;
		this.phoneNumber = phoneNumber;
		this.role = role;
		this.bio = bio;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public String getBio() {
		return bio;
	}
	public void setBio(String bio) {
		this.bio = bio;
	}
	@Override
	public String toString() {
		return "UserExtraInfo [user=" + user + ", id=" + id + ", phoneNumber=" + phoneNumber + ", role=" + role
				+ ", bio=" + bio + "]";
	}
	
}
