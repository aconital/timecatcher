package com.algorithm;

import java.util.*;

/*
 * Class Task is used to describe a task and associated possible assigned time slice 
 * 
 */
public class Task {
	private static int taskIdentifier=0;// identifier of each task
	protected Domain domain;//a list of possible time slice 
	
	Task(){
		domain=new Domain();
	}
	
	protected static void increaseTaskIdentifier(){
		taskIdentifier++;
	}
	
	int getTaskIdentifier(){
		return taskIdentifier;
	}
	
	Set<TimeSlice> getDomain(){
		return domain.getDomainSet();
	}
	
	void initializeDomainSet(Time dayStart, Time dayEnd,Time step) { 
		//System.out.println(" call at here super Task ");
	}
	void initializeDomainSet(){}
}

class FlexibleTask extends Task{
	private Time estimatedTime;// estimated amount of time to finish a task
	FlexibleTask(Time t){
		domain=new Domain();
		estimatedTime=t;
		Task.increaseTaskIdentifier();
	}

	void initializeDomainSet(Time dayStart, Time dayEnd,Time step){
		domain.initializeDomainSet(dayStart, dayEnd, estimatedTime, step);
		//System.out.println(" call at here flexible ");
	}
}

class FixedTask extends Task{
	private Time startTime;
	private Time endTime; 
	
	FixedTask(Time s, Time e){
		domain=new Domain();
		startTime=s;
		endTime=e;
		Task.increaseTaskIdentifier();
	}
	
	void initializeDomainSet(){
		domain.initializeDomainSet(startTime, endTime);
	}
}











