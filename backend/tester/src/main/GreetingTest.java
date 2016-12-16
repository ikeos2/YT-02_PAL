package main;

import static org.junit.Assert.*;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import org.json.*;

import org.junit.Before;
import org.junit.Test;

public class GreetingTest {
	String url = "http://proj-309-yt-02.cs.iastate.edu:8080/greeting";
	String charset = "UTF-8";  // Or in Java 7 and later, use the constant: java.nio.charset.StandardCharsets.UTF_8.name()
	
	HttpURLConnection connection;
	InputStream response;
	String token;
	
	@Before
	public void setUp() throws Exception {
		connection = (HttpURLConnection) new URL(url + "?").openConnection();
		connection.setRequestProperty("Accept-Charset", charset);		
		response = connection.getInputStream();		
	}

	@Test
	public void test() {
		try (Scanner scanner = new Scanner(response)) {
		    String responseBody = scanner.useDelimiter("\\A").next();
		    JSONObject obj = new JSONObject(responseBody);
		    String val = obj.getString("content");
		    System.out.println(responseBody);
		    if(!val.equals("Hello, World!")){
		    	fail("Greeting test failed!");
		    }
		} catch (Exception e){
			fail("Exception in greeting.");
			e.printStackTrace(System.out);
		}
		return;
	}

}
