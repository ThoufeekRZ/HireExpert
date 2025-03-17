package utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import DAO.DBConnection;
import DAO.UserDAO;

public class UpdatePassword {
	static Connection conn;

    static {
        try {
            conn = DBConnection.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean updatePassword(String userId, String newPassword) {
		newPassword=UserDAO.encodePassword(newPassword);
        boolean isUpdated = false;
        String query = "UPDATE userDetails SET password = ? WHERE userId = ?";

        try {
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
