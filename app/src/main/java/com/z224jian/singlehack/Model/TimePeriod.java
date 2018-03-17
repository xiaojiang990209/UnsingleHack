package com.z224jian.singlehack.Model;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lichi on 2018/3/16.
 */

public class TimePeriod {
    private static SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm");
    private Date startFrom;
    private Date endAt;

    public TimePeriod(String start, String end) {
        try{
            this.startFrom = formatter.parse(start);
            this.endAt = formatter.parse(end);
        }catch(ParseException e){
            Log.e("error", e.getMessage());
        }
    }

    public static SimpleDateFormat getFormatter() {
        return formatter;
    }

    public static void setFormatter(SimpleDateFormat formatter) {
        TimePeriod.formatter = formatter;
    }

    public String getStartFrom() {
        return formatter.format(this.startFrom);
    }

    public void setStartFrom(String startFrom){
        try{
            this.startFrom = formatter.parse(startFrom);
        }catch(ParseException e){
            Log.e("error", e.getMessage());
        }
    }

    public String getEndAt() {
        return formatter.format(this.endAt);
    }

    public void setEndAt(String endAt) {
        try{
            this.endAt = formatter.parse(endAt);
        }catch(ParseException e){
            Log.e("error", e.getMessage());
        }
    }

    public Map<String, String> toMap(){
        Map<String, String> map = new HashMap<>();
        map.put("Start", this.getStartFrom());
        map.put("End", this.getEndAt());
        return map;
    }
}
