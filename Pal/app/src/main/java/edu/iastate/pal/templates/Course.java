package edu.iastate.pal.templates;

import java.io.Serializable;

/**
 * Class holder for the Course Objects
 */
public class Course implements Serializable {

    private String courseName;
    private String professor;
    private String startTime;
    private String endTime;
    private String buildingName;
    private String roomNumber;
    private String meetingDays;

    private String uID;



    public Course(String course, String professor, String building, String roomNumber, String meetingDays, String startTime, String endTime){
        courseName = course;
        this.professor = professor;
        buildingName = building;
        this.roomNumber = roomNumber;
        this.startTime = startTime;
        this.endTime = endTime;
        this.meetingDays = meetingDays;
    }

    //Getters
    public String getCourseName(){ return courseName; }
    public String getProfessorName() { return professor; }
    public String getBuildingName() { return buildingName; }
    public String getRoomNumber(){ return roomNumber; }
    public String getStartTime() { return startTime; }
    public String getEndTime() { return endTime; }
    public String getMeetingDays() { return meetingDays; }

    //Setters
    public void setCourseName(String newCourseName){ courseName = newCourseName; }
    public void setProfessorName(String newProfessorName){ professor = newProfessorName; }
    public void setBuildingName(String newBuildingName){ buildingName = newBuildingName; }
    public void setRoomNumber(String newRoomNumber){ roomNumber = newRoomNumber; }
    public void setStartTime(String newStartTime){ startTime = newStartTime; }
    public void setEndTime(String newEndTime){ endTime = newEndTime; }
    public void setMeetingDays(String newMeetingTime){ meetingDays = newMeetingTime; }


    public String getuID() {
        return uID;
    }

    public void setuID(String uID) {
        this.uID = uID;
    }
}
