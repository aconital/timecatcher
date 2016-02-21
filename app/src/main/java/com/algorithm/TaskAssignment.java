package com.algorithm;

public class TaskAssignment {
	private TimeSlice assignment;// specific assigned TimeSlice for current task 
	private int taskIdentifier;// identifier of each task
	
	TaskAssignment(){}
	
	TaskAssignment(TimeSlice assign,int id){
		assignment=assign;
		taskIdentifier=id;
	}
	
	void setAssignment(TimeSlice assign){
		assignment=assign;
	}
	
	void setTaskIdentifier(int id){
		taskIdentifier=id;
	}
	
	TimeSlice getAssignment(){
		return assignment;
	}
	
	int getTaskIdentifier(){
		return taskIdentifier;
	}
	
}
