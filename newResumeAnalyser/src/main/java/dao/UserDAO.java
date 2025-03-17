package dao;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;
import java.util.UUID;

import org.json.JSONObject;

//import com.mysql.cj.jdbc.Driver;

import models.User;
import view.CookieUtil;

public class UserDAO {
	
//	public User user;
	

	
	public static boolean insertUser(String username,String email,String password) {

		password = encodePassword(password);
   
        String userId = UUID.randomUUID().toString().replace("-", "").substring(0, 16);

        String query = "INSERT INTO userDetails (userId, userName, email, password) VALUES (?, ?, ?, ?)";

        try (   Connection conn = DBConnection.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, userId);
            pstmt.setString(2, username);
            pstmt.setString(3, email);
            pstmt.setString(4, password);

            int rowsInserted = pstmt.executeUpdate();
            return rowsInserted > 0; 
        } catch (SQLException e) {
            System.err.println("Error inserting user: " + e.getMessage());
        }
        return false;
	}
	
	public static User getUser(String email,String password) {
		
		String hashedPassword = encodePassword(password); 

        String query = "SELECT userId, userName, email, password FROM userDetails WHERE email = ? AND password = ?";
        
        try (   Connection conn = DBConnection.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, email);
            pstmt.setString(2, hashedPassword);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                  
                    User user = new User(rs.getString("userName"), rs.getString("email"), rs.getString("password"));
                    user.setUserId(rs.getString("userId"));
                    return user; 
                }
            }
        } catch (SQLException e) {
            System.err.println("Database error in getUser: " + e.getMessage());
        }
        return null; 
	}
	
	public static boolean insertUserExtraInfo(JSONObject jsonObject, String userId) {

//	    String userName = jsonObject.getString("userName");
//	    String email = jsonObject.getString("email");
//	    String phoneNumber = jsonObject.getString("phoneNumber");
//	    String role = jsonObject.getString("role");
//	    String bio = jsonObject.getString("bio");
//	    String profileImage = jsonObject.getString("profileImage");
		String userName = jsonObject.optString("userName", "Default User");
		String email = jsonObject.optString("email", "default@example.com");
		String phoneNumber = jsonObject.optString("phoneNumber", "0000000000");
		String role = jsonObject.optString("role", "User");
		String bio = jsonObject.optString("bio", "No bio available");
		String profileImage = jsonObject.optString("profileImage", "default-profile.png");


	    String updateUserDetailsQuery = "UPDATE userDetails SET userName = ?, email = ? WHERE userId = ?";
	    String checkUserExtraInfoQuery = "SELECT COUNT(*) FROM userExtraInfo WHERE userId = ?";
	    String insertUserExtraInfoQuery = "INSERT INTO userExtraInfo (userId, phoneNumber, role, bio, profileImage) VALUES (?, ?, ?, ?, ?)";
	    String updateUserExtraInfoQuery = "UPDATE userExtraInfo SET phoneNumber = ?, role = ?, bio = ?, profileImage = ? WHERE userId = ?";

	    try (Connection conn = DBConnection.getConnection();){
	         PreparedStatement updateUserDetailsStmt = conn.prepareStatement(updateUserDetailsQuery);
	         PreparedStatement checkUserExtraInfoStmt = conn.prepareStatement(checkUserExtraInfoQuery);
	         PreparedStatement insertUserExtraInfoStmt = conn.prepareStatement(insertUserExtraInfoQuery);
	         PreparedStatement updateUserExtraInfoStmt = conn.prepareStatement(updateUserExtraInfoQuery);

	        conn.setAutoCommit(false); // Start transaction

	        // Update userDetails table
	        updateUserDetailsStmt.setString(1, userName);
	        updateUserDetailsStmt.setString(2, email);
	        updateUserDetailsStmt.setString(3, userId);
	        int userDetailsUpdated = updateUserDetailsStmt.executeUpdate();

	        // Check if userId exists in userExtraInfo
	        checkUserExtraInfoStmt.setString(1, userId);
	        ResultSet rs = checkUserExtraInfoStmt.executeQuery();
	        rs.next();
	        boolean exists = rs.getInt(1) > 0;

	        int userExtraInfoUpdated = 0;
	        if (exists) {
	            // Update if userId exists
	            updateUserExtraInfoStmt.setString(1, phoneNumber);
	            updateUserExtraInfoStmt.setString(2, role);
	            updateUserExtraInfoStmt.setString(3, bio);
	            updateUserExtraInfoStmt.setString(4, profileImage);
	            updateUserExtraInfoStmt.setString(5, userId);
	            userExtraInfoUpdated = updateUserExtraInfoStmt.executeUpdate();
	        } else {
	            // Insert if userId does not exist
	            insertUserExtraInfoStmt.setString(1, userId);
	            insertUserExtraInfoStmt.setString(2, phoneNumber);
	            insertUserExtraInfoStmt.setString(3, role);
	            insertUserExtraInfoStmt.setString(4, bio);
	            insertUserExtraInfoStmt.setString(5, profileImage);
	            userExtraInfoUpdated = insertUserExtraInfoStmt.executeUpdate();
	        }

	        if (userDetailsUpdated > 0 && userExtraInfoUpdated > 0) {
	            conn.commit(); // Commit transaction if both succeed
	            return true;
	        } else {
	            conn.rollback(); // Rollback if any fails
	            return false;
	        }
	    } catch (SQLException e) {
	        System.err.println("Error updating/inserting user info: " + e.getMessage());
	        return false;
	    }
    }
	
	public static JSONObject getUserWithExtraInfo(String userId) {
		 JSONObject userJson = new JSONObject();
		    String query = "SELECT u.userName, u.email, e.phoneNumber, e.role, e.bio, e.profileImage " +
		                   "FROM userDetails u " +
		                   "LEFT JOIN userExtraInfo e ON u.userId = e.userId " +
		                   "WHERE u.userId = ?";

		    try (   Connection conn = DBConnection.getConnection();
					PreparedStatement pstmt = conn.prepareStatement(query)) {
		        pstmt.setString(1, userId);
		        ResultSet rs = pstmt.executeQuery();

		        if (rs.next()) {
		            userJson.put("userName", rs.getString("userName"));
		            userJson.put("email", rs.getString("email"));
		            userJson.put("phoneNumber", rs.getString("phoneNumber"));
		            userJson.put("role", rs.getString("role"));
		            userJson.put("bio", rs.getString("bio"));
		            userJson.put("profileImage", rs.getString("profileImage"));
		        }
		    } catch (SQLException e) {
		        System.err.println("Error fetching user with extra info: " + e.getMessage());
		    }

		    return userJson;
	}
	
	public static String encodePassword(String password) {
        return Base64.getEncoder().encodeToString(password.getBytes());
    }

    public static String decodePassword(String encodedPassword) {
        byte[] decodedBytes = Base64.getDecoder().decode(encodedPassword);
        return new String(decodedBytes);
    }
    
    
}

