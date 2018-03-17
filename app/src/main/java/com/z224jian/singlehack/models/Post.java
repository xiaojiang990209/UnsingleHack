package com.z224jian.singlehack.models;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

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
    public String date = "";
    public String genderPreference = "";

    public Post() {
        // Default constructor required for calls for DataSnapshot.getValue(User.class)
    }

    public Post(String userId, String location, String courseCode,
                String timeFrom, String timeTo, String date, String genderPreference) {
        this.userId = userId;
        this.location = location;
        this.courseCode = courseCode;
        this.timeFrom = timeFrom;
        this.timeTo = timeTo;
        this.date = date;
        this.genderPreference = genderPreference;
    }

    // [START post_to_map]
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("Course", courseCode);
        result.put("Gender", genderPreference);
        result.put("Location", location);
        result.put("Uid", userId);
        result.put("end", timeTo);
        result.put("start", timeFrom);
        result.put("date", date);

        return result;
    }
    // [END post_to_map]

}
