package com.algorithm;
import java.util.*;

import com.algorithm.TimeSlice;

public class Domain {
	private Set<TimeSlice> domainSet;//a set of possible time slice 
	private ArrayList<TimeSlice> domainArrayList;//  possible time slice stored in an array list
	Domain(){
		domainSet= new HashSet<TimeSlice>();
	}
	
	Set<TimeSlice> getDomainSet(){
		return domainSet;
	}
	
	ArrayList<TimeSlice> getDomainArrayList(){
		return domainArrayList;
	}
	
	private void insertTimeSlice (Time start,Time end,boolean available){
		TimeSlice slice= new TimeSlice(start,end,available);
		domainSet.add(slice);	
	}//function 
	
	//for fixed task domain initialization 
	void initializeDomainSet(Time startTime,Time endTime){
		domainSet.clear();
		insertTimeSlice(startTime,endTime,true);
		domainArrayList=new ArrayList<TimeSlice>(domainSet);
	}//function 
	
	//for flexible task domain initialization 
	void initializeDomainSet(Time dayStart, Time dayEnd,Time duration,Time step){
		domainSet.clear();
		Time start,end,startPoint;
		end=new Time(dayStart);
		startPoint= new Time(dayStart);
		for(int i=1;(startPoint.addTime(duration)).compareTime(dayEnd) <=0 ;i++){	
			//System.out.println("startPoint  " + startPoint.getHour() + ":"+ startPoint.getMinute() );
			start=new Time(startPoint);
			end=start.addTime(duration);
			
			while(end.compareTime(dayEnd) <=0){
				//System.out.println("time  " + start.getHour() + ":"+ start.getMinute() +
				//					" -- " + end.getHour() + ":"+ end.getMinute());
				insertTimeSlice(start,end,true);
				start=new Time(end);
				end=start.addTime(duration);
			}//while
			
			if(i!=0){
				startPoint=startPoint.addTime(step);
			}//if
			 //System.out.println("---------------------------------------------------------" );
		}//for
		domainArrayList=new ArrayList<TimeSlice>(domainSet);
	}//function	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((domainSet == null) ? 0 : domainSet.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final Domain other = (Domain) obj;
		if (domainSet == null) {
			if (other.domainSet != null)
				return false;
		} else if (!domainSet.equals(other.domainSet))
			return false;
		return true;
	}
	
	
}

