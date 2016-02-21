package com.algorithm;

import java.util.LinkedList;

/*
 * Class Task is used to describe a task and associated possible assigned time slice 
 * 
 */
public class Task {
	private static int taskIdentifier=0;// identifier of each task
	private float estimatedTime;// estimated amount of time to finish a task
	private LinkedList<Domain> domain;//a list of possible time slice 
	
	Task(){}
	Task(float time){
		taskIdentifier++;
		estimatedTime=time;
		domain=new LinkedList<Domain>();	
	}
	
	int getTaskIdentifier(){
		return taskIdentifier;
	}
	
	float getEstimatedTime(){
		return estimatedTime;
	}
	LinkedList<Domain> getDomain(){
		return domain;
	}
	
	void addTimeSlice(float start,float end){
		while(Float.compare(end-start, estimatedTime)>=0){//only add qualified time slice to domain
			TimeSlice time= new TimeSlice(start,end);
			domain.add(new Domain(time,true));
			start+=estimatedTime+0.01;
		}	
	}
	
	void removeTimeSlice(float start,float end){
	 //using either start or end time  to locate the time slice to be removed
		//Iterator it=domain.listIterator();
		for(int i=0;i< domain.size();i++){
			if((Float.compare(domain.get(i).getTimeSlice().getStartTime() , start) == 0)){// judge whether equal 
				domain.remove(new TimeSlice(start,end));
				break;
			}//if
		}//for
	}
}














