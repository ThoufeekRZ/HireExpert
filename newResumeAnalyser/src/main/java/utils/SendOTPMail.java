package utils;

import java.util.Properties;
import java.util.Random;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.json.JSONObject;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class SendOTPMail {
	public static JSONObject isSendOTP(String email,HttpSession mySession) {
		int otpvalue = 0;
		JSONObject jsonObj=new JSONObject();
		
		
		if(email!=null || !email.equals("")) {

			Random rand = new Random();
			otpvalue = rand.nextInt(1255650);
			jsonObj.put("otp", otpvalue);
			
			String to = email; 

			Properties props = new Properties();
			props.put("mail.smtp.host", "smtp.gmail.com");
			props.put("mail.smtp.socketFactory.port", "465");
			props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.port", "465");
			Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
//					return new PasswordAuthentication("jerlinamsleena11@gmail.com", "lmdk mlby mkdj leab");
					return new PasswordAuthentication("hirexpert11@gmail.com", "jbjh cljy gedj unym");																			
																									
				}
			});
			
			try {
				MimeMessage message = new MimeMessage(session);
				message.setFrom(new InternetAddress(email));
				message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
				message.setSubject("This is HireXpert!!!");
				message.setText("Your OTP is: " + otpvalue);

				Transport.send(message);
				System.out.println("message sent successfully");
				
				System.out.println("set "+otpvalue);
				mySession.setAttribute("otp",otpvalue); 
				System.out.println(mySession.getAttribute("otp")); 
				jsonObj.put("isSend", true);
				return jsonObj;
			}

			catch (MessagingException e) {
				
				throw new RuntimeException(e);
				
			}
		}
		jsonObj.put("isSend", false);
		return jsonObj;

	}
}
