package user;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import api.Authenticator;

@RestController
@ControllerAdvice("api.Application")
public class UserController {

	@RequestMapping("/user")
	public User user(@RequestParam(value = "name", defaultValue = "World") String name,
			@RequestParam(value = "token", defaultValue = "null") String token) {
		if (Authenticator.authenticate(token, 3)) {
			return UserMethods.getUser(name);
		}
		return null;
	}

	@RequestMapping("/login")
	public String slogin(@RequestParam(value = "name", defaultValue = "null") String name,
			@RequestParam(value = "password", defaultValue = "null") String password,
			@RequestParam(value = "seed", defaultValue = "0") int seed) {
		return UserMethods.login(name, password, seed);
	}

	@RequestMapping("/logout")
	public boolean logout(@RequestParam(value = "token", defaultValue = "null") String token) {
		return UserMethods.logout(token);
	}

	@RequestMapping("/createUser")
	public String create(@RequestParam(value = "name", defaultValue = "null") String name,
			@RequestParam(value = "password", defaultValue = "null") String password,
			@RequestParam(value = "email", defaultValue = "null") String email,
			@RequestParam(value = "type", defaultValue = "Student") String type) {
		return UserMethods.insertUser(name, password, email, type);
	}

	@RequestMapping("/deleteUser")
	public String delete(@RequestParam(value = "name") String name) {
		return UserMethods.deleteUser(name);
	}

	@RequestMapping("/refreshToken")
	public String refreshToken(@RequestParam(value = "token", defaultValue = "0") String token,
			@RequestParam(value = "username", defaultValue = "0") String username) {
		return UserMethods.refreshToken(token, username);
	}

	@RequestMapping("/userExists")
	public String userExists(@RequestParam(value = "name", defaultValue = "null") String name) {
		return UserMethods.userExists(name);
	}

	@RequestMapping("/forceLogout")
	public void forceLogout(@RequestParam(value = "token", defaultValue = "0") String token){
		if (Authenticator.authenticate(token, 3)) {
			UserMethods.forceLogout();
		}
	}

	@RequestMapping("/changePassword")
	public String changePassword(@RequestParam(value = "username", defaultValue = "null") String username,
			@RequestParam(value = "password", defaultValue = "null") String password,
			@RequestParam(value = "token", defaultValue = "null") String token) {
		return UserMethods.changePassword(username, password, token, false);
	}
	
	@RequestMapping("/changeUsername")
	public String changeUsername(@RequestParam(value = "username", defaultValue = "null") String username,
			@RequestParam(value = "token", defaultValue = "null") String token) {
		return UserMethods.changeUsername(username, token);
	}
	
	@RequestMapping("/checkSecurityQuestion")
	public String checkSecurityQuestion(@RequestParam(value = "username", defaultValue = "null") String username,
			@RequestParam(value = "answer", defaultValue = "null") String answer) {
		return UserMethods.checkSecurityAnswer(answer, username);
	}
}
