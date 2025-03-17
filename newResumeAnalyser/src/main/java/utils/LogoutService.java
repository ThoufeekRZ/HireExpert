package utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

//import controllers.PreparedStatement;
//import controllers.SQLException;
import dao.DBConnection;
//import jakarta.servlet.http.HttpServletResponse;

public class LogoutService {

	public static boolean logout(String sessionId) {
		try (Connection conn=DBConnection.getConnection();) {
            String sql = "DELETE FROM session WHERE sessionId = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, sessionId);
                int rowsAffected = pstmt.executeUpdate();

                if (rowsAffected > 0) {
                	return true;
                } 
                
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
		return false;
	}
}
