package com.algorithm;

import java.util.*;

/*
 * Class Task is used to describe a task and associated possible assigned time slice 
 * 
 */
public class Task {
	public static int taskCount=0;// the number of task 
	protected Domain domain;//a list of possible time slice 
	
	Task(){
		domain=new Domain();
	}
	
	protected static void increaseTaskCount(){
		taskCount++;
	}
	
	Set<TimeSlice> getDomainSet(){
		return domain.getDomainSet();
	}
	int getTaskId(){return -1;}
	
	void initializeDomainSet(Time step) { 
		//System.out.println(" call at here super Task ");
	}
	void initializeDomainSet(){}
}

class FlexibleTask extends Task{
	private Time estimatedTime;// estimated amount of time to finish a task
	private Time dayStart;
	private Time dayEnd;
	private int taskId; 
	
	FlexibleTask(Time t,Time dayStart, Time dayEnd){
		domain=new Domain();
		estimatedTime=t;
		this.dayStart=dayStart;
		this.dayEnd=dayEnd;
		taskId=Task.taskCount;
		Task.increaseTaskCount();
	}
	
	int getTaskId(){
		return taskId;
	}

	void initializeDomainSet(Time step){
		domain.initializeDomainSet(dayStart, dayEnd, estimatedTime, step);
		//System.out.println(" call at here flexible ");
	}
}

class FixedTask extends Task{
	private Time startTime;
	private Time endTime; 
	private int taskId; 
	
	FixedTask(Time s, Time e){
		domain=new Domain();
		startTime=s;
		endTime=e;
		taskId=Task.taskCount;
		Task.increaseTaskCount();
	}
	
	int getTaskId(){
		return taskId;
	}
	void initializeDomainSet(){
		domain.initializeDomainSet(startTime, endTime);
	}
}











