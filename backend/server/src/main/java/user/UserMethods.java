package user;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.Random;

import api.Logger;

public class UserMethods {

	private final static String DBUSER = "dbu309yt02";
	private final static String DBPASS = "PfV9gzujb2x";
	private final static String URL = "jdbc:mysql://mysql.cs.iastate.edu:3306/db309yt02?useSSL=false";
	private static Random rand = new Random(System.currentTimeMillis());

	private static Connection connection;
	private static Statement stmt;
	private static ResultSet rset;

	public static User getUser(String user) {
		String select = "SELECT * FROM Users WHERE username='" + user + "'";

		User tmp = null;
		String pw;
		String uname;
		Long uid;
		String type;
		String active;
		boolean loggedin;
		String email;

		try {
			connection = DriverManager.getConnection(URL, DBUSER, DBPASS);
			stmt = connection.createStatement();
			rset = stmt.executeQuery(select);

			while (rset.next()) {
				pw = rset.getString("password_hash");
				uname = rset.getString("username");
				uid = rset.getLong("uid");
				type = rset.getString("user_type");
				active = rset.getString("user_active");
				loggedin = rset.getBoolean("loggedin");
				email = rset.getString("user_email");

				tmp = new User(uid, uname, pw, type, active, loggedin, email);
			}

			stmt.close();// Closed Connections
			connection.close();

		} catch (SQLException e) {
			e.printStackTrace();
			Logger.log(e);
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
				Logger.log(e);
			}
		}
		return tmp;
	}
	
	/**
	 * Inserts user into database and creates account so user can use the app. 
	 * @param user username created
	 * @param password password created
	 * @param email email created
	 * @param userType type defined, Student is default
	 * @return String - "User created successfully" will return if user is now in database
	 */
	public static String insertUser(String user, String password, String email, String userType) {
		if (user == null | user.isEmpty() | password == null | password.isEmpty() | email == null | email.isEmpty())
			return null;

		String select = "INSERT INTO Users(username, password_hash, user_type, user_email, user_active) VALUES('" + user
				+ "', '" + password + "', '" + userType + "', '" + email + "', 1)";
		
		//Checks if username is already taken
		String check = "SELECT * FROM Users WHERE username = '" + user + "'";

		try {
			connection = DriverManager.getConnection(URL, DBUSER, DBPASS);
			stmt = connection.createStatement();
			rset = stmt.executeQuery(check);

			if (rset.next()) {

				return "Username already exists";
			}

			stmt.executeUpdate(select);

			stmt.close();
			connection.close();

		} catch (SQLException e) {
			Logger.log(e);
			return "User not created\n";
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
				Logger.log(e);
			}
		}
		return "User created successfully";
	}

	public static String deleteUser(String user) {
		String delete = "DELETE FROM Users WHERE username = '" + user + "'";

		try {
			connection = DriverManager.getConnection(URL, DBUSER, DBPASS);
			stmt = connection.createStatement();

			stmt.executeUpdate(delete);

			stmt.close();
			connection.close();

		} catch (SQLException e) {
			Logger.log(e);
			return "User not deleted.";
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
				Logger.log(e);
			}
		}
		return "User deleted successfully";
	}

	// Token login
	public static String login(String user, String password, int seed) {
		if (seed == 0) {
			seed = rand.nextInt();
		}

		String select = "SELECT * FROM Users WHERE username='" + user + "' AND password_hash='" + password
				+ "' AND user_active=1"; // check that username and PW hash are
											// correct,
											// that user is active, and that the
											// user isn't already logged in(has
											// a token)
		try {
			connection = DriverManager.getConnection(URL, DBUSER, DBPASS);
			stmt = connection.createStatement();
			rset = stmt.executeQuery(select);

			while (rset.next()) {

				String token = generateToken(rset.getInt("uid"), seed);
				String update = "UPDATE Users SET loggedin=1,token='" + token + "' WHERE username='" + user + "'";

				stmt.executeUpdate(update);

				return token;
			}

			stmt.close();
			connection.close();

		} catch (SQLException e) {
			e.printStackTrace();
			Logger.log(e);
			return "An error occured";
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
				Logger.log(e);
			}
		}
		return "Unable to log in.";
	}

	private static String generateToken(int user_ID, int seed) {
		String token = "";

		token = hash(Integer.toString(seed) + Integer.toString(user_ID));

		return token;
	}

	public static String hash(String password) {
		try {
			MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
			String salt = "some_random_salt";
			String passWithSalt = password + salt;
			byte[] passBytes = passWithSalt.getBytes();
			byte[] passHash = sha256.digest(passBytes);
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < passHash.length; i++) {
				sb.append(Integer.toString((passHash[i] & 0xff) + 0x100, 16).substring(1));
			}
			String generatedPassword = sb.toString();
			return generatedPassword;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			Logger.log(e);
		}
		return null;
	}

	public static boolean logout(String token) {
		String select = "UPDATE Users SET loggedin ='0', token= null WHERE token='" + token + "'";

		try {
			connection = DriverManager.getConnection(URL, DBUSER, DBPASS);
			stmt = connection.createStatement();

			stmt.executeUpdate(select);

			stmt.close();
			connection.close();

		} catch (SQLException e) {
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

		return true;
	}

	public static boolean logout(String token, int uid) {
		String select = "UPDATE Users SET loggedin='0',token=null WHERE token='" + token + "' AND uid=" + uid;

		try {
			connection = DriverManager.getConnection(URL, DBUSER, DBPASS);
			stmt = connection.createStatement();

			stmt.executeUpdate(select);

			stmt.close();
			connection.close();

		} catch (SQLException e) {
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

		return true;
	}

	public static void forceLogout() {
		String update = "UPDATE Users set token=null WHERE UID<100";
		try {
			connection = DriverManager.getConnection(URL, DBUSER, DBPASS);
			stmt = connection.createStatement();

			stmt.executeUpdate(update);

			stmt.close();
			connection.close();

		} catch (SQLException e) {
			Logger.log(e);
			e.printStackTrace();
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
				Logger.log(e);
			}
		}
	}

	public static String refreshToken(String token, String username) {
		String select = "SELECT * FROM Users WHERE username=" + username + " AND token='" + token + "'";

		try {
			connection = DriverManager.getConnection(URL, DBUSER, DBPASS);
			stmt = connection.createStatement();
			rset = stmt.executeQuery(select);
			while (rset.next()) {

				String newToken = generateToken(rset.getInt("UID"), rand.nextInt());
				if (newToken == null) {
					System.out.println("String is null!");
				}

				String update = "UPDATE Users SET loggedin=1,token='" + newToken + "' WHERE username=" + username;
				stmt.executeUpdate(update);

				return newToken;
			}

			stmt.close();
			connection.close();

			return "Unable to refresh";

		} catch (SQLException e) {
			e.printStackTrace();
			Logger.log(e);
			return "An error occured";
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
				Logger.log(e);
			}
		}
	}

	public static String userExists(String user) {
		String check = "SELECT * FROM Users WHERE username = '" + user + "'";

		try {
			connection = DriverManager.getConnection(URL, DBUSER, DBPASS);
			stmt = connection.createStatement();

			rset = stmt.executeQuery(check);

			if (rset.next()) {

				return "User already exists";
			}

			stmt.close();
			connection.close();

		} catch (SQLException e) {
			e.printStackTrace();
			Logger.log(e);
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				Logger.log(e);
			}
		}
		return "User does not exist";
	}

	public static String changePassword(String username, String password, String token, boolean reset) {
		String update = null;
		if(reset){
			// if the user's PW needs reseting
			update = "UPDATE Users SET password_hash = '" + password + "' WHERE username='" + username + "'";
		} else {
			update = "UPDATE Users SET password_hash = '" + password + "' WHERE username='" + username + "'";
		}

		try {
			connection = DriverManager.getConnection(URL, DBUSER, DBPASS);
			stmt = connection.createStatement();

			stmt.executeUpdate(update);
			return refreshToken(token, username);

		} catch (SQLException e) {
			Logger.log(e);
			return "false";
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				Logger.log(e);
			}
		}

	}

	public static String changeUsername(String username, String token) {
		String update = "UPDATE Users SET username = '" + username + "' WHERE token = '" + token + "'";
		String select = "SELECT username FROM Users WHERE token = '" + token + "'";
		String check = "SELECT * FROM Users WHERE username = '" + username + "'";
		String s = null;

		try {
			connection = DriverManager.getConnection(URL, DBUSER, DBPASS);
			stmt = connection.createStatement();
			
			ResultSet rset1 = stmt.executeQuery(check);
			if (rset1.next()) {
				return "Username already exists";
			}
			
			stmt.executeUpdate(update);
			
			ResultSet rset2 = stmt.executeQuery(select);
			while (rset2.next()) {
				s = rset.getString("username");
			}

		} catch (SQLException e) {
			Logger.log(e);
			return "false";
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				Logger.log(e);
			}
		}
		return s;
	}
	
	public static String checkSecurityAnswer(String answer, String username){
		String select = "SELECT * FROM Users WHERE username='"+username+"' AND security_a1='"+answer+"'";
		
		try {
			connection = DriverManager.getConnection(URL, DBUSER, DBPASS);
			stmt = connection.createStatement();
			
			ResultSet rset2 = stmt.executeQuery(select);
			while (rset2.next()) {
				connection.close();
				stmt.close();
				return "Correct";
			}

		} catch (SQLException e) {
			Logger.log(e);
			return "Something went wrong: " + e.toString();
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				Logger.log(e);
			}
		}
		return "Security answer doesn't match";
	}
}
