package main;

import static org.junit.Assert.*;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class NoteTest {

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
	public void getNoteTest() {
		String url = "getNote?title=note2&token=" +token;
		String result = Util.connect(url);
		String answer = "{\"owner\":\"yetian\",\"collab\":\"null\",\"data\":\"stuffs\",\"title\":\"Note2\",\"uid\":75}";
		if(result.equals(answer)){return;}
		fail("Result didn't match: " + result);
	}
	
	@Test
	public void getNotesTest() {
		String url = "getNotes?user=yetian";
		String result = Util.connectAppendToken(url, token);
		String answer = "[{\"owner\":\"yetian\",\"collab\":\"null\",\"data\":\"stuffs\",\"title\":\"Note2\",\"uid\":75}]";
		if(result.equals(answer)){
			return;
		}
		fail("Result didn't match: " + result);
	}
	
	@Test
	public void addNoteTest() {
		String noteTitle = Integer.toString((int) System.currentTimeMillis());
		String data = "test";
		String note = "addNote?owner=ReTester&title=" + noteTitle + "&data="+data+"&collab=none";//owner(string)	data	collab(String of username, seperated by commas)	title	token
		String result = Util.connectAppendToken(note, token);
		if(result.equals("true") == false) fail("Note was not added: \"" + result+"\"");
		//Now we should have a new note in the system, check if it's there
		
		String query = "getNote?title=" + noteTitle;
		JSONObject results = new JSONObject(Util.connectAppendToken(query, token));
		//Now we should have the result
		
		if(!results.getString("owner").equalsIgnoreCase("ReTester")) fail("Owner not ReTester:  \"" +results.getString("owner")+"\"");
		if(!results.getString("title").equalsIgnoreCase(noteTitle)) fail("Title not correct:  \"" +results.getString("title")+"\"");
		if(!results.getString("data").equalsIgnoreCase(data)) fail("Data not correct: \"" +results.getString("data")+"\"");
		if(!results.getString("collab").equalsIgnoreCase("none")) fail("Collabs not correct: \"" +results.getString("collab")+"\"");
	}
	
	@Test
	public void updateNoteTest() {
		String title = "-1124951221";
		String data = "helloooooooooooooo";
		String note = "updateNote?title="+title+"&owner=ReTester&data="+data+"";
		String result = Util.connectAppendToken(note, token);
		if(result.equals("true") == false) fail("Data not changed");
		//Data should be changed
		
		String query = "getNote?title="+title+"";
		JSONObject results = new JSONObject(Util.connectAppendToken(query, token));
		
		if(!results.getString("owner").equalsIgnoreCase("ReTester")) fail("Owner not ReTester:  \"" +results.getString("owner")+"\"");
		if(!results.getString("title").equalsIgnoreCase(title)) fail("Title not correct:  \"" +results.getString("title")+"\"");
		if(!results.getString("data").equalsIgnoreCase(data)) fail("Data not correct: \"" +results.getString("data")+"\"");
	}
	
	@Test
	public void updateTitleTest() {
		String title = "TitleTest0";
		String data = "values!";
		String note = "updateTitle?owner=ReTester&title=" + title + "&data="+data+"";
		String result = Util.connectAppendToken(note, token);
		if(result.equals("true") == false) fail("Title not changed: \"" + result+"\"");
		//Title should be changed
		
		String query = "getNote?title=" + title+"";
		JSONObject results = new JSONObject(Util.connectAppendToken(query, token));
		
		if(!results.getString("owner").equalsIgnoreCase("ReTester")) fail("Owner not ReTester:  \"" +results.getString("owner")+"\"");
		if(!results.getString("title").equalsIgnoreCase(title)) fail("Title not correct:  \"" +results.getString("title")+"\"");
		if(!results.getString("data").equalsIgnoreCase(data)) fail("Data not correct: \"" +results.getString("data")+"\"");
	}
	
	

}
