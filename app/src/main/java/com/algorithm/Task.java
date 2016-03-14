package com.algorithm;

import java.util.*;

/*
 * Class Task is used to describe a task and associated possible assigned time slice 
 * 
 */
public class Task {
	static int taskCount=0;// the number of task including both flexible and fixed task 
	protected Domain domain;//a list of possible time slice 
	
	Task(){
		domain=new Domain();
	}
	
	static void setTaskCount(int cnt){
		taskCount=cnt;
	}
	
	protected static void increaseTaskCount(){
		taskCount++;
	}
	
	Set<TimeSlice> getDomainSet(){
		return domain.getDomainSet();
	}
	
	ArrayList<TimeSlice> getDomainArrayList(){
		return domain.getDomainArrayList();
	}
	
	int getTaskId(){return -1;}// overridden by subclass 
	
	Time getDuration(){
		return new Time (0,0);
	}
	
	void initializeDomainSet(Time dayStart,Time dayEnd, Time step) {}// overridden by subclass
	//void initializeDomainSet(){}// overridden by subclass
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
		return duration;
	}

	void initializeDomainSet(Time dayStart,Time dayEnd, Time step){
		domain.initializeDomainSet(dayStart, dayEnd, duration, step);
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
			domain.initializeDomainSet(startTime, endTime);
		}
	}
	
	Time getDuration(){
		return endTime.substractTime(startTime);
	}
	int getTaskId(){
		return taskId;
	}
	/*
	void initializeDomainSet(){
		domain.initializeDomainSet(startTime, endTime);
	}
	*/
}











