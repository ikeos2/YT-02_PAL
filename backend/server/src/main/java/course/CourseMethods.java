package course;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


import api.Logger;

public class CourseMethods {

	private final static String DBUSER = "dbu309yt02";
	private final static String DBPASS = "PfV9gzujb2x";
	private final static String URL = "jdbc:mysql://mysql.cs.iastate.edu:3306/db309yt02?useSSL=false";

	private static Connection connection;
	private static Statement stmt;
	private static ResultSet rset;
	private static PreparedStatement ps;

	public static boolean addCourse(String owner, String name, String instructor, String location, String startTime,
			String endTime, String room, String days) {
		String add = "INSERT INTO Courses(course_owner, course_name, course_instructor, session_locations, start_time, end_time, room_number, days) VALUES('"
				+ owner + "','" + name + "', '" + instructor + "', '" + location + "','" + startTime + "', '" + endTime
				+ "', '" + room + "', '" + days + "')";

		String check = "SELECT * FROM Courses WHERE course_owner = '"+owner+"' AND course_name = '" + name + "' AND course_instructor = '" + instructor
				+ "' AND session_locations = '" + location + "' AND start_time = '" + startTime + "' AND end_time = '"
				+ endTime + "' AND room_number = '" + room + "' AND days = '" + days + "'";
		try {
			connection = DriverManager.getConnection(URL, DBUSER, DBPASS);
			stmt = connection.createStatement();

			rset = stmt.executeQuery(check);
			if (rset.next()) {
			
				return false;
			}
			stmt.executeUpdate(add);

			
			stmt.close();
			connection.close();
			return true;

		} catch (SQLException e) {
			Logger.log(e);
			e.printStackTrace();	
		} finally{
			try {
				connection.close();
			} catch (SQLException e) {
				Logger.log(e);
				e.printStackTrace();
			}
		}
		return false;
	}

	public static boolean deleteCourse(int courseUID) {
		String delete = "DELETE FROM Courses WHERE course_uid = '" + courseUID + "'";

		try {
			connection = DriverManager.getConnection(URL, DBUSER, DBPASS);
			stmt = connection.createStatement();

			stmt.executeUpdate(delete);
			
			stmt.close();
			connection.close();
			return true;
		} catch (SQLException e) {
			Logger.log(e);
			e.printStackTrace();
		} finally{
			try {
				connection.close();
			} catch (SQLException e) {
				Logger.log(e);
				e.printStackTrace();
			}
		}
		return false;
	}

	public static Course getCourse(String name) {
		String select = "SELECT * FROM Courses WHERE course_name='" + name + "'";

		Course course = null;

		String docNames;
		String startTime;
		String endTime;
		String instructor;
		String location;
		String courseName;
		String owner;
		String roomNumber;
		String days;
		int uid;

		try {
			connection = DriverManager.getConnection(URL, DBUSER, DBPASS);
			stmt = connection.createStatement();

			rset = stmt.executeQuery(select);

			while (rset.next()) {
				roomNumber = rset.getString("room_number");
				days = rset.getString("days");
				owner = rset.getString("course_owner");
				docNames = rset.getString("document_names");
				startTime = rset.getString("start_time");
				endTime = rset.getString("end_time");
				instructor = rset.getString("course_instructor");
				location = rset.getString("session_locations");
				courseName = rset.getString("course_name");
				uid = rset.getInt("course_uid");

				course = new Course(courseName, docNames, startTime, endTime, instructor, location, uid, owner,
						roomNumber, days);
			}
			
			stmt.close();
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
			Logger.log(e);
		} finally{
			try {
				connection.close();
			} catch (SQLException e) {
				Logger.log(e);
				e.printStackTrace();
			}
		}
		return course;
	}

	public static boolean updateCourse(String start, String end, String instructor, String location, int id,
			String roomNumber, String days) {
		String update = "UPDATE Courses SET start_time = '" + start + "', end_time = '" + end
				+ "', course_instructor = '" + instructor + "', session_locations = '" + location + "', room_number = '"
				+ roomNumber + "', days = '" + days + "' WHERE course_uid = '" + id + "'";

		try {
			connection = DriverManager.getConnection(URL, DBUSER, DBPASS);
			PreparedStatement ps = connection.prepareStatement(update);
			ps.executeUpdate();

			ps.close();
			connection.close();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			Logger.log(e);
		} finally{
			try {
				connection.close();
			} catch (SQLException e) {
				Logger.log(e);
				e.printStackTrace();
			}
		}
		return false;
	}

	/**
	 * Returns a short blerp about the course"
	 */
	public static String courseSummary(int courseUID) {
		String select = "SELECT * FROM Courses WHERE course_uid = '" + courseUID + "'";
		String summary;
		try {
			connection = DriverManager.getConnection(URL, DBUSER, DBPASS);
			stmt = connection.createStatement();
			rset = stmt.executeQuery(select);

			while (rset.next()) {
				summary = rset.getString("course_summary");
				connection.close();
				return summary;
			}
			
			stmt.close();
			connection.close();
			
		} catch (SQLException e) {
			Logger.log(e);
			e.printStackTrace();
		} finally{
			try {
				connection.close();
			} catch (SQLException e) {
				Logger.log(e);
				e.printStackTrace();
			}
		}
		return "Summary not found";
	}

	/**
	 * Returns the location data about the course.
	 */
	public static String courseLocation(int courseUID) {
		String select = "SELECT * FROM Courses WHERE course_uid = '" + courseUID + "'";
		String location;
		try {
			connection = DriverManager.getConnection(URL, DBUSER, DBPASS);
			stmt = connection.createStatement();
			rset = stmt.executeQuery(select);

			while (rset.next()) {
				location = rset.getString("session_locations");
				connection.close();
				return location;
			}
			
			stmt.close();
			connection.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
			Logger.log(e);
		} finally{
			try {
				connection.close();
			} catch (SQLException e) {
				Logger.log(e);
				e.printStackTrace();
			}
		}
		return "Location not found";
	}

	public static Course[] getCourses(String course_owner) {
		String select = "SELECT course_name FROM Courses WHERE course_owner='" + course_owner + "'";
		String count = "SELECT count(*) as size FROM Courses where course_owner='" + course_owner + "'";
		int rsetSize;

		Course courses[] = null;

		try {
			connection = DriverManager.getConnection(URL, DBUSER, DBPASS);
			stmt = connection.createStatement();
			// Count of the return set elements
			rset = stmt.executeQuery(count);
			rset.next();
			rsetSize = rset.getInt("size");
			rset.close();
			rset = stmt.executeQuery(select);

			courses = new Course[rsetSize];
			String course_names[] = new String[rsetSize];

			for (int i = 0; i < rsetSize; i++) {
				rset.next();
				course_names[i] = rset.getString("course_name");
			}

			rset.close();

			for (int i = 0; i < rsetSize; i++) {
				courses[i] = getCourse(course_names[i]);
			}
			
			stmt.close();
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
			Logger.log(e);
		} finally{
			try {
				connection.close();
			} catch (SQLException e) {
				Logger.log(e);
				e.printStackTrace();
			}
		}

		return courses;
	}

	public static String insertFile(String document_name, String path, String course_owner, String course_name) {
		String update = "UPDATE Courses SET document_names='"+document_name+"', files=? WHERE course_owner=? AND course_name='"+course_name+"'";
		try {
			connection = DriverManager.getConnection(URL, DBUSER, DBPASS);
			stmt = connection.createStatement();
			ps = connection.prepareStatement(update);
			
			File file = new File(path);
			FileInputStream input = new FileInputStream(file);
			
			ps.setBinaryStream(1, input);
			ps.setString(2, course_owner);

			ps.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
			return "not uploaded";
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return "File not found";
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
				return "not uploaded";
			}
		}
		return "success";
	}
}
