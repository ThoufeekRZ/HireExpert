package utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.json.JSONObject;

import dao.DBConnection;
import dao.UserDAO;

public class PasswordChangeService {

	
	public static JSONObject changePassword(String sessionId,String newPassword) {
		
		newPassword=UserDAO.encodePassword(newPassword);
		JSONObject responseJson = new JSONObject();

        try(Connection conn=DBConnection.getConnection();) {
            // 1️⃣ Get userId from sessionId in the session table
            String userId = getUserIdFromSession(conn, sessionId);
            
            if (userId == null) {
                responseJson.put("status", "error");
                responseJson.put("message", "Invalid session. User not found.");
                return responseJson;
            }

            // 2️⃣ Update the password in the userDetails table
            boolean isUpdated = updatePasswordInUserDetails(conn, userId, newPassword);
            
            if (isUpdated) {
                responseJson.put("status", "success");
                responseJson.put("message", "Password updated successfully.");
            } else {
                responseJson.put("status", "error");
                responseJson.put("message", "Password update failed.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            responseJson.put("status", "error");
            responseJson.put("message", "Internal server error.");
        }

        return responseJson;
		
	}
	
	private static String getUserIdFromSession(Connection conn, String sessionId) {
        String userId = null;
        String query = "SELECT userId FROM session WHERE sessionId = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, sessionId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                userId = rs.getString("userId");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userId;
    }

    private static boolean updatePasswordInUserDetails(Connection conn, String userId, String newPassword) {
        boolean isUpdated = false;
        String query = "UPDATE userDetails SET password = ? WHERE userId = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, newPassword);
            pstmt.setString(2, userId);
            int rowsUpdated = pstmt.executeUpdate();
            isUpdated = rowsUpdated > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isUpdated;
    }
}
