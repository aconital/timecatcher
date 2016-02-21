package com.algorithm;

public class TimeSlice{
	private float startTime;// float 9.45 means 9.45am; 
	private float endTime; // float 13.20 means 1.20pm;

	TimeSlice(float start,float end){
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
