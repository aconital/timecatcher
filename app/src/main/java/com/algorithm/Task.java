package com.algorithm;

import java.util.LinkedList;

/*
 * Class Task is used to describe a task and associated possible assigned time slice 
 * 
 */
public class Task {
	private int taskIdentifier;// identifier of each task
	private float estimatedTime;// estimated amount of time to finish a task
	private LinkedList<TimeSlice> domain;//a list of possible time slice 
	
	Task(){}
	Task(int indetifier,float time){
		taskIdentifier=indetifier;
		estimatedTime=time;
		domain=new LinkedList<TimeSlice>();	
	}
	
	int getTaskIdentifier(){
		return taskIdentifier;
	}
	
	LinkedList<TimeSlice> getDomain(){
		return domain;
	}
	
	void addTimeSlice(float start,float end){
		if((end-start-estimatedTime)>0.000001){//only add qualified time slice to domain
			domain.add(new TimeSlice(start,end));
		}	
	}
	
	void removeTimeSlice(float start,float end){
	 //using either start or end time  to locate the time slice to be removed
		//Iterator it=domain.listIterator();
		for(int i=0;i< domain.size();i++){
			if((domain.get(i).getStartTime() - start)>0.000001){// judge whether equal 
				domain.remove(new TimeSlice(start,end));
				break;
			}//if
		}//for
	}
}














