package main;

import static org.junit.Assert.*;

import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CourseTest {

	String token;

	@Before
	public void setUp() throws Exception {
		token = Util.login();
	}

	@After
	public void tearDown() throws Exception {
		Util.logout(token);
	}
	
	@Test 
	public void getCourseTest(){
		String url = "getCourse?name=CS 309&token=" +token;
		String result = Util.connect(url);
		String answer = "{\"docs\":\"null\",\"instructor\":\"Mitra\",\"location\":\"MacKay\",\"courseName\":\"CS 309\",\"owner\":\"yetian\",\"days\":\"Monday, Wednesday, Friday\",\"uid\":25,\"endTime\":\"5:00 PM\",\"startTime\":\"4:10 PM\",\"room\":0117}";
		if(result.equals(answer)){
			return;
		}
		fail("Result didn't match" + result);
	}
	
	@Test
	public void getCoursesTest() {
		String url = "getCourses?owner=yetian";
		String result = Util.connectAppendToken(url, token);
		String answer = "[{\"docs\":\"null\",\"instructor\":\"Mitra\",\"location\":\"MacKay\",\"courseName\":\"CS 309\",\"owner\":\"yetian\",\"days\":\"Monday, Wednesday, Friday\",\"uid\":25,\"endTime\":\"5:00 PM\",\"startTime\":\"4:10 PM\",\"room\":0117}]";
		if (result.equals(answer)) {
			return;
		}
		fail("Result didn't match: " + result);
	}

	@Test
	public void addCourseTest(){
		String name = Integer.toString((int) System.currentTimeMillis());
		String add = "addCourse?instructor=Mitra&owner=ReTester&name="+name+"&location=MacKay Hall&startTime=4 PM&endTime=5 PM&room=0117&days=Monday, Wednesday, Friday";
		String result = Util.connectAppendToken(add, token);
		if(result.equals("true") == false) fail("Course was not added: \"" + result+"\"");
		
		String query = "getCourse?name=ReTester";
		JSONObject results = new JSONObject(Util.connectAppendToken(query, token));
		//Now we should have the result
		
		
		if(!results.getString("instructor").equalsIgnoreCase("Mitra")) fail("Instructor not Mitra:  \"" +results.getString("instructor")+"\"");
		if(!results.getString("owner").equalsIgnoreCase("ReTester")) fail("Owner not ReTester:  \"" +results.getString("owner")+"\"");
		if(!results.getString("name").equalsIgnoreCase(name)) fail("Name not correct:  \"" +results.getString("name")+"\"");
		if(!results.getString("location").equalsIgnoreCase("MacKay Hall")) fail("Location not correct: \"" +results.getString("location")+"\"");
		if(!results.getString("startTime").equalsIgnoreCase("4 PM")) fail("Start time not correct: \"" +results.getString("startTime")+"\"");
		if(!results.getString("endTime").equalsIgnoreCase("5 PM")) fail("End time not correct: \"" +results.getString("endTime")+"\"");
		if(!results.getString("room").equalsIgnoreCase("0117")) fail("Room not correct: \"" +results.getString("room")+"\"");
		if(!results.getString("days").equalsIgnoreCase("Monday, Wednesday, Friday")) fail("Days not correct: \"" +results.getString("days")+"\"");
	}
	
	
}
