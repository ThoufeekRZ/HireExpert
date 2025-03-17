package validation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import dao.DBConnection;
import dao.UserDAO;

public class InsertSessionId {

	public static boolean insertSession(String email,String password,String sessionId) {
		password=UserDAO.encodePassword(password);
		System.out.println(email+ password+sessionId);
		System.out.println("insert session");
		String userId = null;
	    String userQuery = "SELECT userId FROM userDetails WHERE email = ? AND password = ?";
	    String sessionQuery = "INSERT INTO session (sessionId, userId, createdAt) VALUES (?, ?, UNIX_TIMESTAMP(NOW(3)) * 1000)";

	    try (   Connection conn=DBConnection.getConnection();
				PreparedStatement pstmtUser = conn.prepareStatement(userQuery)) {
	        pstmtUser.setString(1, email);
	        pstmtUser.setString(2, password);
	        try (ResultSet rs = pstmtUser.executeQuery()) {
	            if (rs.next()) {
	                userId = rs.getString("userId");
	            }
	        }
	        
	        if (userId == null) {
	            System.out.println("Invalid email or password");
	            return false;
	        }

	        try (PreparedStatement pstmtSession = conn.prepareStatement(sessionQuery)) {
	            pstmtSession.setString(1, sessionId);
	            pstmtSession.setString(2, userId);
	            int rowsInserted = pstmtSession.executeUpdate();
	            return rowsInserted > 0;
	        }
	    } catch (SQLException e) {
	        System.err.println("Database Error: " + e.getMessage());
	    }
	    return false;
	}
}
