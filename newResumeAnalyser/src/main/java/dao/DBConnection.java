package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.mysql.cj.jdbc.Driver;

public class DBConnection {
static Connection connection=null;
	
	private DBConnection() {
		
	}
	
	public static Connection getConnection() throws SQLException {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		String url = "jdbc:mysql://localhost:3306/resume_analyser"; 
	    String name = "root";  
	    String pass = "Thoufeek@2006";
	    if(connection==null || connection.isClosed()) {
	    	try{

	    		connection=DriverManager.getConnection(url,name,pass);
		    }
		    catch(SQLException e) {
		    	System.out.println(e.getMessage());

		    }
	    }
	    return connection;
	    
	}
}
