package com.cpsc.timecatcher.model;

import android.util.Log;

import com.parse.FindCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by yutongluo on 2/7/16.
 */
@ParseClassName("Day")
public class Day extends ParseObject {

    public static final String TAG = "Day";

    public void setDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        put("date", calendar.getTime());
    }

    public Date getDate() {
        return getDate("date");
    }

    public void setDayStart(Date date) {
        put("dayStart", date);
    }

    public Date getDayStart() {
        return getDate("dayStart");
    }

    public void setDayEnd(Date date) {
        put("dayEnd", date);
    }

    public Date getDayEnd() {
        return getDate("dayEnd");
    }

    public ParseUser getUser() {
        return getParseUser("user");
    }

    public void setUser(ParseUser value) {
        if (value != null)
            put("user", value);
    }

    public List<Task> getUserTasksOfDay() throws ParseException {
        ParseUser currentUser = ParseUser.getCurrentUser();
        if(currentUser == null) {
            Log.e(TAG, "Can't get tasks of day: User not found!");
            return null;
        }
        else {
            return taskOfDayQuery(currentUser).find();
        }
    }

    public void getUserTaskOfDayInBackground(FindCallback<Task> cb) {
        ParseUser currentUser = ParseUser.getCurrentUser();
        if(currentUser == null) {
            Log.e(TAG, "Can't get tasks of day: User not found!");
        } else {
            taskOfDayQuery(currentUser).findInBackground(cb);
        }
    }

    private ParseQuery<Task> taskOfDayQuery(ParseUser currentUser) {
        ParseQuery<Task> query = Task.getQuery();
        query.whereEqualTo("day", this);
        query.whereEqualTo("user", currentUser);
        return query;
    }

    public static ParseQuery<Day> getQuery() {
        return ParseQuery.getQuery(Day.class);
    }


}
