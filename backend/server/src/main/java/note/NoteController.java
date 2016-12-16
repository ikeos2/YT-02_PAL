package note;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import api.Authenticator;

@RestController
public class NoteController {

	@RequestMapping("/getNotes")
	public Note[] getNotes(@RequestParam(value = "user", defaultValue = "null") String user,
			@RequestParam(value = "token", defaultValue = "null") String token) {
		if (Authenticator.authenticate(token)) {
			return NoteMethods.getNotes(user);
		}
		return null;
	}

	@RequestMapping("/getNote")
	public Note getNote(@RequestParam(value = "title", defaultValue = "null") String title,
			@RequestParam(value = "token", defaultValue = "null") String token) {
		if (Authenticator.authenticate(token)) {
			return NoteMethods.getNote(title);
		}
		return null;
	}

	@RequestMapping("/addNote")
	public Boolean addNote(@RequestParam(value = "owner", defaultValue = "null") String owner,
			@RequestParam(value = "data", defaultValue = "null") String data,
			@RequestParam(value = "collab", defaultValue = "null") String collabs,
			@RequestParam(value = "title", defaultValue = "null") String title,
			@RequestParam(value = "token", defaultValue = "null") String token) {
		if (Authenticator.authenticate(token)) {
			return NoteMethods.addNote(data, owner, collabs, title);
		}
		return false;
	}

	@RequestMapping("/updateNote")
	public boolean updateNote(@RequestParam(value = "owner", defaultValue = "null") String owner,
			@RequestParam(value = "data", defaultValue = "null") String data,
			@RequestParam(value = "title", defaultValue = "null") String title,
			@RequestParam(value = "token", defaultValue = "null") String token) {
		if (Authenticator.authenticate(token)) {
			return NoteMethods.updateNote(title, owner, data);
		}
		return false;
	}

	@RequestMapping("/updateTitle")
	public boolean updateTitle(@RequestParam(value = "owner", defaultValue = "null") String owner,
			@RequestParam(value = "title", defaultValue = "null") String title,
			@RequestParam(value = "data", defaultValue = "null") String data,
			@RequestParam(value = "token", defaultValue = "null") String token) {
		if (Authenticator.authenticate(token)) {
			return NoteMethods.updateTitle(owner, title, data);
		}
		return false;
	}

	@RequestMapping("/updateDataTitle")
	public boolean updateDataTitle(@RequestParam(value = "owner", defaultValue = "null") String owner,
			@RequestParam(value = "id", defaultValue = "null") int id,
			@RequestParam(value = "data", defaultValue = "null") String data,
			@RequestParam(value = "title", defaultValue = "null") String title,
			@RequestParam(value = "token", defaultValue = "null") String token) {
		if (Authenticator.authenticate(token)) {
			return NoteMethods.updateDataTitle(owner, id, data, title);
		}
		return false;
	}

	@RequestMapping("/deleteNote")
	public boolean deleteNote(@RequestParam(value = "title", defaultValue = "null") String title,
			@RequestParam(value = "token", defaultValue = "null") String token) {
		if (Authenticator.authenticate(token)) {
			return NoteMethods.deleteNote(title);
		}
		return false;
	}

	@RequestMapping("/shareNote")
	public boolean shareNote(@RequestParam(value = "title", defaultValue = "null") String title,
			@RequestParam(value = "collabs", defaultValue = "null") String collabs,
			@RequestParam(value = "token", defaultValue = "null") String token) {
		if (Authenticator.authenticate(token)) {
			return NoteMethods.shareNote(title, collabs);
		}
		return false;
	}
}
