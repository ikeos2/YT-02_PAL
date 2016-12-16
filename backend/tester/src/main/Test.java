package main;

import java.io.InputStream;
import java.net.*;
import java.util.Scanner;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;


public class Test {

	public static void main(String[] args) {
		//This area is an attempt to run all tests in this package in one go, not sure how to use it though :/
		// https://www.tutorialspoint.com/junit/junit_suite_test.htm
		@RunWith(Suite.class)
		@Suite.SuiteClasses({
			UserTest.class,
			CourseTest.class,
			NoteTest.class
		}) class Suite_bar{
			
		}
		
	}
	
	public static void test(){
		String url = "http://proj-309-yt-02.cs.iastate.edu:8080/greeting";
		String charset = "UTF-8";  // Or in Java 7 and later, use the constant: java.nio.charset.StandardCharsets.UTF_8.name()
		String param1 = "value1";
		String param2 = "value2";
		// ...
		
		try{

			String query = String.format("param1=%s&param2=%s", 
					URLEncoder.encode(param1, charset), 
					URLEncoder.encode(param2, charset));
			
			
			HttpURLConnection connection = (HttpURLConnection) new URL(url + "?" + query).openConnection();
			connection.setRequestProperty("Accept-Charset", charset);		
			InputStream response = connection.getInputStream();
			
			try (Scanner scanner = new Scanner(response)) {
			    String responseBody = scanner.useDelimiter("\\A").next();
			    System.out.println(responseBody);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
