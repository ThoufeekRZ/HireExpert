package models;

public class Contact {
	int contactId;
	String contactNumber;
	String email;
	String address;
	String linkedInId;
	public Contact(String contactNumber, String email, String linkedInId) {
//		super();
		this.contactNumber = contactNumber;
		this.email = email;
		this.linkedInId = linkedInId;
	}
	public String getContactNumber() {
		return contactNumber;
	}
	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getLinkedInId() {
		return linkedInId;
	}
	public void setLinkedInId(String linkedInId) {
		this.linkedInId = linkedInId;
	}
	public int getContactId() {
		return contactId;
	}
	public void setContactId(int contactId) {
		this.contactId = contactId;
	}
	@Override
	public String toString() {
		return "Contact [contactNumber=" + contactNumber + ", email=" + email + ", address=" + address + ", linkedInId="
				+ linkedInId + "]";
	}
	
}
