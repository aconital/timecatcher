package com.algorithm;
import java.util.Comparator;

public class TaskAssignment implements Comparable<TaskAssignment>{
	private int taskId;// identifier of each task
	private TimeSlice assignment;// specific assigned TimeSlice for current task 

	TaskAssignment(int id,TimeSlice slice){
		assignment=slice;
		taskId=id;
	}
	
	void setAssignment(TimeSlice slice){
		assignment=slice;
	}
	
	void setTaskId(int id){
		taskId=id;
	}
	
	public TimeSlice getAssignment(){
		return assignment;
	}
	
	public int getTaskId(){
		return taskId;
	}
	
    public int compareTo (TaskAssignment t1) {
    	TimeSlice slice =((TaskAssignment)t1).getAssignment();
    	Time start,start1;
    	start=assignment.getStartTime();
    	start1=slice.getStartTime();
        /* For Ascending order*/
        return  start.compareTime(start1);
    }
	
}
