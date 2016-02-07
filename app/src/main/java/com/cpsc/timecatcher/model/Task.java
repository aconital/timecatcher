package com.cpsc.timecatcher.model;

import android.util.Log;

import com.cpsc.timecatcher.algorithm.TimeUtils;
import com.parse.FindCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.Date;
import java.util.List;

/**
 * Created by yutongluo on 2/5/16.
 */
@ParseClassName("Task")
public class Task extends ParseObject implements ITask, ITimeSlot {

    public static final String TAG = "Task";

    @Override
    public String getTitle() {
        return getString("title");
    }

    @Override
    public void setTitle(String title) {
        put("title", title);
    }

    @Override
    public String getDescription() {
        return getString("description");
    }

    @Override
    public void setDescription(String description) {
        put("description", description);
    }

    @Override
    public Date getStartTime() {
        return getDate("startTime");
    }

    public void setStartTime(Date startTime) {
        put("startTime", startTime);
    }

    @Override
    public Date getEndTime() {
        return getDate("endTime");
    }

    public void setEndTime(Date endTime) {
        put("endTime", endTime);
    }

    @Override
    public void addCategory(final Category category) {
        final ParseObject that = this;
        category.saveInBackground(new SaveCallback() {
            public void done(ParseException e) {
                if (e != null) {
                    Log.d(TAG, e.getMessage());
                } else {
                    ParseRelation relation = that.getRelation("categories");
                    relation.add(category);
                    that.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e != null) {
                                Log.d(TAG, e.getMessage());
                            }
                        }
                    });
                }
            }
        });
    }

    public void addCategory(String categoryId) {
        ParseObject category = ParseObject.createWithoutData("Category", categoryId);
        ParseRelation relation = this.getRelation("categories");
        relation.add(category);
        this.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.d(TAG, e.getMessage());
                }
            }
        });
    }

    @Override
    public void removeCategory(Category category) {
        ParseRelation relation = this.getRelation("categories");
        relation.remove(category);
        this.saveInBackground();
    }

    @Override
    public ParseQuery<ParseObject> getCategories() {
        ParseRelation relation = this.getRelation("categories");
        return relation.getQuery();
    }

    @Override
    public void setTotalTime(int minutes) {
        put("totalTime", minutes);
    }

    @Override
    public int getTotalTime() {
        return getInt("totalTime");
    }

    public ParseUser getUser() {
        return getParseUser("user");
    }

    public void setUser(ParseUser value) {
        put("user", value);
    }

    @Override
    public boolean isOverlap(ITimeSlot other) {
        return TimeUtils.isOverlap(this.getStartTime(),
                this.getEndTime(), other.getStartTime(), other.getEndTime());
    }

    public static ParseQuery<Task> getQuery() {
        return ParseQuery.getQuery(Task.class);
    }
}
