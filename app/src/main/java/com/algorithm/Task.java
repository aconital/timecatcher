package com.algorithm;

import java.util.*;

/*
 * Class Task is used to describe a task and associated possible assigned time slice 
 * 
 */
public class Task {
	protected Domain domain;//a list of possible time slice
	
	Task() {
		domain = new Domain();
	}

	
	Set<TimeSlice> getDomainSet() {
		return domain.getDomainSet();
	}
	
	List<TimeSlice> getDomainArrayList() {
		return domain.getDomainArrayList();
	}
	
	int getTaskId() {
		return -1;
	}
	
	Time getDuration() {
		return new Time (0,0);
	}
	
	void initializeDomainSet(Time dayStart,Time dayEnd, Time step) {}// overridden by subclass
	//void initializeDomainSet() {}// overridden by subclass
}

class FlexibleTask extends Task {
	private int taskId; 
	private Time duration;// planed working time for this flexible task
	
	FlexibleTask(Time duration, int taskId) {
		domain = new Domain();
		this.duration = duration;
		this.taskId = taskId;
	}
	
	int getTaskId() {
		return taskId;
	}
	
	Time getDuration() {
		return duration;
	}

	void initializeDomainSet(Time dayStart, Time dayEnd, Time step) {
		domain.initializeDomainSet(dayStart, dayEnd, duration, step);
	}	
}

class FixedTask extends Task {
	private Time startTime;
	private Time endTime;
	private int taskId; 
	
	FixedTask(Time s, Time e, int taskId) {
		if(s.compareTime(e) < 0) {
			domain = new Domain();
			startTime = s;
			endTime = e;
			this.taskId = taskId;
			domain.initializeDomainSet(startTime, endTime);
		}
	}
	
	Time getDuration() {
		return endTime.subtractTime(startTime);
	}
	int getTaskId() {
		return taskId;
	}
}











