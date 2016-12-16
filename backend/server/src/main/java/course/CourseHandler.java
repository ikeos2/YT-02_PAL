package course;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import api.Authenticator;

@RestController
public class CourseHandler {

	@RequestMapping("/addCourse")
	public Boolean addCourse(@RequestParam(value = "instructor", defaultValue = "null") String instructor,
			@RequestParam(value = "owner", defaultValue = "null") String owner,
			@RequestParam(value = "name", defaultValue = "null") String name,
			@RequestParam(value = "location", defaultValue = "null") String location,
			@RequestParam(value = "startTime", defaultValue = "null") String startTime,
			@RequestParam(value = "endTime", defaultValue = "null") String endTime,
			@RequestParam(value = "room", defaultValue = "null") String room,
			@RequestParam(value = "days", defaultValue = "null") String days,
			@RequestParam(value = "token", defaultValue = "null") String token) {
		if (Authenticator.authenticate(token)) {
			return CourseMethods.addCourse(owner, name, instructor, location, startTime, endTime, room, days);
		}
		return false;
	}

	@RequestMapping("/deleteCourse")
	public Boolean deleteCourse(@RequestParam(value = "id", defaultValue = "null") int id,
			@RequestParam(value = "token", defaultValue = "null") String token) {
		if (Authenticator.authenticate(token)) {
			return CourseMethods.deleteCourse(id);
		}
		return false;
	}

	@RequestMapping("/getCourse")
	public Course getCourse(@RequestParam(value = "name", defaultValue = "null") String name,
			@RequestParam(value = "token", defaultValue = "null") String token) {
		if (Authenticator.authenticate(token)) {
			return CourseMethods.getCourse(name);
		}
		return null;
	}

	@RequestMapping("/getCourses")
	public Course[] getCourses(@RequestParam(value = "owner") String owner,
			@RequestParam(value = "token", defaultValue = "null") String token) {
		if (Authenticator.authenticate(token)) {
			return CourseMethods.getCourses(owner);
		}
		return null;
	}

	@RequestMapping("/updateCourse")
	public Boolean updateCourse(@RequestParam(value = "start", defaultValue = "null") String startTime,
			@RequestParam(value = "end", defaultValue = "null") String endTime,
			@RequestParam(value = "instructor", defaultValue = "null") String instructor,
			@RequestParam(value = "location", defaultValue = "null") String location,
			@RequestParam(value = "id", defaultValue = "null") int id,
			@RequestParam(value = "room", defaultValue = "null") String room,
			@RequestParam(value = "days", defaultValue = "null") String days,
			@RequestParam(value = "token", defaultValue = "null") String token) {
		if (Authenticator.authenticate(token)) {
			return CourseMethods.updateCourse(startTime, endTime, instructor, location, id, room, days);
		}
		return false;
	}

	@RequestMapping("/courseSummary")
	public String courseSummary(@RequestParam(value = "course_uid", defaultValue = "0") int courseID,
			@RequestParam(value = "token", defaultValue = "null") String token) {
		if (Authenticator.authenticate(token)) {
			return CourseMethods.courseSummary(courseID);
		}
		return null;
	}

	@RequestMapping("/courseLocation")
	public String courseLocation(@RequestParam(value = "id", defaultValue = "null") int id,
			@RequestParam(value = "token", defaultValue = "null") String token) {
		if (Authenticator.authenticate(token)) {
			return CourseMethods.courseLocation(id);
		}
		return null;
	}

	@RequestMapping("/insertFile")
	public String insertFile(@RequestParam(value = "document_name", defaultValue = "null") String doc,
			@RequestParam(value = "path", defaultValue = "null") String path,
			@RequestParam(value ="course_owner", defaultValue = "null") String owner,
			@RequestParam(value = "course_name", defaultValue = "null") String name,
			@RequestParam(value = "token", defaultValue = "null") String token) {
		if(Authenticator.authenticate(token)){
			return CourseMethods.insertFile(doc, path, owner, name);
		}
		return null;
	}
	
}
