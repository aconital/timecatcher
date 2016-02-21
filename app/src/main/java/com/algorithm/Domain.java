package com.algorithm;
import com.algorithm.TimeSlice;

public class Domain {
	private TimeSlice time;
	private boolean available; 
	
	Domain(){}
	Domain(TimeSlice t, boolean a){
		time=t;
		available=a;
	}
	
	void setTime(TimeSlice t){
		time=t;
	}
	
	void setAvailable(boolean a){
		available=a;
	}
	
	TimeSlice getTimeSlice(){
		return time;
	}
	
	boolean getAvailable(){
		return available;
	}
}
