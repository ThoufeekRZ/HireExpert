package DTO;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class User {
	
	String userId;
	String name;
	String email;
	
	@JsonIgnore
	String password;
	
	public User(String name, String email, String password) {
		
//		this.firstName = firstName;
//		this.lastName = lastName;
		this.name=name;
		this.email = email;
		this.password = password;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	@Override
	public String toString() {
		return "User [userId=" + userId + ", name=" + name + ", email=" + email
				+ ", password=" + password + "]";
	}
	
}
