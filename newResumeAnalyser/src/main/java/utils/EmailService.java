package utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import dao.DBConnection;
import dao.RecruitDAO;
import models.Recruit;

public class EmailService {


	public static List<String> getSelectedFinalizedEmails(String status, int recruitId) {
		// Fetch Recruit Object
		Recruit recruit = RecruitDAO.getRecruitById(recruitId);

		List<String> emails = new ArrayList<>();
		List<Integer> resumeIds = new ArrayList<>();
		String query = null;

		// Build Query Based on Status
		if ("All candidates".equals(status)) {
			query = "SELECT r.resumeId, c.email FROM resume r "
					+ "JOIN contactDetails c ON r.contactId = c.contactId "
					+ "JOIN recr_resu_relation rr ON r.resumeId = rr.resumeId "
					+ "WHERE rr.recruitId = ? AND rr.status IN ('New', 'Reviewed', 'Shortlisted') "
					+ "ORDER BY rr.resumeScore DESC";
		} else if ("Shortlisted candidates".equals(status)) {
			query = "SELECT r.resumeId, c.email FROM resume r "
					+ "JOIN contactDetails c ON r.contactId = c.contactId "
					+ "JOIN recr_resu_relation rr ON r.resumeId = rr.resumeId "
					+ "WHERE rr.recruitId = ? AND rr.status = 'Shortlisted' "
					+ "ORDER BY rr.resumeScore DESC";
		}


		if (query == null) {
			return emails; // Return empty list if no valid status
		}

		// Execute Query and Collect Data
		try (Connection conn = DBConnection.getConnection();
			 PreparedStatement pstmt = conn.prepareStatement(query)) {

			pstmt.setInt(1, recruitId);
			try (ResultSet rs = pstmt.executeQuery()) {
				while (rs.next()) {
					// Collect Data
					int resumeId = rs.getInt("resumeId");
					String email = rs.getString("email");

					emails.add(email);
					resumeIds.add(resumeId);

					// Limit Emails if Exceeding Maximum Number
					if (emails.size() >= recruit.getMaximumNumber()) {
						emails = emails.subList(0, recruit.getMaximumNumber());
						resumeIds = resumeIds.subList(0, recruit.getMaximumNumber());
						break; // Stop if limit reached
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// ✅ Update Email Status AFTER Closing ResultSet
		for (int resumeId : resumeIds) {
			updateEmailSentStatus(recruitId, resumeId);
		}

		return emails;
	}


	private static void updateEmailSentStatus(int recruitId, int resumeId) {
		String updateQuery = "UPDATE recr_resu_relation SET isEmailSentStatus = 'Interview' " +
				"WHERE recruitId = ? AND resumeId = ?";

		try (   Connection conn = DBConnection.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(updateQuery)) {
			pstmt.setInt(1, recruitId);
			pstmt.setInt(2, resumeId);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}


	public static boolean sendEmails(String status, int recruitId, String subject, String emailBody) {

		List<String> recipients = getSelectedFinalizedEmails(status,recruitId);
		System.out.println(recipients);

		int emailCount = recipients.size();

		String query = "UPDATE recruit SET mailSented = ? WHERE recruitId = ?";

		try(Connection conn = DBConnection.getConnection();) {
			PreparedStatement pstmt = conn.prepareStatement(query);

			pstmt.setInt(1, emailCount);
			pstmt.setInt(2, recruitId);

			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
//		List<String> recipients=new ArrayList<String>();
//		recipients.add("jerlinamsleena11@gmail.com");
//		recList.add("ramizthoufeek@gmail.com");

//		System.out.println(recipients.toString());
		final String senderEmail = "hirexpert11@gmail.com";
		final String senderPassword = "jbjh cljy gedj unym";
//		final String senderPassword="aydd pjex kqmz mudi";

		// Email properties
		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "465");

		// Create session
		Session session = Session.getInstance(props, new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(senderEmail, senderPassword);
			}
		});

		try {
			System.out.println("fvwdgiuwbih");
			for (String recipient : recipients) {
//			String recipient="jerlinasmleena11@gmail.com";
				Message message = new MimeMessage(session);
				message.setFrom(new InternetAddress(senderEmail));
				System.out.println("gvvjh");
				message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
//				message.setSubject("Congratulations! You have been selected");
//				message.setText(
//						"This is HireXpert!!!\nDear Candidate,\n\nWe are pleased to inform you that you have been selected for the next step in our hiring process.\n\nBest regards,\nHR Team");

				message.setSubject(subject);
				message.setContent(emailBody, "text/html; charset=UTF-8");
				System.out.println("gyuhjgfg");
				Transport.send(message);
				System.out.println("Email sent to: " + recipient);

			}
			return true;
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static boolean sendOneEmail(String recipient, String subject, String emailBody) {
		final String senderEmail = "hirexpert11@gmail.com";
		final String senderPassword = "jbjh cljy gedj unym";

		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "465");

		Session session = Session.getInstance(props, new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(senderEmail, senderPassword);
			}
		});

		try {
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(senderEmail));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
			message.setSubject(subject);
			message.setContent(emailBody, "text/html; charset=UTF-8");

			Transport.send(message);
			System.out.println("Email sent to: " + recipient);

			return true;
		} catch (MessagingException e) {
			e.printStackTrace();
			return false;
		}
	}
}

//emails.add("jerlinamsleena11@gmail.com");
//emails.add("yosebath@gmail.com");
//emails.add("ananthaselvijeyakumar@gmail.com");
//emails.add("memak36727@lxheir.com");
//emails.add("ledey87651@noomlocs.com");


