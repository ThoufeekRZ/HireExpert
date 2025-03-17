package DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
	private static Connection connection = null;

	private DBConnection() {
	}

	// Load MySQL driver once
	static {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			System.out.println("✅ MySQL Driver loaded successfully.");
		} catch (ClassNotFoundException e) {
			System.err.println("❌ MySQL Driver not found.");
			e.printStackTrace();
			throw new ExceptionInInitializerError(e);
		}
	}

	public static Connection getConnection() throws SQLException {
		String url = "jdbc:mysql://localhost:3306/resume_analyser";
		String name = "root";
		String pass = "Thoufeek@2006";

		if (connection == null || connection.isClosed()) {
			try {
				connection = DriverManager.getConnection(url, name, pass);
				System.out.println("✅ Database connection established.");
			} catch (SQLException e) {
				System.err.println("❌ Database connection failed: " + e.getMessage());
				throw e; // Throw exception instead of returning null
			}
		}
		return connection;
	}
}
