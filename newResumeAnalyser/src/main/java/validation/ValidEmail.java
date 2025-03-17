package validation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import dao.DBConnection;

public class ValidEmail {

	
	public static boolean isValidEmail(String email) {

		String query = "SELECT COUNT(*) FROM userDetails WHERE email = ?";
        boolean exists = false;

        try(Connection conn = DBConnection.getConnection();) {
             PreparedStatement pstmt = conn.prepareStatement(query);

            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                exists = rs.getInt(1) > 0; 
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return exists;
	}
}
