package main;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class UserTest {
	
	String token;
	
	@Before
	public void setUp() throws Exception {
		token = Util.login();
		System.out.println(token);
	}

	@After
	public void tearDown() throws Exception {
		Util.logout(token);
	}

	@Test
	public void LoginTest() {
		if(token == null) fail("Token not null");
		if(token.isEmpty()) fail("Token is empty.");
		if(token.length() < 50) fail("Token is too short: \"" + token +"\"");
	}
	
	@Test
	public void UtilConnectTest(){
		String r = Util.connect("/greeting");
		if(r.length()<5) fail("greeting result too short!");
	}


}
