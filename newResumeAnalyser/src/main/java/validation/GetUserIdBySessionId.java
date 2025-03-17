package validation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import dao.DBConnection;

public class GetUserIdBySessionId {

	public static String getUserID(String sessionId) {
		
		try(Connection conn=DBConnection.getConnection();) {
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
