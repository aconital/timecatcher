package com.cpsc.timecatcher.model;

import java.util.Date;

/**
 * Created by yutongluo on 2/5/16.
 */
public interface ITimeSlot {

    // Mutators
    void setStartTime(Date startTime);
    void setEndTime(Date endTime);

    // Accessors
    Date getStartTime();
    Date getEndTime();
    int getTotalTime();

    boolean isOverlap(ITimeSlot other);
}
