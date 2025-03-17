package Validation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import DAO.DBConnection;

public class GetUserIdBySessionId {
	public static Connection conn;

    static {
        try {
            conn = DBConnection.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getUserID(String sessionId) {
		
		try {
			String userQuery = "SELECT userId FROM session WHERE sessionId = ?";
            PreparedStatement pstmtUser = conn.prepareStatement(userQuery);
            pstmtUser.setString(1, sessionId);
            ResultSet rs = pstmtUser.executeQuery();

            String userId = null;
            if (rs.next()) {
                userId = rs.getString("userId");
            } 
            return userId;
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			// TODO: handle exception
		}
		
		return null;
	}
}
