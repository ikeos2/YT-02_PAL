package api;

import java.sql.*;

public class Connect {
	private final static String DBUSER = "dbu309yt02";
	private final static String DBPASS = "PfV9gzujb2x";
	private final static String URL = "jdbc:mysql://mysql.cs.iastate.edu:3306/db309yt02?useSSL=false";
	
	public static String getUsername(String username){
		try{
			Connection connection = DriverManager.getConnection(URL,DBUSER,DBPASS);
			Statement stmt = connection.createStatement();
			ResultSet rs;
			
			rs = stmt.executeQuery("SELECT username FROM Users");
			while(rs.next()){
				username = rs.getString("username");
				
			}
			connection.close();
		}
		catch(Exception e){
			System.err.println("Got an exception");
			System.err.println(e.getMessage());
		}
		return username;
	}
	public static String getPassword(String password){
		try{
			Connection connection = DriverManager.getConnection(URL,DBUSER,DBPASS);
			Statement stmt = connection.createStatement();
			ResultSet rs;
		
			rs = stmt.executeQuery("SELECT password_hash FROM Users");
			while(rs.next()){
				password = rs.getString("password_hash");
								
			}
			connection.close();
		}
		catch(Exception e){
			System.err.println("Got an exception");
			System.err.println(e.getMessage());
		}
		return password;
	}
	public static String getUserType(String user_type){
		try{
			Connection connection = DriverManager.getConnection(URL,DBUSER,DBPASS);
			Statement stmt = connection.createStatement();
			ResultSet rs;
		
			rs = stmt.executeQuery("SELECT user_type FROM Users");
			while(rs.next()){
				user_type = rs.getString("user_type");
								
			}
			connection.close();
		}
		catch(Exception e){
			System.err.println("Got an exception");
			System.err.println(e.getMessage());
		}
		return user_type;
		
	}
	public static String getCourseName(String course_name){
		try{
			Connection connection = DriverManager.getConnection(URL,DBUSER,DBPASS);
			Statement stmt = connection.createStatement();
			ResultSet rs;
		
			rs = stmt.executeQuery("SELECT course_name FROM Users");
			while(rs.next()){
				course_name = rs.getString("course_name");
								
			}
			connection.close();
		}
		catch(Exception e){
			System.err.println("Got an exception");
			System.err.println(e.getMessage());
		}
		return course_name;
		
	}
}