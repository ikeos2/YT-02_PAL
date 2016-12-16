package main;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Scanner;

public class Util {
	/**
	 * Logs user in, since test user shouldn't change creds they are hardcoded
	 * @return the token returned
	 */
	public static String login(){
		String url = "http://proj-309-yt-02.cs.iastate.edu:8080/login";
		String charset = "UTF-8";
		String name = "ReTester";
		String password = "hello";
		 
		try{

			String query = "name="+name+"&password=" + password;
			
			
			HttpURLConnection connection = (HttpURLConnection) new URL(url + "?" + query).openConnection();
			connection.setRequestProperty("Accept-Charset", charset);		
			InputStream response = connection.getInputStream();
			
			try (Scanner scanner = new Scanner(response)) {
			    String responseBody = scanner.useDelimiter("\\A").next();
			    return responseBody;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Logs out the user, used in after() method to close connections
	 * @param token
	 */
	public static void logout(String token){
		String uid = "31";
		String url = "http://proj-309-yt-02.cs.iastate.edu:8080/logout";
		String charset = "UTF-8";
		//TODO I dont think this works, user remains logged in even after tests.
		try{

			String query = String.format("uid=%s&token=%s", 
					URLEncoder.encode(uid, charset), 
					URLEncoder.encode(token, charset));
			
			//String query = "uid="+uid+"&token="+token;
			
			HttpURLConnection connection = (HttpURLConnection) new URL(url + "?" + query).openConnection();
			connection.setRequestProperty("Accept-Charset", charset);		
			InputStream response = connection.getInputStream();
			
			try (Scanner scanner = new Scanner(response)) {
			    String responseBody = scanner.useDelimiter("\\A").next();
			    if(responseBody.equals("true"))  return;
			    System.out.println("Something went wrong, response not true: " + responseBody);
			}

			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Connects to the specified call and receives data, this method decreases amount of redundant code 
	 * since the connection is always required.
	 * @param query The call and all parameters required (login?user=example&password=AAA)
	 * @return the result of the call
	 */
	public static String connect(String query){
		String url = "http://proj-309-yt-02.cs.iastate.edu:8080/";
		String charset = "UTF-8";
		
		try{
			HttpURLConnection connection = (HttpURLConnection) new URL(url + query).openConnection();
			connection.setRequestProperty("Accept-Charset", charset);		
			InputStream response = connection.getInputStream();
			
			try (Scanner scanner = new Scanner(response)) {
			    String responseBody = scanner.useDelimiter("\\A").next();
			    return responseBody;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * Does the same thing as connect but will automatically add &token= to the end of the query,
	 *  setting the the value to whatever token was provided
	 * @param query The desired API and required parameters
	 * @param token The token for the session
	 * @return API call results
	 */
	public static String connectAppendToken(String query, String token){
		return connect(query + "&token=" + token);
	}
}
