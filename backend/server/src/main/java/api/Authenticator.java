package api;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Authenticator {

	private final static String DBUSER = "dbu309yt02";
	private final static String DBPASS = "PfV9gzujb2x";
	private final static String URL = "jdbc:mysql://mysql.cs.iastate.edu:3306/db309yt02?useSSL=false";

	private static Connection connection;
	private static ResultSet rset;
	
	public static boolean authenticate(String token){
		String select = "SELECT * FROM Users WHERE token='" + token +"'";
		try {
			connection = DriverManager.getConnection(URL, DBUSER, DBPASS);
			Statement stmt = connection.createStatement();
			rset = stmt.executeQuery(select);
			while (rset.next()) {
				connection.close();
				return true;
			}

			connection.close();

		} catch (SQLException e) {
			e.printStackTrace();
			Logger.log(e);
			return false;
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
				Logger.log(e);
			}
		}
		return false;
	}
	
	/**
	 * 
	 * @param token token used to authenticate user is logged in
	 * @param AuthLevel 1 = student, 2 = Faculty, 3 = admin
	 * @return returns if the user's token is allowed to access the method
	 */
	public static boolean authenticate(String token, int AuthLevel){
		String select = "SELECT * FROM Users WHERE token='" + token +"'";
		try {
			connection = DriverManager.getConnection(URL, DBUSER, DBPASS);
			Statement stmt = connection.createStatement();
			rset = stmt.executeQuery(select);
			if(rset.next()) {
				//Check  rset.getString("password_hash");
				
				String userLevel = rset.getString("user_type").toLowerCase();
				boolean Auth = false;
				switch(userLevel){
				case "student":
					if(AuthLevel <= 1) Auth = true;
					break;
				case "faculty":
					if(AuthLevel <= 2) Auth = true;
					break;
				case "admin":
					Auth = true;
					break;
				}
				connection.close();
				return Auth;
			}

			connection.close();

		} catch (SQLException e) {
			e.printStackTrace();
			Logger.log(e);
			return false;
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
				Logger.log(e);
			}
		}
		return false;
	}

	
}
