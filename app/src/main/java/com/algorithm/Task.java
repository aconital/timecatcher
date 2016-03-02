package com.algorithm;

import java.util.*;

/*
 * Class Task is used to describe a task and associated possible assigned time slice 
 * 
 */
public class Task {
	private static int taskIdentifier=0;// identifier of each task
	protected List<Domain> domain;//a list of possible time slice 
	
	protected static void increaseTaskIdentifier(){
		taskIdentifier++;
	}
	
	int getTaskIdentifier(){
		return taskIdentifier;
	}
	
	List<Domain> getDomain(){
		return domain;
	}
	
	void addTimeSlice(Time start,Time end){
		TimeSlice time= new TimeSlice(start,end);
		domain.add(new Domain(time,true));	
	}
	protected Time addTime(Time t1,Time t2){
		Time time=new Time(0,0);
		int minutes=t1.getMinute()+t2.getMinute();
		time.setMinute(minutes%60);
		time.setHour(t1.getHour() + t2.getHour() + (minutes/60));
		return time;
	}
	
	void initializeDomain(Time dayStart, Time dayEnd,int step){}
}

class FlexibleTask extends Task{
	private Time estimatedTime;// estimated amount of time to finish a task
	
	FlexibleTask(Time t){
		estimatedTime=t;
		Task.increaseTaskIdentifier();
	}
	
	void initializeDomain(Time dayStart, Time dayEnd,Time step){
		dayStart= addTime(dayStart,step);
		Time start,end;
		start=dayStart;
		end=addTime(start,estimatedTime);
		
		while(start.compareTime(end) <0){
			addTimeSlice(start,end);
			start=addTime(end,new Time(0,1));// start= end+1 
			end=addTime(start,estimatedTime);
		}	
	}
}

class FixedTask extends Task{
	private Time startTime;
	private Time endTime; 
	private Time duration; 
	
	FixedTask(Time s, Time e){
		startTime=s;
		endTime=e;
		Task.increaseTaskIdentifier();
	}
	
	// t1-t2
	private Time substractTime (Time t1, Time t2){
		Time time =new Time(0,0);
		if(t1.getMinute()>=t2.getMinute()){
			time.setMinute( t1.getMinute()- t2.getMinute());
		}
		else{
			time.setMinute( t1.getMinute()+60- t2.getMinute());
			time.setHour(t1.getHour()-1-t2.getHour());
		}
		return time;
	}
	
	void initializeDomain(Time dayStart, Time dayEnd,Time step){
		dayStart= addTime(dayStart,step);
		duration=substractTime (endTime, startTime);
		Time start,end;
		start=dayStart;
		end=addTime(start,duration);
		
		while(start.compareTime(end) <0){
			addTimeSlice(start,end);
			start=addTime(end,new Time(0,1));// start= end+1 
			end=addTime(start,duration);
		}	
	}
}











