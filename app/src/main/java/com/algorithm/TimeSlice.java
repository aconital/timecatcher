package com.algorithm;

public class TimeSlice{
	
	private Time startTime;
	private Time endTime; 
	
	TimeSlice(Time start,Time end){
		if(start.compareTime(end) <=0){
			startTime=start;
			endTime=end;
		}
	}
	Time getStartTime(){return startTime;}
	Time getEndTime(){return endTime;}
	
	void setTimeSlice(Time start,Time end){
		startTime=start;
		endTime=end;
	}
	
	/*
	 * check if calling object timeSlice slice1 is before argument slice
	 * result 1  => slice1 is before slice 
	 * return -1 => overlapping or slice1 is 
	 */
	int isBefore(TimeSlice slice){
		if(endTime.compareTime(slice.getStartTime()) <0){
			return 1;
		}
		else{
			return -1;
		}
	}
}
