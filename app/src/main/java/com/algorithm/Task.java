package com.algorithm;

import java.util.LinkedList;

/*
 * Class Task is used to describe a task and associated possible assigned time slice 
 * 
 */
public class Task {
	private int taskIdentifier;// identifier of each task
	private LinkedList<Domain> domain;//a list of possible time slice 
	
	class Domain{
		private float startTime;// float 9.45 means 9.45am; 
		private float endTime; // float 13.20 means 1.20pm;

		Domain(float start,float end){
			startTime=start;
			endTime=end;
		}
		float getStartTime(){return startTime;}
		float getEndTime(){return endTime;}
		void setTimeSlice(float start,float end){
			startTime=start;
			endTime=end;
		}
	}
	
	Task(int indetifier){
		taskIdentifier=indetifier;
		domain=new LinkedList<Domain>();	
	}
	
	int getTaskIndetifier(){
		return taskIdentifier;
	}
	
	LinkedList<Domain> getDomain(){
		return domain;
	}
	
	void addTimeSlice(float start,float end){
		domain.add(new Domain(start,end));
	}
	
	void removeTimeSlice(float start,float end){
	 //using either start or end time  to locate the time slice to be removed
		//Iterator it=domain.listIterator();
		for(int i=0;i< domain.size();i++){
			if((domain.get(i).getStartTime() - start)>0.000001){// judge whether equal 
				domain.remove(new Domain(start,end));
				break;
			}//if
		}//for
	}
}














