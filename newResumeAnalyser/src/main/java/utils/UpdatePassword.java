package utils;

import java.sql.Connection;
import java.sql.PreparedStatement;

import dao.DBConnection;
import dao.UserDAO;

public class UpdatePassword {

	public static boolean updatePassword(String userId, String newPassword) {
		newPassword=UserDAO.encodePassword(newPassword);
        boolean isUpdated = false;
        String query = "UPDATE userDetails SET password = ? WHERE userId = ?";

        try(Connection conn=DBConnection.getConnection();) {
             PreparedStatement stmt = conn.prepareStatement(query);
             
            stmt.setString(1, newPassword);
            stmt.setString(2, userId);
            int rowsAffected = stmt.executeUpdate();
            
            isUpdated = rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isUpdated;
    }
}
