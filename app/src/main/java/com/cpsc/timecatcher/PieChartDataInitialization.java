package com.cpsc.timecatcher;

import android.util.Log;

import com.cpsc.timecatcher.model.Day;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.Date;
import java.util.List;

/**
 * Created by fujiaoyang1 on 3/18/16.
 */
public class PieChartDataInitialization {
    private String[] xData = { "School", "Work", "Housework","Family","Gym"};
    private float[] yData = { 0,0,0,0,0};
    private Date startDate;
    private Date endDate;

    PieChartDataInitialization(Date start,Date end) {
        startDate=start;
        endDate=end;
    }

    public void setDateRange(Date start,Date end) {
        startDate=start;
        endDate=end;
    }

    public float[] getYData() {
        long timeSpentOnCategory[]= {0,0,0,0,0};
        long total=0;

        ParseQuery<Day> dayParseQuery = com.cpsc.timecatcher.model.Day.getQuery();
        dayParseQuery.whereEqualTo("user", ParseUser.getCurrentUser());

        if(startDate.compareTo(endDate)==0) {
            dayParseQuery.whereEqualTo("date", startDate);
        }
        else {
            dayParseQuery.whereGreaterThanOrEqualTo("date", startDate);
            dayParseQuery.whereLessThanOrEqualTo("date", endDate);
        }
        try {
            List<Day> days = dayParseQuery.find();
            for (Day day : days) {
                timeSpentOnCategory[0]+=day.getTimeSpentOn(xData[0]);
                timeSpentOnCategory[1]+=day.getTimeSpentOn(xData[1]);
                timeSpentOnCategory[2]+=day.getTimeSpentOn(xData[2]);
                timeSpentOnCategory[3]+=day.getTimeSpentOn(xData[3]);
                timeSpentOnCategory[4]+=day.getTimeSpentOn(xData[4]);
            }

            for(int i=0;i<5;i++) {
                total+=timeSpentOnCategory[i];
            }

            if(total!=0){
                for(int i=0;i<5;i++) {
                    yData[i]=100* ((float)timeSpentOnCategory[i]/(float)total);
                }
            }
        }catch(Exception e) {
            Log.e("Total", "Exception thrown  :" + e);
        }
        return yData;
    }
}
