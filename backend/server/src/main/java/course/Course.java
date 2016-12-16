package course;


public class Course {
	String docs;
	String start;
	String end;
	String instructor;
	String location;
	String courseName;
	String owner;
	String roomNumber;
	String days;
	int uid;
	
	public Course(String name, String doc, String startTime, String endTime, String instr, String loc, int id, String gOwner, String roomNum, String day){
		roomNumber = roomNum;
		days = day;
		courseName = name;
		docs = doc;
		start = startTime;
		end = endTime;
		instructor = instr;
		location = loc;
		uid = id;
		owner = gOwner;
	}
	public String getDays(){
		return days;
	}
	
	public String getRoom(){
		return roomNumber;
	}
	public String getOwner(){
		return owner;
	}
	
	public String getCourseName(){
		return courseName;
	}
	
	public String getDocs(){
		return docs;
	}
	
	public String getStartTime(){
		return start;
	}
	
	public String getEndTime(){
		return end;
	}
	
	public String getInstructor(){
		return instructor;
	}
	
	public String getLocation(){
		return location;
	}
	
	public int getUID(){
		return uid;
	}
}
