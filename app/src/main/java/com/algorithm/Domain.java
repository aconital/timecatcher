package com.algorithm;
import java.util.*;

import com.algorithm.TimeSlice;

public class Domain {
	private Set<TimeSlice> domainSet;//a set of possible time slice 
	
	Domain(){
		domainSet= new HashSet<TimeSlice>();
	}
	
	Set<TimeSlice> getDomainSet(){
		return domainSet;
	}
	
	private void insertTimeSlice (Time start,Time end,boolean available){
		TimeSlice slice= new TimeSlice(start,end,available);
		domainSet.add(slice);	
	}//function 
	
	
	//for fixed task domain initialization 
	void initializeDomainSet(Time startTime,Time endTime){
		domainSet.clear();
		insertTimeSlice(startTime,endTime,true);
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
	}//function	
}

