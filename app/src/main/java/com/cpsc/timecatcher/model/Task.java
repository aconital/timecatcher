package com.cpsc.timecatcher.model;

import android.util.Log;

import com.cpsc.timecatcher.algorithm.TimeUtils;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.Date;

/**
 * Created by yutongluo on 2/5/16.
 */
@ParseClassName("Task")
public class Task extends ParseObject implements ITimeSlot {

    public static final String TAG = "Task";

    public Boolean getFixed() {
        return getBoolean("fixed");
    }

    public void setFixed(Boolean fixed) {
        put("fixed", fixed);
    }

    public String getTitle() {
        return getString("title");
    }

    public void setTitle(String title) {
        put("title", title);
    }

    public String getDescription() {
        return getString("description");
    }

    public void setDescription(String description) {
        put("description", description);
    }

    @Override
    public Date getStartTime() {
        return getDate("startTime");
    }

    @Override
    public void setStartTime(Date startTime) {
        put("startTime", startTime);
    }

    @Override
    public Date getEndTime() {
        return getDate("endTime");
    }

    @Override
    public void setEndTime(Date endTime) {
        put("endTime", endTime);
    }

    public void setDay(Day day) {
        put("day", day);
    }

    public void setDay(String dayId) {
        ParseObject day = ParseObject.createWithoutData("Day", dayId);
        put("day", day);
    }

    public Day getDay() {
        return (Day) getParseObject("day");
    }

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

    public void removeCategory(Category category) {
        ParseRelation relation = this.getRelation("categories");
        relation.remove(category);
        this.saveInBackground();
    }

    public ParseQuery<ParseObject> getCategories() {
        ParseRelation relation = this.getRelation("categories");
        return relation.getQuery();
    }

    public void addConstraint(final Constraint constraint) {
        final ParseObject that = this;
        constraint.saveInBackground(new SaveCallback() {
            public void done(ParseException e) {
                if (e != null) {
                    Log.d(TAG, e.getMessage());
                } else {
                    ParseRelation relation = that.getRelation("constraints");
                    relation.add(constraint);
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

    public void addConstraint(String constraintId) {
        ParseObject constraint = ParseObject.createWithoutData("Constraint", constraintId);
        ParseRelation relation = this.getRelation("constraints");
        relation.add(constraint);
        this.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.d(TAG, e.getMessage());
                }
            }
        });
    }

    public void removeConstraint(Constraint constraint) {
        ParseRelation relation = this.getRelation("constraints");
        relation.remove(constraint);
        this.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.d(TAG, e.getMessage());
                }
            }
        });
    }

    public ParseQuery<ParseObject> getConstraints() {
        ParseRelation relation = this.getRelation("constraints");
        return relation.getQuery();
    }

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
        if (value != null)
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
