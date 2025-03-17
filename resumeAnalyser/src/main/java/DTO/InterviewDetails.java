package DTO;

import java.sql.Date;
import java.sql.Time;

public class InterviewDetails {
    private int id;
    private String position;
    private java.sql.Date interviewDate;   // Using java.sql.Date for date fields
    private java.sql.Time interviewTime;   // Using java.sql.Time for time fields
    private String interviewType;
    private String interviewLocationLink;
    private String interviewerName;
    private String interviewerTitle;
    private String additionalInformation;
    private String status;  // Enum: 'Approved', 'Pending', 'Reject'

    // Constructors
    public InterviewDetails() {}

    public InterviewDetails(int id, String position, java.sql.Date interviewDate, java.sql.Time interviewTime,
                            String interviewType, String interviewLocationLink, String interviewerName,
                            String interviewerTitle, String additionalInformation, String status) {
        this.id = id;
        this.position = position;
        this.interviewDate = interviewDate;
        this.interviewTime = interviewTime;
        this.interviewType = interviewType;
        this.interviewLocationLink = interviewLocationLink;
        this.interviewerName = interviewerName;
        this.interviewerTitle = interviewerTitle;
        this.additionalInformation = additionalInformation;
        this.status = status;
    }

    public InterviewDetails(int id, String position, Date interviewDate, Time interviewTime, String interviewType, String interviewLocationLink, String interviewerName, String interviewerTitle, String additionalInformation, String companyName, String hrName, String hrTitle, String phoneNumber, String emailAddress, String status) {
        this.id = id;
        this.position = position;
        this.interviewDate = interviewDate;
        this.interviewTime = interviewTime;
        this.interviewType = interviewType;
        this.interviewLocationLink = interviewLocationLink;
        this.interviewerName = interviewerName;
        this.interviewerTitle = interviewerTitle;
        this.additionalInformation = additionalInformation;
        this.status = companyName;

    }

    public InterviewDetails(int id, String position, Date interviewDate, Time interviewTime, String interviewType, String interviewLocationLink, String interviewerName, String interviewerTitle, String additionalInformation) {
        this.id = id;
        this.position = position;
        this.interviewDate = interviewDate;
        this.interviewTime = interviewTime;
        this.interviewType = interviewType;
        this.interviewLocationLink = interviewLocationLink;
        this.interviewerName = interviewerName;
        this.interviewerTitle = interviewerTitle;
        this.additionalInformation = additionalInformation;

    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public java.sql.Date getInterviewDate() {
        return interviewDate;
    }

    public void setInterviewDate(java.sql.Date interviewDate) {
        this.interviewDate = interviewDate;
    }

    public java.sql.Time getInterviewTime() {
        return interviewTime;
    }

    public void setInterviewTime(java.sql.Time interviewTime) {
        this.interviewTime = interviewTime;
    }

    public String getInterviewType() {
        return interviewType;
    }

    public void setInterviewType(String interviewType) {
        this.interviewType = interviewType;
    }

    public String getInterviewLocationLink() {
        return interviewLocationLink;
    }

    public void setInterviewLocationLink(String interviewLocationLink) {
        this.interviewLocationLink = interviewLocationLink;
    }

    public String getInterviewerName() {
        return interviewerName;
    }

    public void setInterviewerName(String interviewerName) {
        this.interviewerName = interviewerName;
    }

    public String getInterviewerTitle() {
        return interviewerTitle;
    }

    public void setInterviewerTitle(String interviewerTitle) {
        this.interviewerTitle = interviewerTitle;
    }

    public String getAdditionalInformation() {
        return additionalInformation;
    }

    public void setAdditionalInformation(String additionalInformation) {
        this.additionalInformation = additionalInformation;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "InterviewDetails{" +
                "id=" + id +
                ", position='" + position + '\'' +
                ", interviewDate=" + interviewDate +
                ", interviewTime=" + interviewTime +
                ", interviewType='" + interviewType + '\'' +
                ", interviewLocationLink='" + interviewLocationLink + '\'' +
                ", interviewerName='" + interviewerName + '\'' +
                ", interviewerTitle='" + interviewerTitle + '\'' +
                ", additionalInformation='" + additionalInformation + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
