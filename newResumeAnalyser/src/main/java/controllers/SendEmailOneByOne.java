package controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.json.JSONObject;

import jakarta.servlet.ServletException;
import utils.EmailService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class SendEmailOneByOne
 */
@WebServlet("/SendEmailOneByOne")
public class SendEmailOneByOne extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * Default constructor.
     */
    public SendEmailOneByOne() {
        super();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.getWriter().append("Served at: ").append(request.getContextPath());
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        JSONObject jsonResponse = new JSONObject();

        try (BufferedReader reader = request.getReader()) {
            StringBuilder requestBody = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                requestBody.append(line);
            }

            JSONObject jsonObject = new JSONObject(requestBody.toString());
            String email = jsonObject.getString("email");
            String subject = jsonObject.getString("subject");
            String emailBody = jsonObject.getString("emailBody");

            boolean isEmailSent = EmailService.sendOneEmail(email, subject, emailBody);
            jsonResponse.put("success", isEmailSent);
        } catch (Exception e) {
            e.printStackTrace();
            jsonResponse.put("success", false);
        }

        response.getWriter().write(jsonResponse.toString());
    }
}


