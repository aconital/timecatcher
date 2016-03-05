package com.algorithm;

public class TaskAssignment {
	private TimeSlice assignment;// specific assigned TimeSlice for current task 
	private int taskId;// identifier of each task
	
	TaskAssignment(TimeSlice assign,int id){
		assignment=assign;
		taskId=id;
	}
	
	void setAssignment(TimeSlice assign){
		assignment=assign;
	}
	
	void setTaskIdentifier(int id){
		taskId=id;
	}
	
	TimeSlice getAssignment(){
		return assignment;
	}
	
	int getTaskIdentifier(){
		return taskId;
	}
	
}
