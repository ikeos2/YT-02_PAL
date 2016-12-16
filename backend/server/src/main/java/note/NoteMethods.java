package note;

import java.sql.*;

import api.Logger;

public class NoteMethods {

	private final static String DBUSER = "dbu309yt02";
	private final static String DBPASS = "PfV9gzujb2x";
	private final static String URL = "jdbc:mysql://mysql.cs.iastate.edu:3306/db309yt02?useSSL=false";

	private static Connection connection;
	private static Statement stmt;
	private static ResultSet rset;

	
	
	public static Boolean addNote(String data, String owner, String collabs, String title) {
		// Won't let you add a note if there is no title or owner
		if (owner == null || title == null) {
			return false;
		}
		
		// TODO: parse collabs into a list of users, owner to a user, and store all this data.
		String add = "INSERT INTO Notes(note_data, note_owner, note_collaborators, note_title) VALUES('" + data + "', '"
				+ owner + "', '" + collabs + "', '" + title + "')";

		String check = "SELECT * FROM Notes WHERE note_title = '" + title + "' AND note_owner = '" + owner + "'";

		try {
			connection = DriverManager.getConnection(URL, DBUSER, DBPASS);
			stmt = connection.createStatement();

			rset = stmt.executeQuery(check);
			if (rset.next()) {
				
				connection.close();
				return false;
			}
			stmt.executeUpdate(add);

			stmt.close();
			connection.close();
			return true;

		} catch (SQLException e) {
			e.printStackTrace();
			Logger.log(e);

		} finally{
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
				Logger.log(e);
			}
		}

		return false;
	}

	public static boolean updateNote(String title, String owner, String data) {
		
		String update = "UPDATE Notes SET note_data='" + data + "' WHERE note_title='" + title + "' AND note_owner = '"+owner+"'";

		try {
			connection = DriverManager.getConnection(URL, DBUSER, DBPASS);
			stmt = connection.createStatement();

			stmt.executeUpdate(update);

			stmt.close();
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

	public static boolean updateTitle(String owner, String title, String data) {
		String newTitle = "UPDATE Notes SET note_title = '" + title + "' WHERE note_owner = '"+owner+"' AND note_data = '"+data+"'";
		
		String check = "SELECT * FROM Notes WHERE note_title = '" + title + "' AND note_owner = '" + owner + "'";

		try {
			connection = DriverManager.getConnection(URL, DBUSER, DBPASS);
			stmt = connection.createStatement();

			rset = stmt.executeQuery(check);
			if (rset.next()) {
				connection.close();
				return false;
			}
			stmt.executeUpdate(newTitle);

			stmt.close();
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

	public static boolean updateDataTitle(String owner, int id, String data, String title){
		String dataTitle = "UPDATE Notes SET note_title = '"+title+"', note_data = '"+data+"' WHERE note_owner = '"+owner+"' AND note_uid = '"+id+"'";
		
		String check = "SELECT * FROM Notes WHERE note_title = '"+title+"' AND note_data = '"+data+"'";
		try {
			connection = DriverManager.getConnection(URL, DBUSER, DBPASS);
			stmt = connection.createStatement();

			rset = stmt.executeQuery(check);
			if (rset.next()) {
				connection.close();
				return false;
			}
			stmt.executeUpdate(dataTitle);

			
			stmt.close();
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

	public static boolean deleteNote(String title) {
		String delete = "DELETE FROM Notes " + "WHERE note_title = '" + title + "'";

		try {
			connection = DriverManager.getConnection(URL, DBUSER, DBPASS);
			stmt = connection.createStatement();

			stmt.executeUpdate(delete);

			stmt.close();
			connection.close();

			return true;

		} catch (SQLException e) {
			e.printStackTrace();
			Logger.log(e);
			return false;
		} finally{
			try {
				connection.close();
			} catch (SQLException e) {
				Logger.log(e);
				e.printStackTrace();
			}
		}
	}

	public static boolean shareNote(String title, String collabs) {
		String update = "UPDATE Notes SET note_collaborators='" + collabs + "' WHERE note_title=" + title;

		try {
			connection = DriverManager.getConnection(URL, DBUSER, DBPASS);
			stmt = connection.createStatement();

			stmt.executeUpdate(update);
			
			stmt.close();
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

	public static Note[] getNotes(String note_owner) {
		String select = "SELECT note_title FROM Notes WHERE note_owner='" + note_owner + "'";
		String count = "SELECT count(*) as size FROM Notes where note_owner='" + note_owner + "'";
		int rsetSize;

		Note notes[] = null;

		try {
			connection = DriverManager.getConnection(URL, DBUSER, DBPASS);
			stmt = connection.createStatement();
			// Count of the return set elements
			rset = stmt.executeQuery(count);
			rset.next();
			rsetSize = rset.getInt("size");
			rset.close();
			rset = stmt.executeQuery(select);

			notes = new Note[rsetSize];
			String note_titles[] = new String[rsetSize];

			for (int i = 0; i < rsetSize; i++) {
				rset.next();
				note_titles[i] = rset.getString("note_title");
			}

			rset.close();

			for (int i = 0; i < rsetSize; i++) {
				notes[i] = getNote(note_titles[i]);
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

		return notes;
	}

	public static Note getNote(String title) {
		String select = "SELECT * FROM Notes WHERE note_title='" + title + "'";

		Note note;
		String note_title = null;
		String note_data = null;
		String owner_username = null;
		String note_collaborators = null;
		int note_uid = 0;

		try {
			connection = DriverManager.getConnection(URL, DBUSER, DBPASS);
			stmt = connection.createStatement();

			rset = stmt.executeQuery(select);

			while (rset.next()) {
				note_title = rset.getString("note_title");
				owner_username = rset.getString("note_owner");
				note_collaborators = rset.getString("note_collaborators");
				note_data = rset.getString("note_data");
				note_uid = rset.getInt("note_uid");
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

		note = new Note(note_title, owner_username, note_collaborators, note_data, note_uid);
		return note;
	}
}
