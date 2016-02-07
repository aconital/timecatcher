package com.cpsc.timecatcher.model;

import java.util.Date;

/**
 * Created by yutongluo on 2/5/16.
 */
public interface ITimeSlot {

    // Mutators
    void setTotalTime(int minutes);

    // Accessors
    Date getStartTime();
    Date getEndTime();
    int getTotalTime();

    boolean isOverlap(ITimeSlot other);
}
