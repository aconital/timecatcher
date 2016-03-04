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
	
	Time getDuration(){
		//System.out.println(" call at here Super Task ");
		return new Time (0,0);
	}
	
	void initializeDomainSet(Time dayStart,Time dayEnd, Time step) { 
		//System.out.println(" call at here super Task ");
	}
	void initializeDomainSet(){}
}

class FlexibleTask extends Task{
	private int taskId; 
	private Time duration;// planed working time for this flexible task
	
	FlexibleTask(Time duration){
		domain=new Domain();
		this.duration= duration;
		
		taskId=Task.taskCount;
		Task.increaseTaskCount();
	}
	
	int getTaskId(){
		return taskId;
	}
	
	Time getDuration(){
		//System.out.println(" call at here Flexible Task ");
		return duration;
	}

	void initializeDomainSet(Time dayStart,Time dayEnd, Time step){
		domain.initializeDomainSet(dayStart, dayEnd, duration, step);
		//System.out.println(" call at here flexible ");
	}
}

class FixedTask extends Task{
	private Time startTime;
	private Time endTime;
	private int taskId; 
	
	FixedTask(Time s, Time e){
		if(s.compareTime(e) <0){
			domain=new Domain();
			startTime=s;
			endTime=e;
			taskId=Task.taskCount;
			Task.increaseTaskCount();
		}
	}
	
	Time getDuration(){
		return endTime.substractTime(startTime);
	}
	
	int getTaskId(){
		return taskId;
	}
	void initializeDomainSet(){
		domain.initializeDomainSet(startTime, endTime);
	}
}











