package com.z224jian.singlehack.models;


import java.util.HashMap;
import java.util.Map;

public class Post {
    private String location;
    private String userID;
    private String courseCode;
    private TimePeriod timePeriod;
    private String gender;

    public Post(String userID, String location, String courseCode, String gender, String start,
                String end){
        this.courseCode = courseCode;
        this.location = location;
        this.userID = userID;
        this.timePeriod = new TimePeriod(start, end);
        this.gender = gender;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public TimePeriod getTimePeriod() {
        return this.timePeriod;
    }

    public void setTimePeriod(String start, String end) {
        this.timePeriod.setStartFrom(start);
        this.timePeriod.setEndAt(end);
    }

    public Map<String, String> toMap(){
        Map<String, String> map = new HashMap<>();
        map.put("Location", this.location);
        map.put("Course", this.courseCode);
        map.put("Gender", this.gender);
        map.put("Uid", this.userID);
        map.put("start", this.timePeriod.getStartFrom());
        map.put("end", this.timePeriod.getEndAt());
        return map;
    }
}
