package com.z224jian.singlehack.Model;

/**
 * Created by z224jian on 17/03/18.
 */

public class Post {

    private static final String TAG = "Post";

    public String userId = "";
    public String location = "";
    public String courseCode = "";
    public String timeFrom = "";
    public String timeTo = "";
    public String genderPreference = "";

    public Post() {
        // Default constructor required for calls for DataSnapshot.getValue(User.class)
    }

    public Post(String userId, String location, String courseCode,
                String timeFrom, String timeTo, String genderPreference) {
        this.userId = userId;
        this.location = location;
        this.courseCode = courseCode;
        this.timeFrom = timeFrom;
        this.timeTo = timeTo;
        this.genderPreference = genderPreference;
    }

}
