package utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

//import controllers.PreparedStatement;
//import controllers.SQLException;
import DAO.DBConnection;
//import jakarta.servlet.http.HttpServletResponse;

public class LogoutService {
	public static Connection conn;

    static {
        try {
            conn = DBConnection.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean logout(String sessionId) {
		try  { 
            String sql = "DELETE FROM session WHERE sessionId = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, sessionId);
                int rowsAffected = pstmt.executeUpdate();

                if (rowsAffected > 0) {
                	return true;
//                    response.setStatus(HttpServletResponse.SC_OK);
//                    response.getWriter().write("{\"message\": \"Session deleted successfully\"}");
                } 
//                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
//                    response.getWriter().write("{\"error\": \"Session not found\"}");
                
            }
        } catch (SQLException e) {
            e.printStackTrace();
            
//            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//            response.getWriter().write("{\"error\": \"Database error occurred\"}");
        }
		return false;
	}
}
